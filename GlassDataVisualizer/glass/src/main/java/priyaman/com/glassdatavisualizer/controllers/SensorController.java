package priyaman.com.glassdatavisualizer.controllers;

import android.content.Context;
import android.content.ContextWrapper;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import priyaman.com.glassdatavisualizer.beans.SensorValueStruct;
import priyaman.com.glassdatavisualizer.utilities.Log;
import priyaman.com.glassdatavisualizer.utilities.Utilities;

/**
 * Created by priya_000 on 5/21/2015.
 */
public class SensorController implements SensorEventListener {

    private Context context;
    private DrawViewController drawView;
    // Sensor manager
    private SensorManager mSensorManager = null;

    // Motion sensors
    private Sensor mSensorAccelerometer = null;
    private Sensor mSensorGravity = null;
    private Sensor mSensorLinearAcceleration = null;
    private Sensor mSensorGyroscope = null;
    private Sensor mSensorRotationVector = null;

    private long lastRefreshedTime = 0L;

    private void initializeSensorManager()
    {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorGravity = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        mSensorLinearAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mSensorGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorRotationVector = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }


    public SensorController(Context context, DrawViewController drawView){
            Log.d("onServiceStart() called.");
            this.context = context;
            this.drawView = drawView;
            initializeSensorManager();
            mSensorManager.registerListener(this, mSensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            mSensorManager.registerListener(this, mSensorGravity, SensorManager.SENSOR_DELAY_NORMAL);
            mSensorManager.registerListener(this, mSensorLinearAcceleration, SensorManager.SENSOR_DELAY_NORMAL);
            mSensorManager.registerListener(this, mSensorGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
            mSensorManager.registerListener(this, mSensorRotationVector, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("onSensorChanged() called.");

        processMotionSensorData(event);
        drawView.invalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("onAccuracyChanged() called.");
    }

    private void processMotionSensorData(SensorEvent event)
    {
        long now = System.currentTimeMillis();

        Sensor sensor = event.sensor;
        int type = sensor.getType();
        long timestamp = event.timestamp;
        float[] values = event.values;
        int accuracy = event.accuracy;
        SensorValueStruct data = new SensorValueStruct(type, timestamp, values, accuracy);

        switch(type) {
            case Sensor.TYPE_ACCELEROMETER:
                Utilities.lastSensorValuesAccelerometer = data;
                break;
            case Sensor.TYPE_GRAVITY:
                Utilities.lastSensorValuesGravity = data;
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                Utilities.lastSensorValuesLinearAcceleration = data;
                break;
            case Sensor.TYPE_GYROSCOPE:
                Utilities.lastSensorValuesGyroscope = data;
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                Utilities.lastSensorValuesRotationVector = data;
                break;
            default:
                Log.w("Unknown type: " + type);
        }



    }
}
