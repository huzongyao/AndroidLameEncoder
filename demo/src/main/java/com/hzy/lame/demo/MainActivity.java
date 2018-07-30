package com.hzy.lame.demo;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;

public class MainActivity extends AppCompatActivity {

    private Button mButtonStart;
    private Mp3Recorder mMp3Recorder;
    private boolean mIsRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMp3Recorder = Mp3Recorder.INSTANCE;
        setContentView(R.layout.activity_main);
        mButtonStart = findViewById(R.id.button_start);
        mButtonStart.setOnClickListener(v -> switchRecorder());
    }

    private void switchRecorder() {
        if (mIsRunning) {
            stopAudioRecorder();
        } else {
            startAudioRecorder();
        }
    }

    private void stopAudioRecorder() {
        mMp3Recorder.stop();
        mIsRunning = false;
        mButtonStart.setText(R.string.start);
    }

    private void startAudioRecorder() {
        PermissionUtils.permission(Manifest.permission.RECORD_AUDIO)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        String mp3Path = CommonUtils.getMp3FilePath(Utils.getApp());
                        mMp3Recorder.setOutputFilePath(mp3Path);
                        mMp3Recorder.start();
                        mIsRunning = true;
                        mButtonStart.setText(R.string.stop);
                    }

                    @Override
                    public void onDenied() {
                        ToastUtils.showShort(R.string.permission_error);
                    }
                }).request();
    }
}
