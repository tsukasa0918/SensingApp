package com.example.test02;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity implements SensorEventListener,LocationListener,OnClickListener{

	final static String TAG = "MyService";
	
	private SensorManager smanager;
	private LocationManager lmanager;
	//表示
	private TextView text1;
	private TextView text2;
	private TextView text3;
	private TextView text4;
	private TextView text5;
	private TextView text6;
	private TextView text7;
	private TextView text8;
	private TextView text9;
	private TextView text10;
	private TextView text11;
	private TextView text12;
	private TextView text13;
	private TextView text14;
	private TextView text15;
	
	//保存用と表示 別変数化計画凍結中
	/*
	public double data1;
	public double data2;
	public double data3;
	public double data4;
	public double data5;
	public double data6;
	public double data7;
	public double data8;
	public double data9;
	public double data10;
	public double data11;
	public double data12;
	public double data13;
	public double data14;	
	*/
	
	private long repeatInterval = 1;  // 繰り返しの間隔（単位：msec）0はできない．
	private long delayPoint = 0;  // この時間を基準とする（単位：msec）
	private Date date;
	private String SDFile;
	private File file;
	Timer timer;
	long startTime;
	private boolean showflag = true;
	//private boolean SDflag = false;
	
	private Handler handler;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//SDカードのチェック機能 API9以降
		/*
		if (Environment.isExternalStorageRemovable() == true){
			//内部メモリが取り外し可能
			SDflag = true;
		}else{
			//内部メモリが取り外し不能
			SDflag = false;
		}
		*/

		//センサ，GPS各サービスマネージャの取得
		smanager = (SensorManager)this.getSystemService(SENSOR_SERVICE);
		//lmanager = (LocationManager)this.getSystemService(LOCATION_SERVICE);
		
		//センサの情報をリストに読み込み
		List<Sensor> sensors = smanager.getSensorList(Sensor.TYPE_ALL);
		
		//リストの中身をレジスタに取り出し
		if(sensors.size() > 0){
			for(Sensor sensor :sensors){
			smanager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
		}

		text1 = (TextView) this.findViewById(R.id.TextView1);
		text2 = (TextView) this.findViewById(R.id.TextView2);
		text3 = (TextView) this.findViewById(R.id.TextView3);
		text4 = (TextView) this.findViewById(R.id.TextView4);
		text5 = (TextView) this.findViewById(R.id.TextView5);
		text6 = (TextView) this.findViewById(R.id.TextView6);
		text7 = (TextView) this.findViewById(R.id.TextView7);
		text8 = (TextView) this.findViewById(R.id.TextView8);
		text9 = (TextView) this.findViewById(R.id.TextView9);
		text10 = (TextView) this.findViewById(R.id.TextView10);
		text11 = (TextView) this.findViewById(R.id.TextView11);
		text12 = (TextView) this.findViewById(R.id.TextView12);
		text13 = (TextView) this.findViewById(R.id.TextView13);
		text14 = (TextView) this.findViewById(R.id.TextView14);
		text15 = (TextView) this.findViewById(R.id.textView15);
		

		View startButton = findViewById(R.id.button1);
		View stopButton = findViewById(R.id.Button02);
		View hiddenButton = findViewById(R.id.Button01);
		
		startButton.setOnClickListener(this);
		stopButton.setOnClickListener(this);
		hiddenButton.setOnClickListener(this);
		}
	}
	

	 private final Runnable runnable = new Runnable() {
	  @Override
	  public void run() {

		 SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyy'/'MM'/'dd'_'HH':'mm':'ss",Locale.JAPAN);
			 
	   int ho = (int)((System.nanoTime() - startTime)/1000000000);
	   text15.setText("Start Time:" + fileNameDateFormat.format(date) + "\n Elapsed time: " + ho + " s ");
	   //1秒(1000ミリ秒)後に再帰呼び出し
	   handler.postDelayed(runnable, 1000);
	  }
	 };

	public void onClick(View view){
		   
		
		//各ボタンの処理
		switch(view.getId()){
		
		//Startボタン処理
		case R.id.button1:
			// タイマーをセット
	        timer = new Timer("testTimer");
	        
	        //startTime = System.currentTimeMillis();
			startTime = System.nanoTime();
			long cTM  = System.currentTimeMillis();
			handler = new Handler();
			handler.post(runnable);
			
			
			//時間をセット
			date = new Date(cTM);
			//読み書き用ファイル名をセット
			SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyy'_'MM'_'dd'_'HH'-'mm'-'ss",Locale.JAPAN);
			SDFile = android.os.Environment.getExternalStorageDirectory().getPath()
			    + "/LogData/"+ fileNameDateFormat.format(date) + ".txt";
			
			file = new File(SDFile);
			file.getParentFile().mkdir();
			 Log.d(TAG, "Hello!\n" + SDFile);
			        
			try{
			    FileOutputStream fos = new FileOutputStream(file,true);
			    OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");
			    PrintWriter pw = new PrintWriter(osw);
			    //見出し
			    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss.SSS",Locale.JAPAN);
			    String titleStr = simpleDateFormat.format(date) + "\tAccX\tAccY\tAccZ\tLAccX\tLAccY\tLAccZ\tGyroX\tGyroY\tGyroZ\tMangX\tMagY\tMagZ\tLatitude\tLongitude\n";
			    pw.append(titleStr); 
			    pw.close();
			    osw.close();
			    fos.close();
			  }
			  catch(FileNotFoundException e){
			    e.printStackTrace();
			  }
			  catch(UnsupportedEncodingException e){
			    e.printStackTrace();
			  }
			  catch(IOException e){
			    e.printStackTrace();
			  }
			Log.d(TAG, "Hello!_START BUTTON");
			
			int flag = 0;
			
			if(flag == 1){
			//書き込み祭り
			while(true){
				try{
				    FileOutputStream fos = new FileOutputStream(file,true);
				    OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");
				    PrintWriter pw = new PrintWriter(osw);
				    
				    double myTime = System.nanoTime() - startTime;
				    myTime *= 0.000000001;
				    Log.d(TAG, "Hello!_WRITING NOW");
				    String str = Double.toString(myTime)
				    		+ "\t" + text1.getText().toString()
				    		+ "\t" + text2.getText().toString()
				    		+ "\t" + text3.getText().toString()
				    		+ "\t" + text12.getText().toString()
				    		+ "\t" + text13.getText().toString()
				    		+ "\t" + text14.getText().toString()
				    		+ "\t" + text4.getText().toString()
				    		+ "\t" + text5.getText().toString()
				    		+ "\t" + text6.getText().toString()
				    		+ "\t" + text7.getText().toString()
				    		+ "\t" + text8.getText().toString()
				    		+ "\t" + text9.getText().toString()
				    		+ "\t" + text10.getText().toString()
				    		+ "\t" + text11.getText().toString()
				    		+ "\n";
				    pw.append(str);  
				    pw.close();
				    osw.close();
				    fos.close();
				    
				  }
				  catch(FileNotFoundException e){
				    e.printStackTrace();
				  }
				  catch(UnsupportedEncodingException e){
				    e.printStackTrace();
				  }
				  catch(IOException e){
				    e.printStackTrace();
				  }
				}
			}else{
			//repeatIntervalでの定期呼び出し開始
			timer.scheduleAtFixedRate(new TimerTask(){
				@Override
				public void run(){
				try{
				    FileOutputStream fos = new FileOutputStream(file,true);
				    OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");
				    PrintWriter pw = new PrintWriter(osw);
				    
				    double myTime = System.nanoTime() - startTime;
				    myTime *= 0.000000001;
				    Log.d(TAG, "Hello!_WRITING NOW");
				    String str = Double.toString(myTime)
				    		+ "\t" + text1.getText().toString()
				    		+ "\t" + text2.getText().toString()
				    		+ "\t" + text3.getText().toString()
				    		+ "\t" + text12.getText().toString()
				    		+ "\t" + text13.getText().toString()
				    		+ "\t" + text14.getText().toString()
				    		+ "\t" + text4.getText().toString()
				    		+ "\t" + text5.getText().toString()
				    		+ "\t" + text6.getText().toString()
				    		+ "\t" + text7.getText().toString()
				    		+ "\t" + text8.getText().toString()
				    		+ "\t" + text9.getText().toString()
				    		+ "\t" + text10.getText().toString()
				    		+ "\t" + text11.getText().toString()
				    		+ "\n";
				    pw.append(str);  
				    pw.close();
				    osw.close();
				    fos.close();
				    
				    /*凍結中
				    if(showflag == true){
				    	Log.d(TAG, "Hello!_SHOWING NOW");
				    	text1.setText(Double.toString(data1));
				    	text1.setText(String.format("%d",data1));
						text2.setText(String.format("%d",data2));
						text3.setText(String.format("%d",data3));
						text4.setText(String.format("%d",data4));
						text5.setText(String.format("%d",data5));
						text6.setText(String.format("%d",data6));
						text7.setText(String.format("%d",data7));
						text8.setText(String.format("%d",data8));
						text9.setText(String.format("%d",data9));
						text10.setText(String.format("%d",data10));
						text11.setText(String.format("%d",data11));
						text12.setText(String.format("%d",data12));
						text13.setText(String.format("%d",data13));
						text14.setText(String.format("%d",data14));
						
				    }
				*/
				    
				  }
				  catch(FileNotFoundException e){
				    e.printStackTrace();
				  }
				  catch(UnsupportedEncodingException e){
				    e.printStackTrace();
				  }
				  catch(IOException e){
				    e.printStackTrace();
				  }
				}
			}, delayPoint, repeatInterval);
			break;
			}
		//Stopボタン処理
		case R.id.Button02:
			Log.d(TAG, "Hello!_STOP BUTTON");
			//STOP連打するとおかしな表示になるよ
			int ho = (int)((System.nanoTime() - startTime)/1000000000);
			text15.setText("Logged Time: " + ho + " s ");
			handler.removeCallbacks(runnable);
			cancelTimer();
			break;
		
		//hiddenボタン処理
		case R.id.Button01:
			Log.d(TAG, "Hello!_HIDDEN BUTTON ");
			showflag = !showflag;
			

			Button btn1 = (Button) this.findViewById(R.id.button2);  
	        if (btn1.getVisibility() != View.VISIBLE) {  
	            btn1.setVisibility(View.VISIBLE);  
	        } else {  
	            btn1.setVisibility(View.INVISIBLE);  
	        }   
			
			break;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor,int accuracy ){
		//精度が変更されたときの処理
	}

	@Override
	public void onSensorChanged(SensorEvent event){
		//センサーの値が変更されたときの処理

		//各センサの値を取得
		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
		
			text1.setText(String.format("%f",event.values[0]));
			text2.setText(String.format("%f",event.values[1]));
			text3.setText(String.format("%f",event.values[2]));
			//data1 = event.values[0];
			//data2 = event.values[1];
			//data3 = event.values[2];
			
		}else if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
			
			text4.setText(String.format("%f",event.values[0]));
			text5.setText(String.format("%f",event.values[1]));
			text6.setText(String.format("%f",event.values[2]));
			
			//data4 = event.values[0];
			//data5 = event.values[1];
			//data6 = event.values[2];
		
		}else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
			
			text7.setText(String.format("%f",event.values[0]));
			text8.setText(String.format("%f",event.values[1]));
			text9.setText(String.format("%f",event.values[2]));
			
			//data7 = event.values[0];
			//data8 = event.values[1];
			//data9 = event.values[2];
		
		}else if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
			
			text12.setText(String.format("%f",event.values[0]));
			text13.setText(String.format("%f",event.values[1]));
			text14.setText(String.format("%f",event.values[2]));
			
			//data12 = event.values[0];
			//data13 = event.values[1];
			//data14 = event.values[2];
				
		
		}	
	}	
	@Override
	public void onLocationChanged(Location location){
		//GPSの値が取得された時の処理
		double lat = location.getLatitude();
		double lon = location.getLongitude();
		
		//data10 = lat;
		//data11 = lon;
		text10.setText(Double.toString(lat));
		text11.setText(Double.toString(lon));	
	}
	
	@Override
	public void onProviderDisabled(String provider){}
	
	@Override
	public void onProviderEnabled(String provider){}
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras){}
	

    @Override
    protected void onResume() {
    	lmanager = (LocationManager) getSystemService(LOCATION_SERVICE);
        
    	
    	if(!lmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
    		//GPSの有効化を尋ねる．
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setTitle("Location Manager");
    		builder.setMessage("Please on GPS.\n  Can you change these settings now?");
    		
    		builder.setPositiveButton("YES",new DialogInterface.OnClickListener(){
    			@Override
    			public void onClick(DialogInterface dialog, int which){
    				Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    				startActivity(i);
    			}
    		});
    		builder.setNegativeButton("NO",new DialogInterface.OnClickListener(){
				@Override
    			public void onClick(DialogInterface dialog, int which){
    				finish();
    			}
			});
    	}
    	
        //List<String> providers = lmanager.getProviders(true);
        //for (String provider : providers) {
    	String provider = LocationManager.GPS_PROVIDER;  
    	lmanager.requestLocationUpdates(provider, 0, 0, this);
        //}
        super.onResume();
    }

    private void cancelTimer() {
    	if (timer != null) {
    		timer.cancel();
        }
     }
   /* 凍結中
    public void showData(){
    	
    	text1.setText(String.format("%f",data1));
		text2.setText(String.format("%f",data2));
		text3.setText(String.format("%f",data3));
		text4.setText(String.format("%f",data4));
		text5.setText(String.format("%f",data5));
		text6.setText(String.format("%f",data6));
		text7.setText(String.format("%f",data7));
		text8.setText(String.format("%f",data8));
		text9.setText(String.format("%f",data9));
		text10.setText(String.format("%f",data10));
		text11.setText(String.format("%f",data11));
		text12.setText(String.format("%f",data12));
		text13.setText(String.format("%f",data13));
		text14.setText(String.format("%f",data14));
    }
    */
    
    @Override
    protected void onPause() {
        if (lmanager != null) {
            lmanager.removeUpdates(this);
        }
        super.onPause();
    }
    
    @Override
    protected void onDestroy() {
      super.onDestroy();
      // 重要：requestLocationUpdatesしたままアプリを終了すると挙動がおかしくなる。
      lmanager.removeUpdates(this);
      cancelTimer();
    }
}

