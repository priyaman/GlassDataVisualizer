package priyaman.com.glassdatavisualizer.controllers;

import android.content.Context;
import android.content.ContextWrapper;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import priyaman.com.glassdatavisualizer.beans.SensorValueStruct;
import priyaman.com.glassdatavisualizer.utilities.CameraUtils;
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
    String mediaStorageDir = null;

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

            mediaStorageDir = CameraUtils.getOutputMediaFile(3).toString();
    }
    @Override
    public void onSensorChanged(SensorEvent event) {

        Log.v("onSensorChanged() called.");

        processMotionSensorData(event);
        if(Utilities.logSensorValues){
            StringBuffer strBuf = new StringBuffer();
            if(Utilities.lastSensorValuesAccelerometer != null) {
                strBuf.append("Accelerometer:\n" + Utilities.lastSensorValuesAccelerometer.toString() + "\n");
            }
            if(Utilities.lastSensorValuesGravity != null) {
                strBuf.append("Gravity:\n" + Utilities.lastSensorValuesGravity.toString() + "\n");
            }
            if(Utilities.lastSensorValuesLinearAcceleration != null) {
                strBuf.append("Linear Acceleration:\n" + Utilities.lastSensorValuesLinearAcceleration.toString() + "\n");
            }
            if(Utilities.lastSensorValuesGyroscope != null) {
                strBuf.append("Gyroscope:\n" + Utilities.lastSensorValuesGyroscope.toString() + "\n");
            }
            if(Utilities.lastSensorValuesRotationVector != null) {
                strBuf.append("Rotation Vector:\n" + Utilities.lastSensorValuesRotationVector.toString() + "\n");
            }
            if(Utilities.lastSensorValuesRotationVector != null) {
                strBuf.append("Timestamp:\n" + Utilities.lastSensorValuesRotationVector.getTimestamp() + "\n");
            }

            writeToFile(strBuf.toString());
        }
        if(!Utilities.isStopping)
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

    private void writeToFile(String data) {
        try {
            //OutputStreamWriter outputStreamWriter = new FileOutputStream(new File(mediaStorageDir),true);//new OutputStreamWriter(context.openFileOutput(mediaStorageDir, Context.MODE_PRIVATE));
            FileOutputStream outputStreamWriter = new FileOutputStream(new File(mediaStorageDir),true);//new OutputStreamWriter(context.openFileOutput(mediaStorageDir, Context.MODE_PRIVATE));
            outputStreamWriter.write(data.getBytes());
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception" + "File write failed: " + e.toString());
        }
    }

}
