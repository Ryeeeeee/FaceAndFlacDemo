package com.ryeeeeee.faceandflacdemo.flac;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ryeeeeee.faceandflacdemo.R;

import java.io.File;
import java.io.IOException;

public class FlacFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = FlacFragment.class.getSimpleName();

    private static Button mPlayButton;
    private static Button mRecordButton;
    private final String mFilePath = Environment.getExternalStorageDirectory() + "/test.flac";
    private MediaPlayer mMediaPlayer;
    private boolean mInRecordState = false;
    private boolean mInPlayState = false;
    private Context mContext;
    private FlacRecorder mFlacRecorder;

    public FlacFragment() { }

    public static FlacFragment newInstance() {
        FlacFragment fragment = new FlacFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_flac, container, false);
        mPlayButton = (Button) root.findViewById(R.id.play_button);
        mRecordButton = (Button) root.findViewById(R.id.record_button);

        mPlayButton.setOnClickListener(this);
        mRecordButton.setOnClickListener(this);
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.record_button:
                onRecordButtonClicked();
                break;
            case R.id.play_button:
                onPlayButtonClicked();
                break;
            default:
        }
    }

    private void onRecordButtonClicked() {
        if (!mInRecordState) {
            mInRecordState = true;
            File file = new File(mFilePath);
            if (file.exists()) {
                file.delete();
            }
            mFlacRecorder = new FlacRecorder(mFilePath, new FlacRecorderListener() {
                @Override
                public void onStart() {
                    mRecordButton.setText(R.string.stop);
                    mPlayButton.setEnabled(false);
                }

                @Override
                public void onStop() {
                    mInRecordState = false;
                    mRecordButton.setText(R.string.record);
                    mRecordButton.setEnabled(true);
                    mPlayButton.setEnabled(true);
                }
            });
            mFlacRecorder.start();
        } else {
            mFlacRecorder.stop();
            mRecordButton.setEnabled(false);
        }
    }

    private void onPlayButtonClicked() {
        if (!mInPlayState) {
            try {
                mInPlayState = true;
                mPlayButton.setText(R.string.stop);
                mRecordButton.setEnabled(false);

                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setDataSource(mFilePath);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        onPlayEnded();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(mContext.getApplicationContext(), R.string.file_invalid, Toast.LENGTH_SHORT).show();
            }
        } else {
            mMediaPlayer.stop();
            onPlayEnded();
        }
    }

    private void onPlayEnded() {
        mInPlayState = false;
        mPlayButton.setText(R.string.play);
        mRecordButton.setEnabled(true);
    }
}
