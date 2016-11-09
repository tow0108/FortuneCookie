package com.egco428.a13283.mobileassignment1;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Captain-Tow on 11/6/2016.
 */

public class ResultActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private long lastUpdate;
    public int randomInt;
    public static final String value = "value";
    public Button shakeButton;
    public double d = 0;
    public int check = 1;
    public boolean flag = true;
    public String date,quote,position;
    public String word[] = {"Something surprise you today","You will get A","You 're Lucky","Don't Panic","Something surprise you today","Work Harder"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();
        shakeButton = (Button) findViewById(R.id.shakeButton);
        shakeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (check == 1 ){flag = false;}
                else if (check == 0){
                    BackMethod();
                }
            }
        });

    }
    public void BackMethod() {
        if (date == null || quote == null || position == null){
            return;
        }

        Intent intent = new Intent();
        String inputValue[] = {quote,date,position};
        intent.putExtra(value,inputValue);
        setResult(ResultActivity.RESULT_OK, intent);
        finish();
        return;


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && !flag){
            getAccelerometer(event);
        }

    }
    private void getAccelerometer(SensorEvent event){
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = System.currentTimeMillis();



        if (accelationSquareRoot >= 2.5 )
        {
            if (actualTime - lastUpdate < 1000) {
                return;
            }
            shakeButton.setText("Shaking");

            double randNumber = Math.random();
            d = randNumber * 6;
            randomInt = (int)d;
            lastUpdate = actualTime;

        }
        else if(accelationSquareRoot < 1.5 && randomInt> 0 && randomInt <= 6){
            try {
                Thread.sleep(1000);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            shakeButton.setText("Save");
            DateFormat df = new SimpleDateFormat("dd-MMM-yy HH:mm");
            Date dateobj = new Date();


            TextView viewdate = (TextView) findViewById(R.id.textDate);
            viewdate.setText("Date: "+df.format(dateobj));
            ImageView image = (ImageView)findViewById(R.id.imageView);
            int res = this.getResources().getIdentifier("image"+randomInt,"drawable",this.getPackageName());//ดึงไฟล์ที่อยู่ใต้โฟลเดอร์ res ผ่าน ID ในประเภท drawable
            image.setImageResource(res);
            TextView result = (TextView) findViewById(R.id.textResult);
            result.setText("Result : "+word[randomInt-1]);
            flag = true;
            check = 0;
            //set value for database
            date = df.format(dateobj);
            quote = word[randomInt-1];
            position = String.valueOf(randomInt);
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }


}
