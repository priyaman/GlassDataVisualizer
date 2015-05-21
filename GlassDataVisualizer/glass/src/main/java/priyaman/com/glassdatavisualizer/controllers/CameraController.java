package priyaman.com.glassdatavisualizer.controllers;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import priyaman.com.glassdatavisualizer.utilities.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import priyaman.com.glassdatavisualizer.R;
import priyaman.com.glassdatavisualizer.utilities.CameraUtils;

/**
 * Created by priya_000 on 5/19/2015.
 */
public class CameraController extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder finalView;
    private Camera camera;
    private MediaRecorder mediaRecorder;
    private boolean isRecording = false;
    public CameraController(Context context, Camera camera){
        super(context);
        Log.d("In Camera Controller Constructor");
        this.camera = camera;
        finalView = getHolder();
        finalView.addCallback(this);
        finalView.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.v("In create surface");
        try {
            if(camera==null){
                camera = CameraUtils.getCameraInstance();
                if(camera == null)
                    return;
                //setMeteringParams();
            }

            camera.setPreviewDisplay(holder);

        } catch (IOException e1) {
            Log.e("Error in surface creation",e1);
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        //Log that camera controller destroyed
        Log.d("In surface destroyed:Camera Controller Destroyed");
        if(camera!= null)
            camera.stopPreview();
        releaseCamera();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h){
        Log.d("In Surface Changed");
        if (holder.getSurface() == null){
            // preview surface does not exist
            return;
        }
        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e){
        }
        try {
            //setMeteringParams();
            camera.setPreviewDisplay(holder);
            camera.startPreview();

        } catch (Exception e){

        }
    }

    public boolean prepareVideoRecorder(){
        Log.d("Preparing Video Recorder");
        try {
            if (camera == null) {
                camera = CameraUtils.getCameraInstance();
            }
            camera.unlock();
            mediaRecorder.setCamera(camera);
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
            mediaRecorder.setOutputFile(CameraUtils.getOutputMediaFile(CameraUtils.MEDIA_TYPE_VIDEO).toString());
            mediaRecorder.setPreviewDisplay(finalView.getSurface());
            try {
                mediaRecorder.prepare();
            } catch (IllegalStateException e) {
                Log.d("IllegalStateException preparing MediaRecorder: " + e.getMessage());
                releaseMediaRecorder();
                return false;
            } catch (IOException e) {
                Log.d("IOException preparing MediaRecorder: " + e.getMessage());
                releaseMediaRecorder();
                return false;
            }
            return true;
        }catch(Exception e){
            Log.e("Error in prepare Video Recorder",e);
            e.printStackTrace();
        }finally{
            this.releaseCamera();
            Log.d("Camera released");
            return false;
        }
    }

    public void startRecording(){
                        Log.d("In Start Recording");
                        Log.d("Camera is :" + String.valueOf(camera==null));
                        if(isRecording){
                            mediaRecorder.stop();
                            releaseMediaRecorder();
                            camera.lock();
                            isRecording = false;
                        }else{
                            if(prepareVideoRecorder()){
                                mediaRecorder.start();
                            }
                }
    }



    public void releaseMediaRecorder(){
        if (mediaRecorder != null) {
            mediaRecorder.reset();   // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            camera.lock();           // lock camera for later use
        }
    }

    public void releaseCamera(){
        if (camera != null){
            camera.release();        // release the camera for other applications
            camera = null;
        }
    }

    public void setMeteringParams(){
        Camera.Parameters params = camera.getParameters();
        Log.d("In metering params" + params.getMaxNumMeteringAreas());
        if(params.getMaxNumMeteringAreas()>0){
            List<Camera.Area> meteringAres = new ArrayList<Camera.Area>();
            Rect areaRect1 = new Rect(-10,-10,10,10);
            meteringAres.add(new Camera.Area(areaRect1,600));
            Rect areaRect2 = new Rect(800, -1000, 1000, -800);  // specify an area in upper right of image
            meteringAres.add(new Camera.Area(areaRect2, 400));
            params.setMeteringAreas(meteringAres);
        }
        camera.setParameters(params);
    }

    public void setAccelReadings(){
        Camera.Parameters params = camera.getParameters();
        List<Camera.Area> accelAreas = new ArrayList<Camera.Area>();
        Rect areaRect2 = new Rect(800, -1000, 1000, -800);  // specify an area in upper right of image
        //areaRect2.
        //accelAreas.add(new Camera.Area(areaRect2, 400));

    }
}








