package com.ryeeeeee.faceandflacdemo.flac;

import android.media.AudioRecord;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Ryeeeeee on 1/14/16.
 */
public class EncodeThread extends HandlerThread implements AudioRecord.OnRecordPositionUpdateListener {
    private static final String TAG = EncodeThread.class.getSimpleName();

    public static final int MSG_PROCESS_STOP = 1;

    private Handler mCallbackHandler;
    private ConcurrentLinkedQueue<Task> mQueue = new ConcurrentLinkedQueue<>();

    public EncodeThread(Handler handler) throws FileNotFoundException {
        super("EncodeThread");
        mCallbackHandler = handler;
    }

    @Override
    public void onMarkerReached(AudioRecord audioRecord) { }

    @Override
    public void onPeriodicNotification(AudioRecord audioRecord) {
        processData();
    }

    public void addTask(Task task) {
        mQueue.add(task);
    }

    public boolean processData() {
        Log.d(TAG, "processData() called with: " + "");
        Task task = mQueue.poll();
        if (task != null) {
            Log.d(TAG, "processData() called with: task not null");
            FlacEncoder.write(task.rawData, task.readSize);
        }
        return false;
    }

    public void flushAndRelease() {
        FlacEncoder.flush();
        FlacEncoder.deinit();
    }

    static class EncodeHandler extends Handler {

        WeakReference<EncodeThread> mEncodeThread;

        public EncodeHandler(Looper looper, EncodeThread encodeThread) {
            super(looper);
            mEncodeThread = new WeakReference<>(encodeThread);
        }

        @Override
        public void handleMessage(Message msg) {
            EncodeThread encodeThread = mEncodeThread.get();
            if (encodeThread == null) {
                return;
            }

            switch (msg.what) {
                case MSG_PROCESS_STOP:
                    while (encodeThread.processData());
                    // Cancel any event left in the queue
                    removeCallbacksAndMessages(null);
                    encodeThread.flushAndRelease();
                    encodeThread.mCallbackHandler.sendEmptyMessage(FlacRecorder.MSG_ON_STOP);
                    getLooper().quit();
                    break;
            }
        }
    }
}
