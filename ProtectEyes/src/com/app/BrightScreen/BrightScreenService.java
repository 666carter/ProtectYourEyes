  package com.app.BrightScreen; 
   
   import java.util.Calendar; 
import java.util.Date; 
   


import com.app.kate.MainActivity;
import com.app.kate.R;

   import android.app.Notification; 
import android.app.NotificationManager; 
import android.app.PendingIntent; 
import android.app.Service; 
import android.content.BroadcastReceiver; 
import android.content.Context; 
import android.content.Intent; 
import android.content.IntentFilter; 
import android.content.SharedPreferences; 
import android.os.IBinder; 
  
  /** 
   * create a service 
   * @author kate 
   */
  public class BrightScreenService extends Service 
  { 
      private static final int NOTIFY_ID=1234;//通知的唯一标识符 
      private Calendar cal=null; 
      
      //主要功能，广播接收器 
      private final BroadcastReceiver receiver=new BroadcastReceiver() 
      { 
          @Override
          public void onReceive(Context context, Intent intent) 
         { 
              SharedPreferences sp=getSharedPreferences("actm", Context.MODE_PRIVATE); 
              SharedPreferences.Editor editor=sp.edit(); 
             
              if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)) 
              { 
                  //保存屏幕启动时的毫秒数                 
                  editor.putLong("lasttime", new Date().getTime()); 
                  editor.commit(); 
            
              } 
              else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) 
              { 
                 //保存屏幕总工作时间 
                  long lasttime=sp.getLong("lasttime", new Date().getTime()); 
                  long sum=sp.getLong("sum", 0L); 
                  sum+=new Date().getTime()-lasttime; 
                  editor.putLong("sum", sum); 
                  editor.commit(); 
                  sum = 0;
             } 
              
          } 
      
     }; 
  
      @Override
      public void onCreate() 
      { 
          //添加过滤器并注册 
          final IntentFilter filter=new IntentFilter(); 
          filter.addAction(Intent.ACTION_SCREEN_ON); 
          filter.addAction(Intent.ACTION_SCREEN_OFF); 
          registerReceiver(receiver, filter); 
          
          super.onCreate(); 
      } 
      
      @Override
      public IBinder onBind(Intent arg0) 
      { 
          return null; 
      } 
 
 }