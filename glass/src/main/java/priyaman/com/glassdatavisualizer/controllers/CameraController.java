package priyaman.com.glassdatavisualizer.controllers;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.CameraProfile;
import android.media.MediaRecorder;
import priyaman.com.glassdatavisualizer.utilities.Log;

import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import priyaman.com.glassdatavisualizer.R;
import priyaman.com.glassdatavisualizer.utilities.CameraUtils;
import priyaman.com.glassdatavisualizer.utilities.Utilities;

/**
 * Created by priya_000 on 5/19/2015.
 */
public class CameraController extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder finalView;
    private Camera camera;
    private MediaRecorder mediaRecorder;
    public boolean isRecording = false;
    public RecThread recordThread = null;
    public StopRecordingThread stopRecordingThread = null;
    public CameraController(Context context, Camera camera){
        super(context);
        Log.d("In Camera Controller Constructor");
        this.camera = camera;
        finalView = getHolder();
        finalView.addCallback(this);
        finalView.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mediaRecorder = new MediaRecorder();
        recordThread = new RecThread(mediaRecorder);
        stopRecordingThread = new StopRecordingThread((mediaRecorder));
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


    public class RecThread implements Runnable{
        MediaRecorder mediaRecorder = null;
        public RecThread(MediaRecorder mediaRecorder){
            this.mediaRecorder = mediaRecorder;
        }

        public void run(){
            startRecording();
        }

        public boolean prepareVideoRecorder(){
            Log.d("Preparing Video Recorder");
            try {
                if (camera == null) {
                    camera = CameraUtils.getCameraInstance();
                }
                camera.unlock();
                //mediaRecorder = new MediaRecorder();
                mediaRecorder.setCamera(camera);
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                try {
                    CamcorderProfile camProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
                    mediaRecorder.setProfile(camProfile);
                }catch(Exception e){
                    Log.e("Exception caught in setting profile");
                    e.printStackTrace();
                }
                //mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                //mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                mediaRecorder.setPreviewDisplay(finalView.getSurface());
                //mediaRecorder.setCaptureRate(0.1);
                File outputFile = CameraUtils.getOutputMediaFile(CameraUtils.MEDIA_TYPE_VIDEO);
                Utilities.sensorFileName = outputFile.getName();
                Utilities.logSensorValues = true;
                if (mediaRecorder == null) {
                    Log.i("media is null!");
                }
                mediaRecorder.setOutputFile(outputFile.toString());

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
                //return true;
            }catch(Exception e){
                Log.e("Error in prepare Video Recorder",e);
                e.printStackTrace();
                return false;
            }finally{
                //this.releaseCamera();
                //Log.d("Camera released");
                //return false;
                 Log.d("In finally block");
            }
            return true;
        }

        public void startRecording(){
            Log.d("In Start Recording");
            Log.d("Camera is :" + String.valueOf(camera==null));
            if(isRecording){
                Log.i("Stopping Recording");
                Utilities.logSensorValues = false;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Log.i("Sleeping for 1 sec after stopping recording");
                    }
                }, 1000);
                mediaRecorder.stop();

                Log.i("Video Stored@");
                releaseMediaRecorder();
                try {
                    //camera.reconnect();
                    //camera.lock();
                    if(camera == null){
                        camera = CameraUtils.getCameraInstance();
                    }

                    camera.setPreviewDisplay(getHolder());
                    camera.lock();
                    camera.startPreview();
                }catch(Exception e){
                    Log.e("Preview display on setpreview after release media recotrdr",e);
                }


                isRecording = false;
            }else{
                if(prepareVideoRecorder()){
                    mediaRecorder.start();
                    Log.i("Started Recording");
                    isRecording = true;
                }
            }
        }
    }

    public void releaseMediaRecorder(){
        if (mediaRecorder != null) {
            mediaRecorder.reset();   // clear recorder configuration
            mediaRecorder.release(); // release the recorder object

            //mediaRecorder = null; //Not setting it to null as it is required for reuse
          /*  try {
                camera.lock();           // lock camera for later use
            }catch(Exception e){
                e.printStackTrace();
            }*/
        }
    }

    public void releaseCamera(){
        if (camera != null){
            camera.release();        // release the camera for other applications
            camera = null;
        }
    }

    public class StopRecordingThread implements Runnable{
        MediaRecorder mediaRecorder = null;
        public StopRecordingThread(MediaRecorder mediaRecorder){
            this.mediaRecorder = mediaRecorder;
        }
        public void run(){

            Utilities.isStopping = true;
            try {
                Log.i("Sleeping the thread for 1 sec");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i("Stopping Recording");

            mediaRecorder.stop();
            Log.i("Video Stored@" );
            releaseMediaRecorder();
            Utilities.isStopping = false;
            try {
                //camera.reconnect();
                //camera.lock();
                if(camera == null){
                    camera = CameraUtils.getCameraInstance();
                }

                camera.setPreviewDisplay(getHolder());
                camera.lock();
                camera.startPreview();
            }catch(Exception e){
                Log.e("Preview display on setpreview after release media recotrdr",e);
            }

            Utilities.logSensorValues = false;
            isRecording = false;
        }
    }


}








