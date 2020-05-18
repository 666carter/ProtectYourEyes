package com.app.kate; 
 import java.text.SimpleDateFormat; 
import java.util.Date; 

import com.app.autolock.LockScreen;
import com.app.step.StepDetector;
import com.app.step.StepService;

import android.app.Activity; 
import android.app.Service;
import android.content.Context; 
import android.content.Intent; 
import android.content.SharedPreferences; 
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle; 
import android.os.IBinder;
import android.util.Log;
import android.view.View; 
import android.view.View.OnClickListener; 
import android.widget.Button; 
import android.widget.EditText; 
import android.widget.TextView; 
import android.widget.Toast; 
 
 /** 
  * Ŀǰ��ʵ����С���ܣ�ֻ��ȡ���ܵ���Ļ����ʱ�� 
  * ͨ���㲥��������Ļ�Ƿ���������¼� 
  * @author kate 
  */
 public class MainActivity extends Activity  
 { 
     private Button start_service;
  	private Button stop_service;
  	
 	private Intent intent;
 	private SensorManager sm = null;
 	private Sensor promixty = null;
 	private Intent step_intent;
 	private Intent bS_intent;
     @Override
     public void onCreate(Bundle savedInstanceState)  
     { 
         super.onCreate(savedInstanceState); 
         setContentView(R.layout.activity_main);                    
         //startBrightScreenService();

         initUI();
           
        step_intent = new Intent(this, StepService.class);
        bS_intent = new Intent("com.app.BrightScreen.SERVICE_DEMO");
   		intent = new Intent("com.app.autoLockService");
   		start_service.setOnClickListener( new OnClickListener() {
   			@Override
   			public void onClick(View v) {
   				//���������ط���
   				startStepService();
   				startAutoLockService();
   				startBrightScreenService();	
   				Log.v("hello", "startservice");
   			}
   		});
   		stop_service.setOnClickListener(new OnClickListener() {
   			@Override
   			public void onClick(View v) {
   				// ֹͣ�����ط���
   				stopStepService();
   				stopAutoLockService();
   			}
   		});
   	}
     
   	private void initUI(){
   		 
        start_service = (Button)super.findViewById(R.id.start_service);
        stop_service = (Button)super.findViewById(R.id.stop_service);
   	}
   	
   	private void startAutoLockService(){
   		Bundle bundle = new Bundle();
   		bundle.putInt("distance", 3);
   		bundle.putBoolean("activited", true);
   		intent.putExtras(bundle);
   		startService(intent);							// startService
   	}
   	//��ֹ����
   	private void stopAutoLockService(){
   		stopService(intent);
   	}
   	private void startStepService(){
   		startService(step_intent);
   	}
   	
   	//��ֹ����
   	private void stopStepService(){
   		stopService(step_intent);
   	}
     private void startBrightScreenService()
     {
    	 Log.v("hello", "startservice0");
    	 startService(bS_intent);//��������
     }
 }
 