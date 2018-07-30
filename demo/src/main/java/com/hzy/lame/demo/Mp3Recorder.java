package com.hzy.lame.demo;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;

import com.hzy.lame.LameEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public enum Mp3Recorder {
    INSTANCE;

    public int DEFAULT_SAMPLE_RATE = 8000;
    private int mSampleRate;
    private OutputStream mOutputStream;
    private boolean mIsRunning;
    private int mMinBufferSize;
    private AudioRecord mAudioRecorder;
    private int mChannelConfig = AudioFormat.CHANNEL_IN_MONO;
    private int mAudioFormat = AudioFormat.ENCODING_PCM_16BIT;

    Mp3Recorder() {
        mSampleRate = DEFAULT_SAMPLE_RATE;
        mMinBufferSize = AudioRecord.getMinBufferSize(mSampleRate, mChannelConfig, mAudioFormat);
    }

    public boolean isRunning() {
        return mIsRunning;
    }

    public void start() {
        if (!isRunning()) {
            new Thread() {
                @Override
                public void run() {
                    startRecorder();
                }
            }.start();
        }
    }

    public void stop() {
        mIsRunning = false;
    }

    public void setOutputStream(OutputStream os) {
        if (!isRunning())
            this.mOutputStream = os;
    }

    public void setOutputFile(File f) {
        if (f != null && f.isFile() && f.canWrite()) {
            try {
                setOutputStream(new FileOutputStream(f));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void setOutputFilePath(String path) {
        File f = new File(path);
        File parentDir = f.getParentFile();
        if (!parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                return;
            }
        }
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        setOutputFile(f);
    }

    private void startRecorder() {
        if (!isValidParams()) {
            return;
        }
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
        mAudioRecorder = new AudioRecord(AudioSource.MIC, mSampleRate, mChannelConfig,
                mAudioFormat, mMinBufferSize * 2);
        short[] buffer = new short[mSampleRate * (16 / 8) * 5];
        byte[] mp3buffer = new byte[(int) (7200 + buffer.length * 2 * 1.25)];
        LameEncoder.init(mSampleRate, 1, mSampleRate, 32);
        mIsRunning = true;
        try {
            mAudioRecorder.startRecording();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        try {
            int readSize;
            while (mIsRunning) {
                readSize = mAudioRecorder.read(buffer, 0, mMinBufferSize);
                if (readSize < 0) {
                    break;
                } else if (readSize == 0) {
                } else {
                    int encResult = LameEncoder.encode(buffer, buffer, readSize, mp3buffer);
                    if (encResult < 0) {
                        break;
                    }
                    if (encResult != 0) {
                        try {
                            mOutputStream.write(mp3buffer, 0, encResult);
                        } catch (IOException e) {
                            break;
                        }
                    }
                }
            }
            int flushResult = LameEncoder.flush(mp3buffer);
            if (flushResult < 0) {
            }
            if (flushResult != 0) {
                try {
                    mOutputStream.write(mp3buffer, 0, flushResult);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                mOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                mAudioRecorder.stop();
                mAudioRecorder.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LameEncoder.close();
        mIsRunning = false;
    }

    private boolean isValidParams() {
        return mSampleRate > 0
                && mMinBufferSize >= 0
                && mOutputStream != null;
    }
}
