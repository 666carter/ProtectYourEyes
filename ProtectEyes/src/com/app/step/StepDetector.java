package com.app.step;

import java.util.Date;

import com.app.autolock.LockScreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class StepDetector implements SensorEventListener {
 
    public static int CURRENT_SETP = 0;
    public static float SENSITIVITY = 7; // SENSITIVITY������
    private float mLastValues[] = new float[3 * 2];
    private float mScale[] = new float[2];
    private float mYOffset;
    private static long end = 0;
    private static long start = 0;
    private Context context;
    /**
     * �����ٶȷ���
     */
    private float mLastDirections[] = new float[3 * 2];
    private float mLastExtremes[][] = { new float[3 * 2], new float[3 * 2] };
    private float mLastDiff[] = new float[3 * 2];
    private int mLastMatch = -1;
 
    /**
     * ���������ĵĹ��캯��
     * 
     * @param context
     */
    public StepDetector(Context context) {
        super();
        this.context =context;
        int h = 480;
        mYOffset = h * 0.5f;
        mScale[0] = -(h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
        mScale[1] = -(h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
    }
 
    //����������⵽����ֵ�����仯ʱ�ͻ�����������
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        synchronized (this) {
            if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
 
                float vSum = 0;
                for (int i = 0; i < 3; i++) {
                    final float v = mYOffset + event.values[i] * mScale[1];
                    vSum += v;
                }
                int k = 0;
                float v = vSum / 3;
 
                float direction = (v > mLastValues[k] ? 1
                        : (v < mLastValues[k] ? -1 : 0));
                if (direction == -mLastDirections[k]) {
                    // Direction changed
                    int extType = (direction > 0 ? 0 : 1); // minumum or
                                                            // maximum?
                    mLastExtremes[extType][k] = mLastValues[k];
                    float diff = Math.abs(mLastExtremes[extType][k]
                            - mLastExtremes[1 - extType][k]);
 
                    if (diff > SENSITIVITY) {
                        boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k] * 2 / 3);
                        boolean isPreviousLargeEnough = mLastDiff[k] > (diff / 3);
                        boolean isNotContra = (mLastMatch != 1 - extType);
 
                        if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough
                                && isNotContra) {
                            end = System.currentTimeMillis();
                            if (end - start > 500) {// ��ʱ�ж�Ϊ����һ��
 
                                CURRENT_SETP++;
                                if(CURRENT_SETP > 10)
                				{
                                	SharedPreferences sp = context.getSharedPreferences("actm", Context.MODE_PRIVATE);        
          				         
                                	long sum=(int)sp.getLong("sum", 0L)/1000; 
                                  long lasttime=sp.getLong("lasttime", new Date().getTime());  
                                  sum+=new Date().getTime()-lasttime; 
        				          int second=(int)(sum%60); 
        				          if(second > 20)
        				          {
        				        	  Log.v("hello", "kaisi2");
        				        	  StepDetector.CURRENT_SETP = 0;
        				        	  SharedPreferences.Editor editor=sp.edit(); 
        				        	  editor.putLong("sum", 0); 
        			                  editor.commit(); 
        			                  LockScreen();
        				          }
                				}
                                mLastMatch = extType;
                                start = end;
                            }
                        } else {
                            mLastMatch = -1;
                        }
                    }
                    mLastDiff[k] = diff;
                }
                mLastDirections[k] = direction;
                mLastValues[k] = v;
            }
 
        }
    }
    public void  LockScreen() {
     	Intent intentClose = new Intent();
   		intentClose.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK ); 
   		intentClose.setClass(context, LockScreen.Controller.class);
   		context.startActivity(intentClose);
 	}
    //���������ľ��ȷ����仯ʱ�ͻ�������������������û����
    public void onAccuracyChanged(Sensor arg0, int arg1) {
 
    }
 
}