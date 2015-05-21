package priyaman.com.glassdatavisualizer.controllers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import priyaman.com.glassdatavisualizer.utilities.Log;
import priyaman.com.glassdatavisualizer.utilities.Utilities;

/**
 * Created by priya_000 on 5/20/2015.
 */
public class DrawViewController extends SurfaceView implements SurfaceHolder.Callback{

    private Paint textPaint = new Paint();
    private SurfaceHolder surfaceHolder = null;
    private Context context = null;

    public DrawViewController(Context context) {
        super(context);
        this.context = context;

        textPaint = new Paint();
        textPaint.setARGB(255, 200, 0, 0);
        textPaint.setTextSize(20);
        setWillNotDraw(false);
        surfaceHolder = getHolder();
    }

    @Override
    protected void onDraw(Canvas canvas){
        String content = "";
        int counter = 50;
        if(Utilities.lastSensorValuesAccelerometer != null) {
            content += "Accelerometer:\n" + Utilities.lastSensorValuesAccelerometer.toString() + "\n";
            canvas.drawText("Accelerometer:\n" + Utilities.lastSensorValuesAccelerometer.toString(), 50, counter+=25, textPaint);
        }
        if(Utilities.lastSensorValuesGravity != null) {
            content += "Gravity:\n" + Utilities.lastSensorValuesGravity.toString() + "\n";
            canvas.drawText("Gravity:\n" + Utilities.lastSensorValuesGravity.toString() , 50, counter+=25, textPaint);
        }
        if(Utilities.lastSensorValuesLinearAcceleration != null) {
            content += "Linear Acceleration:\n" + Utilities.lastSensorValuesLinearAcceleration.toString() + "\n";
            canvas.drawText( "Linear Acceleration:\n" + Utilities.lastSensorValuesLinearAcceleration.toString() , 50, counter+=25, textPaint);
        }
        if(Utilities.lastSensorValuesGyroscope != null) {
            content += "Gyroscope:\n" + Utilities.lastSensorValuesGyroscope.toString() + "\n";
            canvas.drawText( "Gyroscope:\n" + Utilities.lastSensorValuesGyroscope.toString() , 50, counter+=25, textPaint);
        }
        if(Utilities.lastSensorValuesRotationVector != null) {
            content += "Rotation Vector:\n" + Utilities.lastSensorValuesRotationVector.toString() + "\n";
            canvas.drawText( "Rotation Vector:\n" + Utilities.lastSensorValuesRotationVector.toString() , 50, counter+=25, textPaint);
        }
        Log.d("Values Published:" + content);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Canvas c = holder.lockCanvas();
        if(c!=null)
            draw(c);
        if(c!=null)
            holder.unlockCanvasAndPost(c);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Canvas c = holder.lockCanvas();
        if(c!=null)
            draw(c);
        if(c!=null)
            holder.unlockCanvasAndPost(c);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
