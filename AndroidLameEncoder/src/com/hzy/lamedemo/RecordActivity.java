package com.hzy.lamedemo;

import java.io.File;

import com.hzy.lamedemo.R.id;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RecordActivity extends Activity implements OnClickListener{
	
	private Mp3Recorder mMp3Recorder;
	private String mMp3Path;
	private Button mBtnStart;
	private Button mBtnStop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		initEnvironment();
		initViews();
	}

	private void initEnvironment() {
		// TODO Auto-generated method stub
		mMp3Recorder = Mp3Recorder.getInstance();
		mMp3Path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + CommonUtils.generateMp3FileName();
		mMp3Recorder.setOutputFilePath(mMp3Path);
	}

	private void initViews() {
		// TODO Auto-generated method stub
		mBtnStart = (Button)findViewById(id.button_start);
		mBtnStop = (Button)findViewById(id.button_stop);
		
		mBtnStart.setOnClickListener(this);
		mBtnStop.setOnClickListener(this);
		mBtnStop.setEnabled(false);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case id.button_start:
			mMp3Recorder.start();
			mBtnStart.setEnabled(false);
			mBtnStop.setEnabled(true);
			break;
			
		case id.button_stop:
			mMp3Recorder.stop();
			mBtnStart.setEnabled(true);
			mBtnStop.setEnabled(false);
			break;
			
		default:
			break;
		}
	}

}
