package com.app.autolock;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

/** 
 *  
 * @author kate 
 */
public class AutoLockService extends Service implements SensorEventListener {

	private SensorManager sm = null;
	private Sensor promixty = null;
	// Ĭ����������
	private static boolean ACTIVITED = true;
	// ��Ƶ���루��λ�����ף�
	private static int LOCK_DIST = 100;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		if (null == sm) {
			sm = (SensorManager) getSystemService(SENSOR_SERVICE); // ��ȡ������������
			promixty = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY); // ��ȡ���봫����
		}
		// ��ʾ���봫������Ϣ
		if (null != promixty) {
			Toast.makeText(this, "�Ѵ�����̨����", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "�޷��ҵ����봫����", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if( null != sm ){
			//����������
			sm.unregisterListener(this);
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			Toast.makeText(this, "��̨������������", Toast.LENGTH_SHORT).show();
			// ��Intent�л�ȡ���ò���
			if (bundle != null) {
				int dist = bundle.getInt("distance");
				ACTIVITED = bundle.getBoolean("activited");
				if (dist > 0 && dist < 9) {
					LOCK_DIST = dist;
				}
			}
			// ע�������
			sm.registerListener(this, promixty,SensorManager.SENSOR_DELAY_NORMAL);
		}
	}

	//�������ȱ仯
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		Toast.makeText(this, "���봫����promixty���ȱ�Ϊ" + accuracy,
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.values[0] < LOCK_DIST) // ����С��5������
		{
			if (ACTIVITED) {
				lockScreen();
			}
		}
	}

	// ��������ҳ��
	private void lockScreen() {
		Intent intent = new Intent();
		//��Activity֮��������Ҫ����FLAG_ACTIVITY_NEW_TASK flag
		intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK ); 
		intent.setClass(this, LockScreen.Controller.class);
		startActivity(intent);
	}
}
