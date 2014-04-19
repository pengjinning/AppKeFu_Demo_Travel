package com.appkefu.appkehu_2;

import org.jivesoftware.smack.util.StringUtils;

import com.appkefu.lib.interfaces.KFInterfaces;
import com.appkefu.lib.service.KFMainService;
import com.appkefu.lib.service.KFSettingsManager;
import com.appkefu.lib.service.KFXmppManager;
import com.appkefu.lib.utils.KFLog;

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
	 ��ʾ������Ѿ����й��ɰ��Demo���������ֻ���ɾ��ԭ�ȵ�App���������д˹���
	 ����ʹ�ð����μ���http://appkefu.com/AppKeFu/tutorial-android.html
	
	 ��Ҫʹ��˵����
	 ��1������http://appkefu.com/AppKeFu/admin/��ע��/����Ӧ��/����ͷ���������ȡ��appkey����AnroidManifest.xml
	 		�е�com.appkefu.lib.appkey
	 ��2��������ʵ�Ŀͷ�����ʼ��mKefuUsername
	 ��3�������� KFInterfaces.visitorLogin(this); ������¼
	 ��4��������chatWithKeFu(mKefuUsername);��ͷ��Ự������mKefuUsername��Ҫ�滻Ϊ��ʵ�ͷ���
	 ��5����(��ѡ)
     	//�����ǳƣ������ڿͷ��ͻ��� �����Ļ���һ���ַ���(�����ڵ�¼�ɹ�֮����ܵ��ã�����Ч)
     	KFInterfaces.setVisitorNickname("�ÿ�1", this);
	 */
	
	private ImageButton chatButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		chatButton = (ImageButton)findViewById(R.id.intercom_select);
		chatButton.setOnClickListener(listener);

		//���ÿ����ߵ���ģʽ��Ĭ��Ϊfalse����Ҫ����������ģʽ��������Ϊtrue
		KFInterfaces.enableDebugMode(this, true);
		//��һ������¼
		KFInterfaces.visitorLogin(this);		
	}

	@Override
	protected void onStart() {
		super.onStart();
		KFLog.dd("onStart");
		
		IntentFilter intentFilter = new IntentFilter();
		//�����������ӱ仯���
        intentFilter.addAction(KFMainService.ACTION_XMPP_CONNECTION_CHANGED);
        //������Ϣ
        intentFilter.addAction(KFMainService.ACTION_XMPP_MESSAGE_RECEIVED);

        registerReceiver(mXmppreceiver, intentFilter); 
	}


	@Override
	protected void onStop() {
		super.onStop();

		KFLog.dd("onStop");
		
        unregisterReceiver(mXmppreceiver);
	}
	
	
	private OnClickListener listener = new OnClickListener() {	    
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
        	switch(v.getId()){
        	case R.id.intercom_select:
        		chatWithKeFu("your_kefu_username");
        		break;
        	default:
        		break;
        	}
        }
	};
	
	//������ѯ�Ի���
	private void chatWithKeFu(String kefuUsername)
	{
		KFInterfaces.startChatWithKeFu(this,
				kefuUsername, //�ͷ��û���
				"���ã�����΢�ͷ�С���飬������ʲô���԰�����?",  //�ʺ���
				"��ѯ�ͷ�");//�Ự���ڱ���
	}
	
	//����������״̬����ʱͨѶ��Ϣ���ͷ�����״̬
	private BroadcastReceiver mXmppreceiver = new BroadcastReceiver() 
	{
        public void onReceive(Context context, Intent intent) 
        {
            String action = intent.getAction();
            //����������״̬
            if (action.equals(KFMainService.ACTION_XMPP_CONNECTION_CHANGED))//��������״̬
            {
                updateStatus(intent.getIntExtra("new_state", 0));        
            }
            //��������ʱͨѶ��Ϣ
            else if(action.equals(KFMainService.ACTION_XMPP_MESSAGE_RECEIVED))//������Ϣ
            {
            	String body = intent.getStringExtra("body");
            	String from = StringUtils.parseName(intent.getStringExtra("from"));
            	
            	KFLog.dd("body:"+body+" from:"+from);
            }
        }
    };


  //���ݼ����������ӱ仯������½�����ʾ
    private void updateStatus(int status) {

    	switch (status) {
            case KFXmppManager.CONNECTED:
            	KFLog.dd("connected");
            	//mTitle.setText("΢�ͷ�(�ͷ�Demo)");

        		//�����ǳƣ������ڿͷ��ͻ��� �����Ļ���һ���ַ���(�����ڵ�¼�ɹ�֮����ܵ��ã�����Ч)
        		//KFInterfaces.setVisitorNickname("�ÿ�1", this);

                break;
            case KFXmppManager.DISCONNECTED:
            	KFLog.dd("disconnected");
            	//mTitle.setText("΢�ͷ�(�ͷ�Demo)(δ����)");
                break;
            case KFXmppManager.CONNECTING:
            	KFLog.dd("connecting");
            	//mTitle.setText("΢�ͷ�(�ͷ�Demo)(��¼��...)");
            	break;
            case KFXmppManager.DISCONNECTING:
            	KFLog.dd("connecting");
            	//mTitle.setText("΢�ͷ�(�ͷ�Demo)(�ǳ���...)");
                break;
            case KFXmppManager.WAITING_TO_CONNECT:
            case KFXmppManager.WAITING_FOR_NETWORK:
            	KFLog.dd("waiting to connect");
            	//mTitle.setText("΢�ͷ�(�ͷ�Demo)(�ȴ���)");
                break;
            default:
                throw new IllegalStateException();
        }
    }
	
	

}

















