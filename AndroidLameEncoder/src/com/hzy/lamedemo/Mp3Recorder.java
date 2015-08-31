package com.hzy.lamedemo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.hzy.lame.LameEncoder;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;

public class Mp3Recorder implements Runnable {

	private static final String TAG = "Mp3Recorder";
	public int DEFAULT_SAMPLE_RATE = 8000;

	private static Mp3Recorder mInstance;

	private int mSampleRate;
	private OutputStream mOutputStream;
	private boolean mIsRunning;
	private int mMinBufferSize;
	private AudioRecord mAudioRecorder;
	private int mChannelConfig = AudioFormat.CHANNEL_IN_MONO;
	private int mAudioFormat = AudioFormat.ENCODING_PCM_16BIT;

	private Mp3Recorder() {
		mIsRunning = false;
		mSampleRate = DEFAULT_SAMPLE_RATE;
		mMinBufferSize = AudioRecord.getMinBufferSize(mSampleRate, mChannelConfig, mAudioFormat);
	}

	public static Mp3Recorder getInstance() {
		if (mInstance == null) {
			synchronized (Mp3Recorder.class) {
				if (mInstance == null) {
					mInstance = new Mp3Recorder();
				}
			}
		}
		return mInstance;
	}

	public boolean isRunning() {
		return mIsRunning;
	}

	public void start() {
		if (!isRunning()) {
			new Thread(this).start();
		}
	}

	public void stop() {
		mIsRunning = false;
	}

	public void setSampleRate(int sampleRate) {
		if (sampleRate > 0 && !isRunning()) {
			this.mSampleRate = sampleRate;
			mMinBufferSize = AudioRecord.getMinBufferSize(mSampleRate, mChannelConfig, mAudioFormat);
		}
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

	@Override
	public void run() {
		if (!isValidParams()) {
			return;
		}

		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
		mAudioRecorder = new AudioRecord(AudioSource.MIC, mSampleRate, mChannelConfig, mAudioFormat, mMinBufferSize * 2);
		short[] buffer = new short[mSampleRate * (16 / 8) * 1 * 5];
		byte[] mp3buffer = new byte[(int) (7200 + buffer.length * 2 * 1.25)];
		LameEncoder.init(mSampleRate, 1, mSampleRate, 32);
		mIsRunning = true;
		try {
			mAudioRecorder.startRecording();
		} catch (IllegalStateException e) {
		}
		try {
			int readSize = 0;
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
				}
			}
			try {
				mOutputStream.close();
			} catch (IOException e) {
			}
		} finally {
			mAudioRecorder.stop();
			mAudioRecorder.release();
		}
		LameEncoder.close();
		mIsRunning = false;
	}

	private boolean isValidParams() {
		if (mSampleRate <= 0)
			return false;
		if (mMinBufferSize < 0)
			return false;
		if (mOutputStream == null)
			return false;
		return true;
	}
}
