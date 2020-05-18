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
	// 默认启用锁屏
	private static boolean ACTIVITED = true;
	// 锁频距离（单位：厘米）
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
			sm = (SensorManager) getSystemService(SENSOR_SERVICE); // 获取传感器管理类
			promixty = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY); // 获取距离传感器
		}
		// 显示距离传感器信息
		if (null != promixty) {
			Toast.makeText(this, "已创建后台服务。", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "无法找到距离传感器", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if( null != sm ){
			//撤销监听器
			sm.unregisterListener(this);
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			Toast.makeText(this, "后台服务已启动。", Toast.LENGTH_SHORT).show();
			// 从Intent中获取设置参数
			if (bundle != null) {
				int dist = bundle.getInt("distance");
				ACTIVITED = bundle.getBoolean("activited");
				if (dist > 0 && dist < 9) {
					LOCK_DIST = dist;
				}
			}
			// 注册监听器
			sm.registerListener(this, promixty,SensorManager.SENSOR_DELAY_NORMAL);
		}
	}

	//监听精度变化
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		Toast.makeText(this, "距离传感器promixty精度变为" + accuracy,
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.values[0] < LOCK_DIST) // 距离小于5，锁屏
		{
			if (ACTIVITED) {
				lockScreen();
			}
		}
	}

	// 跳至锁屏页面
	private void lockScreen() {
		Intent intent = new Intent();
		//在Activity之外启动，要加上FLAG_ACTIVITY_NEW_TASK flag
		intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK ); 
		intent.setClass(this, LockScreen.Controller.class);
		startActivity(intent);
	}
}
