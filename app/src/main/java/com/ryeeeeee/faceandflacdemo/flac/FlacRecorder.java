package com.ryeeeeee.faceandflacdemo.flac;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;

/**
 * Created by Ryeeeeee on 1/14/16.
 */
public class FlacRecorder {

    private static final String TAG = FlacRecorder.class.getSimpleName();

    public static final int MSG_ON_STOP = 1;

    private static final int DEFAULT_AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
    private static final int DEFAULT_SAMPLING_RATE = 44100;
    private static final int DEFAULT_CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int DEFAULT_CHANNEL_COUNT = 1;
    private static final int DEFAULT_AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final int DEFAULT_BYTES_PER_FRAME = 2;
    private static final int DEFAULT_BITS_PER_FRAME = DEFAULT_BYTES_PER_FRAME * 8;
    private static final int FRAME_COUNT = 160;

    private Handler mEncoderHandler;
    private String mFilePath;
    private AudioRecord mAudioRecord;
    private int mBufferSize;
    private short[] mPCMBuffer;
    private EncodeThread mEncodeThread;
    private FlacRecorderListener mListener;
    private FlacHandler mHandler = new FlacHandler(Looper.getMainLooper(), this);

    private boolean mIsRecording = false;

    public FlacRecorder(String filePath, FlacRecorderListener listener) {
        mFilePath = filePath;
        mListener = listener;
        mBufferSize = AudioRecord.getMinBufferSize(DEFAULT_SAMPLING_RATE, DEFAULT_CHANNEL_CONFIG, DEFAULT_AUDIO_FORMAT);
        int bytesPerFrame = DEFAULT_BYTES_PER_FRAME;
        int frameSize = mBufferSize / bytesPerFrame;
        if (frameSize % FRAME_COUNT != 0) {
            frameSize += (FRAME_COUNT - frameSize % FRAME_COUNT);
            mBufferSize = frameSize * bytesPerFrame;
        }
        mPCMBuffer = new short[mBufferSize];
        mAudioRecord = new AudioRecord(DEFAULT_AUDIO_SOURCE, DEFAULT_SAMPLING_RATE, DEFAULT_CHANNEL_CONFIG,
                DEFAULT_AUDIO_FORMAT, mBufferSize);
    }

    public void start() {
        Log.d(TAG, "start() called with: " + "");
        notifyOnStart();
        if (!FlacEncoder.init(mFilePath, DEFAULT_SAMPLING_RATE, DEFAULT_CHANNEL_COUNT, DEFAULT_BITS_PER_FRAME)) {
            Log.e(TAG, "start: Failed to init FlacEncoder");
            notifyOnStop();
            return;
        }

        if (mIsRecording) {
            notifyOnStop();
            return;
        }

        try {
            mEncodeThread = new EncodeThread(mHandler);
            mEncodeThread.start();
            mEncoderHandler = new EncodeThread.EncodeHandler(mEncodeThread.getLooper(), mEncodeThread);
            mAudioRecord.setRecordPositionUpdateListener(mEncodeThread, mEncoderHandler);
            mAudioRecord.setPositionNotificationPeriod(FRAME_COUNT);
            new Thread() {
                @Override
                public void run() {
                    mIsRecording = true;
                    Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
                    mAudioRecord.startRecording();
                    while (mIsRecording) {
                        int size = mAudioRecord.read(mPCMBuffer, 0, mBufferSize);
                        if (size > 0) {
                            Log.d(TAG, "run: VALID");
                            mEncodeThread.addTask(new Task(mPCMBuffer, mBufferSize));
                        }
                    }

                    mAudioRecord.stop();
                    mAudioRecord.release();

                    mEncoderHandler.sendEmptyMessage(EncodeThread.MSG_PROCESS_STOP);
                }
            }.start();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void notifyOnStart() {
        if (mListener != null) {
            mListener.onStart();
        }
    }

    private void notifyOnStop() {
        if (mListener != null) {
            mListener.onStop();
        }
    }

    public void stop() {
        mIsRecording = false;
    }

    private static class FlacHandler extends Handler {
        WeakReference<FlacRecorder> mReference;

        public FlacHandler(Looper looper, FlacRecorder recorder) {
            super(looper);
            mReference = new WeakReference<>(recorder);
        }

        @Override
        public void handleMessage(Message msg) {
            FlacRecorder recorder = mReference.get();
            if (recorder == null) {
                return;
            }

            switch (msg.what) {
                case MSG_ON_STOP:
                    recorder.notifyOnStop();
                    break;
                default:
            }
        }
    }
}
