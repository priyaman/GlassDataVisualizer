package priyaman.com.glassdatavisualizer.utilities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import priyaman.com.glassdatavisualizer.GlassVisualizerActivity;
import priyaman.com.glassdatavisualizer.Launcher;
import priyaman.com.glassdatavisualizer.beans.SensorValueStruct;

/**
 * Created by priya_000 on 5/19/2015.
 */
public class Utilities {

    // Last known motionsensor values.
    public static SensorValueStruct lastSensorValuesAccelerometer = null;
    public static SensorValueStruct lastSensorValuesGravity = null;
    public static SensorValueStruct lastSensorValuesLinearAcceleration = null;
    public static SensorValueStruct lastSensorValuesGyroscope = null;
    public static SensorValueStruct lastSensorValuesRotationVector = null;
    public static boolean isDirty = true;
    public static boolean logSensorValues = false;
    public static String sensorFileName = null;
    public static boolean isStopping = false;
}
