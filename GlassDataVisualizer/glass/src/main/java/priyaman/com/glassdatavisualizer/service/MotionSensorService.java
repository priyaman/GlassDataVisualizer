package priyaman.com.glassdatavisualizer.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.widget.RemoteViews;

import priyaman.com.glassdatavisualizer.GlassVisualizerActivity;
import priyaman.com.glassdatavisualizer.beans.SensorValueStruct;
import priyaman.com.glassdatavisualizer.utilities.Log;
import priyaman.com.glassdatavisualizer.R;
import priyaman.com.glassdatavisualizer.utilities.Utilities;

import com.google.android.glass.timeline.LiveCard;

/**
 * Created by priya_000 on 5/20/2015.
 */
public class MotionSensorService extends Service implements SensorEventListener {

    // For live card
    private LiveCard liveCard = null;

    // Sensor manager
    private SensorManager mSensorManager = null;

    // Motion sensors
    private Sensor mSensorAccelerometer = null;
    private Sensor mSensorGravity = null;
    private Sensor mSensorLinearAcceleration = null;
    private Sensor mSensorGyroscope = null;
    private Sensor mSensorRotationVector = null;




    // TBD:
    // Need a timer, etc. to refresh the UI (LiveCard) ever x seconds, etc..
    // For now, we just use the "last timestamp".
    private long lastRefreshedTime = 0L;



    // No need for IPC...
    public class LocalBinder extends Binder {
        public MotionSensorService getService() {
            return MotionSensorService.this;
        }
    }
    private final IBinder mBinder = new LocalBinder();


    @Override
    public void onCreate()
    {
        super.onCreate();
        initializeSensorManager();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.i("Received start id " + startId + ": " + intent);
        onServiceStart();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // ????
        onServiceStart();
        return mBinder;
    }

    @Override
    public void onDestroy()
    {
        // ???
        onServiceStop();
        super.onDestroy();
    }


    @Override
    public void onSensorChanged(SensorEvent event)
    {
        Log.d("onSensorChanged() called.");

        processMotionSensorData(event);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        Log.d("onAccuracyChanged() called.");

    }



    // Service state handlers.
    // ....

    private boolean onServiceStart()
    {
        Log.d("onServiceStart() called.");

        mSensorManager.registerListener(this, mSensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorGravity, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorLinearAcceleration, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorRotationVector, SensorManager.SENSOR_DELAY_NORMAL);

        // Publish live card...
        publishCard(this);

        return true;
    }

    private boolean onServicePause()
    {
        Log.d("onServicePause() called.");
        return true;
    }
    private boolean onServiceResume()
    {
        Log.d("onServiceResume() called.");
        return true;
    }

    private boolean onServiceStop()
    {
        Log.d("onServiceStop() called.");

        mSensorManager.unregisterListener(this);

        // TBD:
        // Unpublish livecard here
        // .....
        unpublishCard(this);
        // ...

        return true;
    }




    // For live cards...

    private void publishCard(Context context)
    {
        publishCard(context, false);
    }
    private void publishCard(Context context, boolean update)
    {
        if(Log.D) Log.d("publishCard() called: update = " + update);
        if (liveCard == null || update == true) {

//            // TBD:
//            // We get multiple liveCards if we just call setViews() and publish()...
//            // As a workaround, for now, we always unpublish the previous card first.
//            if (liveCard != null) {
//                liveCard.unpublish();
//            }
//            // ....

            final String cardId = "motionsensordemo_card";
            // Note: getLiveCard() always publishes a new live card....
            if(liveCard == null) {
                // if(liveCard == null || ! liveCard.isPublished()) {
                //TimelineManager tm = TimelineManager.from(context);
                liveCard = new LiveCard(context,cardId);//tm.createLiveCard(cardId);
//                 liveCard.setNonSilent(true);       // for testing.
            }
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.livecard_motionsensor);
            String content = "";
            if(Utilities.lastSensorValuesAccelerometer != null) {
                content += "Accelerometer:\n" + Utilities.lastSensorValuesAccelerometer.toString() + "\n";
            }
            if(Utilities.lastSensorValuesGravity != null) {
                content += "Gravity:\n" + Utilities.lastSensorValuesGravity.toString() + "\n";
            }
            if(Utilities.lastSensorValuesLinearAcceleration != null) {
                content += "Linear Acceleration:\n" + Utilities.lastSensorValuesLinearAcceleration.toString() + "\n";
            }
            if(Utilities.lastSensorValuesGyroscope != null) {
                content += "Gyroscope:\n" + Utilities.lastSensorValuesGyroscope.toString() + "\n";
            }
            if(Utilities.lastSensorValuesRotationVector != null) {
                content += "Rotation Vector:\n" + Utilities.lastSensorValuesRotationVector.toString() + "\n";
            }

            remoteViews.setCharSequence(R.id.livecard_content, "setText", content);
            liveCard.setViews(remoteViews);
            Intent intent = new Intent(context, GlassVisualizerActivity.class);
            liveCard.setAction(PendingIntent.getActivity(context, 0, intent, 0));
            // ???
            // Without this if(),
            // I get an exception:
            // "java.lang.IllegalStateException: State CREATED expected, currently in PUBLISHED"
            // Why???
            if(! liveCard.isPublished()) {
                liveCard.publish(LiveCard.PublishMode.REVEAL);
            } else {
                // ????
                // According to the GDK doc,
                // it appears we should call publish() every time the content changes...
                // But, it seems to work without re-publishing...
                if(Log.D) {
                    long now = System.currentTimeMillis();
                    Log.d("liveCard not published at " + now);
                }
            }
        } else {
            // Card is already published.
            return;
        }
    }

    private void unpublishCard(Context context)
    {
        Log.d("unpublishCard() called.");
        if (liveCard != null) {
            liveCard.unpublish();
            liveCard = null;
        }
    }


    // MotionSensor methods
    private void initializeSensorManager()
    {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorGravity = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        mSensorLinearAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mSensorGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorRotationVector = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    public void processMotionSensorData(SensorEvent event)
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

        // TBD:
        // Update the DB, etc..
        // ...

        // Update the UI.
        // temporary
        final long delta = 500L;   // every 5 seconds
        if(lastRefreshedTime <= now - delta) {
            // if(liveCard != null && liveCard.isPublished()) {
            // Update...
            publishCard(this, true);
            // }
            lastRefreshedTime = now;
        }
    }

}
