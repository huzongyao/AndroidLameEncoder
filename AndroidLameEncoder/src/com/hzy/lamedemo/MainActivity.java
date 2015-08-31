package com.hzy.lamedemo;

import com.hzy.lamedemo.R.id;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	private Button btnRecord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnRecord = (Button) findViewById(id.button_mp3_recorder);
		btnRecord.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case id.button_mp3_recorder:
			startActivity(new Intent(this, RecordActivity.class));
			break;

		default:
			break;
		}
	}
}
