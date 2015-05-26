package priyaman.com.glassdatavisualizer.controllers;

import android.content.Context;

/**
 * Created by priya_000 on 5/22/2015.
 */
public class SensorThread extends Thread {
    SensorController sensorController;
    Context context = null;
    DrawViewController drawView = null;
    public SensorThread(Context context, DrawViewController drawView){
        this.context = context;
        this.drawView = drawView;
    }
    public void run(){
        sensorController = new SensorController(context, drawView);

    }
}
