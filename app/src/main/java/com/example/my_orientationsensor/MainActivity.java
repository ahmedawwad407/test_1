package com.example.my_orientationsensor;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager sensorManager;
    Sensor accelerometerSensor;
    Sensor magneticSensor;
    TextView textView;
    ImageView imageView;

    
    //bransh Ahmed
    private float[] rotationMatrix = new float[9];
    private float[] arrAccelerometerSensor = new float[3];
    private float[] arrMagneticSensor = new float[3];
    private float[] valuesReturn = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        textView = findViewById(R.id.valueS);
        imageView = findViewById(R.id.immageCompass);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null &&
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {
            magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            Toast.makeText(getApplicationContext(), "Sensor is find", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Sensor is not find", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//               arrAccelerometerSensor[0]= event.values[0];
//               arrAccelerometerSensor[1]= event.values[1];
//               arrAccelerometerSensor[2]= event.values[2];
            arrAccelerometerSensor = event.values;

        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            arrMagneticSensor = event.values;
        }

        SensorManager.getRotationMatrix(rotationMatrix, null, arrAccelerometerSensor, arrMagneticSensor);

        //valuesReturn 0....3
        SensorManager.getOrientation(rotationMatrix, valuesReturn);

        // CAST 180, -180
        int final_Val = (int) Math.toDegrees(valuesReturn[0]);

        //final_Val 180, -180 CAST 0.....360
        if (final_Val < 0) {
            textView.setText(" Val is: " + (final_Val + 360));
            imageView.setRotation((final_Val) * -1);

            if (final_Val == 0){

            }
        } else {
            textView.setText(" Val is: " + (final_Val));
            imageView.setRotation((final_Val) * -1);

            //0 -> North
            //90 -> east
            //180 -> south
            //270 -> west

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, magneticSensor, sensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, accelerometerSensor, sensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}