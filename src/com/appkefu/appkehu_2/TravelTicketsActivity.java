package com.appkefu.appkehu_2;

import org.jivesoftware.smack.util.StringUtils;

import com.appkefu.lib.interfaces.KFInterfaces;
import com.appkefu.lib.service.KFMainService;
import com.appkefu.lib.service.KFSettingsManager;
import com.appkefu.lib.service.KFXmppManager;
import com.appkefu.lib.utils.KFSLog;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class TravelTicketsActivity extends Activity {

	/*
	 提示：如果已经运行过旧版的Demo，请先在手机上删除原先的App再重新运行此工程
	 更多使用帮助参见：http://appkefu.com/AppKeFu/tutorial-android.html
	
	 简要使用说明：
	 第1步：到http://appkefu.com/AppKeFu/admin/，注册/创建应用/分配客服，并将获取的appkey填入AnroidManifest.xml
	 		中的com.appkefu.lib.appkey
	 第2步：用真实的客服名初始化mKefuUsername
	 第3步：调用 KFInterfaces.visitorLogin(this); 函数登录
	 第4步：调用chatWithKeFu(mKefuUsername);与客服会话，其中mKefuUsername需要替换为真实客服名
	 第5步：(可选)
     	//设置昵称，否则在客服客户端 看到的会是一串字符串(必须在登录成功之后才能调用，才有效)
     	KFInterfaces.setVisitorNickname("访客1", this);
	 */
	
	private ImageButton chatButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		chatButton = (ImageButton)findViewById(R.id.intercom_select);
		chatButton.setOnClickListener(listener);
		
		//设置开发者调试模式，默认为true，如要关闭开发者模式，请设置为false
		KFSettingsManager.getSettingsManager(this).setDebugMode(true);
		//第一步：登录
		KFInterfaces.visitorLogin(this);		
	}

	@Override
	protected void onStart() {
		super.onStart();
		KFSLog.d("onStart");
		
		IntentFilter intentFilter = new IntentFilter();
		//监听网络连接变化情况
        intentFilter.addAction(KFMainService.ACTION_XMPP_CONNECTION_CHANGED);
        //监听消息
        intentFilter.addAction(KFMainService.ACTION_XMPP_MESSAGE_RECEIVED);

        registerReceiver(mXmppreceiver, intentFilter); 
	}


	@Override
	protected void onStop() {
		super.onStop();

		KFSLog.d("onStop");
		
        unregisterReceiver(mXmppreceiver);
	}
	
	
	private OnClickListener listener = new OnClickListener() {	    
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
        	switch(v.getId()){
        	case R.id.intercom_select:
        		chatWithKeFu("admin");
        		break;
        	default:
        		break;
        	}
        }
	};
	
	//启动咨询对话框
	private void chatWithKeFu(String kefuUsername)
	{
		KFInterfaces.startChatWithKeFu(this,
				kefuUsername, //客服用户名
				"您好，我是微客服小秘书，请问有什么可以帮您的?",  //问候语
				"咨询客服");//会话窗口标题
	}
	
	//监听：连接状态、即时通讯消息、客服在线状态
	private BroadcastReceiver mXmppreceiver = new BroadcastReceiver() 
	{
        public void onReceive(Context context, Intent intent) 
        {
            String action = intent.getAction();
            //监听：连接状态
            if (action.equals(KFMainService.ACTION_XMPP_CONNECTION_CHANGED))//监听链接状态
            {
                updateStatus(intent.getIntExtra("new_state", 0));        
            }
            //监听：即时通讯消息
            else if(action.equals(KFMainService.ACTION_XMPP_MESSAGE_RECEIVED))//监听消息
            {
            	String body = intent.getStringExtra("body");
            	String from = StringUtils.parseName(intent.getStringExtra("from"));
            	
            	KFSLog.d("body:"+body+" from:"+from);
            }
        }
    };


  //根据监听到的连接变化情况更新界面显示
    private void updateStatus(int status) {

    	switch (status) {
            case KFXmppManager.CONNECTED:
            	KFSLog.d("connected");
            	//mTitle.setText("微客服(客服Demo)");

        		//设置昵称，否则在客服客户端 看到的会是一串字符串(必须在登录成功之后才能调用，才有效)
        		//KFInterfaces.setVisitorNickname("访客1", this);

                break;
            case KFXmppManager.DISCONNECTED:
            	KFSLog.d("disconnected");
            	//mTitle.setText("微客服(客服Demo)(未连接)");
                break;
            case KFXmppManager.CONNECTING:
            	KFSLog.d("connecting");
            	//mTitle.setText("微客服(客服Demo)(登录中...)");
            	break;
            case KFXmppManager.DISCONNECTING:
            	KFSLog.d("connecting");
            	//mTitle.setText("微客服(客服Demo)(登出中...)");
                break;
            case KFXmppManager.WAITING_TO_CONNECT:
            case KFXmppManager.WAITING_FOR_NETWORK:
            	KFSLog.d("waiting to connect");
            	//mTitle.setText("微客服(客服Demo)(等待中)");
                break;
            default:
                throw new IllegalStateException();
        }
    }
	
	

}

















