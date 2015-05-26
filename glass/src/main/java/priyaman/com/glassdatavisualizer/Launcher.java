package priyaman.com.glassdatavisualizer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import priyaman.com.glassdatavisualizer.utilities.Utilities;


public class Launcher extends Activity {


    private GestureDetector mGestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        mGestureDetector = this.createGestureDetector(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_launcher, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.v("Testing", "On options menu selected");
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event)
    {
        if (mGestureDetector != null) {
            return mGestureDetector.onMotionEvent(event);
        }
        return false;
    }

    public  GestureDetector createGestureDetector(Context context)
    {
        GestureDetector gestureDetector = new GestureDetector(context);
        //Create a base listener for generic gestures
        gestureDetector.setBaseListener( new GestureDetector.BaseListener() {
            @Override
            public boolean onGesture(Gesture gesture) {
                if(priyaman.com.glassdatavisualizer.utilities.Log.D) priyaman.com.glassdatavisualizer.utilities.Log.d("gesture = " + gesture);
                if (gesture == Gesture.TAP) {
                    handleGestureTap();
                    return true;
                } else if (gesture == Gesture.TWO_TAP) {
                    handleGestureTwoTap();
                    return true;
                }
                return false;
            }
        });
        return gestureDetector;
    }

    private  void handleGestureTap()
    {
        priyaman.com.glassdatavisualizer.utilities.Log.d("handleGestureTap() called.");
        doStartService();

    }

    private  void doStartService()
    {
        Intent resultsIntent = new Intent(this, GlassVisualizerActivity.class);
        Log.v("Start ACt", "Staring glass visualizer");
        startActivity(resultsIntent);
    }

    private  void handleGestureTwoTap()
    {
        priyaman.com.glassdatavisualizer.utilities.Log.d("handleGestureTwoTap() called.");
    }

}
