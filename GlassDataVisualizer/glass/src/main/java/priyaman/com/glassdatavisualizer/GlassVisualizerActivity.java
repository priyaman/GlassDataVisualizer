package priyaman.com.glassdatavisualizer;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import priyaman.com.glassdatavisualizer.controllers.CameraController;
import priyaman.com.glassdatavisualizer.controllers.DrawViewController;
import priyaman.com.glassdatavisualizer.controllers.SensorController;
import priyaman.com.glassdatavisualizer.service.MotionSensorService;
import priyaman.com.glassdatavisualizer.utilities.CameraUtils;

public class GlassVisualizerActivity extends Activity {




    private Camera camera;
    private CameraController cameraController;
    private GestureDetector mGestureDetector;
    private SensorController sensorController;
    private DrawViewController drawView;
    FrameLayout alParent;
    public  GestureDetector createGestureDetector(Context context)
    {
        GestureDetector gestureDetector = new GestureDetector(context);
        //Create a base listener for generic gestures
        gestureDetector.setBaseListener( new GestureDetector.BaseListener() {
            @Override
            public boolean onGesture(Gesture gesture) {
                if(priyaman.com.glassdatavisualizer.utilities.Log.D) priyaman.com.glassdatavisualizer.utilities.Log.d("gesture = " + gesture);
                if (gesture == Gesture.TAP) {
                    cameraController.startRecording();
                    return true;
                } else if (gesture == Gesture.TWO_TAP) {
                    //handleGestureTwoTap();
                    //doStopService();
                    return true;
                }
                return false;
            }
        });
        return gestureDetector;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        Log.d("Debug:", "onCreate: GlassVisualizerActivity");
        super.onCreate(bundle);

        camera = CameraUtils.getCameraInstance();
        cameraController = new CameraController(this, camera);
        //this.setContentView(cameraController);
        mGestureDetector = this.createGestureDetector(this);
        drawView = new DrawViewController(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (cameraController != null){
            alParent = new FrameLayout(this);
            alParent.setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.FILL_PARENT,
                    FrameLayout.LayoutParams.FILL_PARENT));

            //doStartService();
            alParent.addView(cameraController);
            alParent.addView(drawView);
            this.setContentView(alParent);
            sensorController = new SensorController(this,drawView);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cameraController != null) {
            cameraController.releaseCamera();
        }
        //doStartService();

        // Set the view
        //alParent.addView(drawView);
        //alParent.addView(cameraController);
        this.setContentView(alParent);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event)
    {
        if (mGestureDetector != null) {
            return mGestureDetector.onMotionEvent(event);
        }
        return false;
    }


    @Override
    protected void onPause() {
        if (cameraController != null) {
            cameraController.releaseMediaRecorder();       // if you are using MediaRecorder, release it first
            cameraController.releaseCamera();              // release the camera immediately on pause event
        }

        super.onPause();
    }





  /*  private boolean mIsBound = false;
    private MotionSensorService motionSensorDemoLocalService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d("DEBUG","On Service called");
            motionSensorDemoLocalService = ((MotionSensorService.LocalBinder)service).getService();
        }
        public void onServiceDisconnected(ComponentName className) {
            Log.d("DEBUG","On Service onServiceDisconnected() called");
            motionSensorDemoLocalService = null;
        }
    };
    private void doBindService()
    {
        bindService(new Intent(this, MotionSensorService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }
    private void doUnbindService() {
        if (mIsBound) {
            unbindService(serviceConnection);
            mIsBound = false;
        }
    }
    private void doStartService()
    {
        Log.d("DEBUG", "Starting motion sensor");
        startService(new Intent(this, MotionSensorService.class));
    }
    private void doStopService()
    {
        stopService(new Intent(this, MotionSensorService.class));
    }
*/

/*    @Override
    protected void onDestroy()
    {
        doUnbindService();
        // doStopService();   // TBD: When do we call Stop service???
        super.onDestroy();
    }*/

}
