package com.appkefu.appkehu_2;

import com.appkefu.lib.ChatViewActivity;
import com.appkefu.lib.service.UsernameAndKefu;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class TravelTicketsActivity extends Activity {

	private static final String TAG = TravelTicketsActivity.class.getSimpleName();
	private static final int LOGIN_REQUEST_CODE = 1;	
	private static final String SERIAL_KEY = "com.appkefu.lib.username.serialize";
	private AppApplication app;
	
	private ImageButton chatButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		app = (AppApplication)getApplication();
		
		chatButton = (ImageButton)findViewById(R.id.intercom_select);
		chatButton.setOnClickListener(listener);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "onStart");
		
		if(!app.isConnected())
		{
			Log.d(TAG, "start login");
			Intent login = new Intent(this, LoginActivity.class);
			startActivityForResult(login, LOGIN_REQUEST_CODE);
		}
		else
		{
			Log.d(TAG, "already logged in");
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == LOGIN_REQUEST_CODE) {
			
			if (resultCode == Activity.RESULT_OK) 
			{
				Log.d(TAG, "Activity.RESULT_OK");
				app.setConnected(true);

			}
			else if (resultCode == Activity.RESULT_CANCELED) 
			{
				Log.d(TAG, "Activity.RESULT_CANCELED");
				app.setConnected(false);
				

				//请检查网络链接、appkey是否填写正确
				Toast.makeText(this, "链接服务器失败", Toast.LENGTH_LONG).show();
			}
		} 
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(TAG, "onRestart");
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
		
	}
	
	@Override
	protected void onStop() {
		super.onStop();

		Log.d(TAG, "onStop");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
		
	}
	
	private OnClickListener listener = new OnClickListener() {	    
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
        	switch(v.getId()){
        	case R.id.intercom_select:
        		startChat("testusername","admin");
        		break;
        	default:
        		break;
        	}
        }
	};
	
	private void startChat(String username, String kefuName) {
		
		String jid = kefuName + "@appkefu.com";
		Intent intent = new Intent(this, ChatViewActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		UsernameAndKefu usernameAndKefu = new UsernameAndKefu();
		usernameAndKefu.setUsername(username);
		usernameAndKefu.setKefuJID(jid);
		
		Bundle mbundle = new Bundle();
		mbundle.putSerializable(SERIAL_KEY, usernameAndKefu);
		intent.putExtras(mbundle);
			
		startActivity(intent);	
    }

}

















