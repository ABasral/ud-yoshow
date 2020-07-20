package udirect.com.yoshow.opengl;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import udirect.com.yoshow.R;
import udirect.com.yoshow.Utils.FilePaths;
import udirect.com.yoshow.materialcamera.MaterialCamera;
//import udirect.com.yoshow.videocompressor.video.Track;
import udirect.com.yoshow.publicvar;


public class NewStoryActivity4 extends Activity {

    public static final String TAG = "NewStoryActivity";

    private final static int CAMERA_RQ = 6969;
    private final static int DEFAULT_BITRATE = 1024000;
    private static final int RESULT_START_CAMERA = 4567;
    private static final int RESULT_START_VIDEO = 4589;
    private static final int RESULT_ADD_NEW_STORY = 7891;
    private MaterialCamera mMaterialCamera;


    private int mStartType = RESULT_START_VIDEO;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_story);
        Toast.makeText(this,"ALIGN THE SUBJECT TO THE SCREEN",Toast.LENGTH_LONG).show();
        publicvar g = publicvar.getInstance();
        g.setData(5);
        init();
    }

    private void init(){
        Log.d(TAG, "init: initializing material camera.");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

//        File saveFolder = new File(Environment.getExternalStorageDirectory(), "Stories/" + getTimestamp());
        FilePaths filePaths = new FilePaths();
        File saveFolder = new File(filePaths.STORIES);
        try{
            if (!saveFolder.mkdirs());
        }catch (RuntimeException e){
            e.printStackTrace();
        }
      /*     File saveFolder1 = new File( android.os.Environment.getExternalStorageDirectory().getPath()+"/UDIRECT");
        try{
            if (!saveFolder1.mkdirs());
        }catch (RuntimeException e){
            e.printStackTrace();
        }
*/

        mMaterialCamera = new MaterialCamera(this);                               // Constructor takes an Activity

        if(mStartType == RESULT_START_VIDEO) {
            Log.d(TAG, "init: starting camera with VIDEO enabled.");
            mMaterialCamera
                    .allowRetry(true)                                  // Whether or not 'Retry' is visible during playback
                    .autoSubmit(false)                                 // Whether or not user is allowed to playback videos after recording. This can affect other things, discussed in the next section.
                    .saveDir(saveFolder)                               // The folder recorded videos are saved to
                    .primaryColorAttr(R.attr.colorPrimaryDark)             // The theme color used for the camera, defaults to colorPrimary of Activity in the constructor
                    .showPortraitWarning(false)                         // Whether or not a warning is displayed if the user presses record in portrait orientation
                    .defaultToFrontFacing(false)                       // Whether or not the camera will initially show the front facing camera
//                .allowChangeCamera(true)                           // Allows the user to change cameras.
                    .retryExits(false)                                 // If true, the 'Retry' button in the playback screen will exit the camera instead of going back to the recorder
                    .restartTimerOnRetry(false)                        // If true, the countdown timer is reset to 0 when the user taps 'Retry' in playback
                    .continueTimerInPlayback(false)                    // If true, the countdown timer will continue to go down during playback, rather than pausing.
                    //Play with the encoding bitrate to change quality and size.
                    .videoEncodingBitRate(DEFAULT_BITRATE * 5)                     // Sets a custom bit rate for video recording.
                    .audioEncodingBitRate(50000)                       // Sets a custom bit rate for audio recording.
                    .videoFrameRate(30)                                // Sets a custom frame rate (FPS) for video recording.
//                    .qualityProfile(MaterialCamera.QUALITY_1080P)       // Sets a quality profile, manually setting bit rates or frame rates with other settings will overwrite individual quality profile settings
                    .videoPreferredHeight(720)                         // Sets a preferred height for the recorded video output.
                    .videoPreferredAspect(16f / 9f)                     // Sets a preferred aspect ratio for the recorded video output.
                    .maxAllowedFileSize(1024 * 1024 * 40)               // Sets a max file size of 4MB, recording will stop if file reaches this limit. Keep in mind, the FAT file system has a file size limit of 4GB.
                    .iconRecord(R.drawable.mcam_action_capture)        // Sets a custom icon for the button used to start recording
                    .iconStop(R.drawable.mcam_action_stop)             // Sets a custom icon for the button used to stop recording
                    .iconFrontCamera(R.drawable.mcam_camera_front)     // Sets a custom icon for the button used to switch to the front camera
                    .iconRearCamera(R.drawable.mcam_camera_rear)       // Sets a custom icon for the button used to switch to the rear camera
                    .iconPlay(R.drawable.evp_action_play)              // Sets a custom icon used to start playback
                    .iconPause(R.drawable.evp_action_pause)            // Sets a custom icon used to pause playback
                    .iconRestart(R.drawable.evp_action_restart)        // Sets a custom icon used to restart playback
                    .labelRetry(R.string.mcam_retry)                   // Sets a custom button label for the button used to retry recording, when available
                    .labelConfirm(R.string.mcam_use_video)             // Sets a custom button label for the button used to confirm/submit a recording
//                .autoRecordWithDelaySec(5)                         // The video camera will start recording automatically after a 5 second countdown. This disables switching between the front and back camera initially.
//                .autoRecordWithDelayMs(5000)                       // Same as the above, expressed with milliseconds instead of seconds.
                    .audioDisabled(false)                              // Set to true to record video without any audio.
                    .countdownSeconds(60f)
                    .start(CAMERA_RQ);
        }
        else{
            Log.d(TAG, "init: starting camera with STILLSHOT enabled.");
            mMaterialCamera
                    .allowRetry(true)                                  // Whether or not 'Retry' is visible during playback
                    .autoSubmit(false)                                 // Whether or not user is allowed to playback videos after recording. This can affect other things, discussed in the next section.
                    .saveDir(saveFolder)                               // The folder recorded videos are saved to
                    .primaryColorAttr(R.attr.colorPrimaryDark)             // The theme color used for the camera, defaults to colorPrimary of Activity in the constructor
                    .showPortraitWarning(false)                         // Whether or not a warning is displayed if the user presses record in portrait orientation
                    .defaultToFrontFacing(false)                       // Whether or not the camera will initially show the front facing camera
//                .allowChangeCamera(true)                           // Allows the user to change cameras.
                    .retryExits(false)                                 // If true, the 'Retry' button in the playback screen will exit the camera instead of going back to the recorder
                    .restartTimerOnRetry(false)                        // If true, the countdown timer is reset to 0 when the user taps 'Retry' in playback
                    .continueTimerInPlayback(false)                    // If true, the countdown timer will continue to go down during playback, rather than pausing.
                    .videoEncodingBitRate(DEFAULT_BITRATE * 5)                     // Sets a custom bit rate for video recording.
                    .audioEncodingBitRate(50000)                       // Sets a custom bit rate for audio recording.
                    .videoFrameRate(30)                                // Sets a custom frame rate (FPS) for video recording.
//                    .qualityProfile(MaterialCamera.QUALITY_1080P)       // Sets a quality profile, manually setting bit rates or frame rates with other settings will overwrite individual quality profile settings
                    .videoPreferredHeight(720)                         // Sets a preferred height for the recorded video output.
                    .videoPreferredAspect(16f / 9f)                     // Sets a preferred aspect ratio for the recorded video output.
                    .maxAllowedFileSize(1024 * 1024 * 10)               // Sets a max file size of 4MB, recording will stop if file reaches this limit. Keep in mind, the FAT file system has a file size limit of 4GB.
                    .iconRecord(R.drawable.mcam_action_capture)        // Sets a custom icon for the button used to start recording
                    .iconStop(R.drawable.mcam_action_stop)             // Sets a custom icon for the button used to stop recording
                    .iconFrontCamera(R.drawable.mcam_camera_front)     // Sets a custom icon for the button used to switch to the front camera
                    .iconRearCamera(R.drawable.mcam_camera_rear)       // Sets a custom icon for the button used to switch to the rear camera
                    .iconPlay(R.drawable.evp_action_play)              // Sets a custom icon used to start playback
                    .iconPause(R.drawable.evp_action_pause)            // Sets a custom icon used to pause playback
                    .iconRestart(R.drawable.evp_action_restart)        // Sets a custom icon used to restart playback
                    .labelRetry(R.string.mcam_retry)                   // Sets a custom button label for the button used to retry recording, when available
                    .labelConfirm(R.string.mcam_use_video)             // Sets a custom button label for the button used to confirm/submit a recording
//                .autoRecordWithDelaySec(5)                         // The video camera will start recording automatically after a 5 second countdown. This disables switching between the front and back camera initially.
//                .autoRecordWithDelayMs(5000)                       // Same as the above, expressed with milliseconds instead of seconds.
                    .audioDisabled(false)                              // Set to true to record video without any audio.
                    .countdownSeconds(60f)
                    .stillShot()
                    .start(CAMERA_RQ);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: incoming result.");
        // Received recording or error from MaterialCamera
        if (requestCode == CAMERA_RQ) {

            if (resultCode == RESULT_OK) {

                Log.d(TAG, "onActivityResult: result is OK.");
                //  Toast.makeText(this, "Saved to: " + data.getDataString(), Toast.LENGTH_LONG).show();
            }
            else if(resultCode == RESULT_START_CAMERA){
                Log.d(TAG, "onActivityResult: got activity result. Opening Camera.");
                mStartType = RESULT_START_CAMERA;
                init();
            }
            else if(resultCode == RESULT_START_VIDEO){
                Log.d(TAG, "onActivityResult: got activity result. Opening video.");
                mStartType = RESULT_START_VIDEO;
                init();
            }
            else if(resultCode == RESULT_ADD_NEW_STORY){
                //Log.d(TAG, "onActivityResult: preparing to add new story.");
                // Log.d(TAG, "onActivityResult: upload uri: " + data.getData());
                //  Toast.makeText(this, "GOT YOU", Toast.LENGTH_LONG).show();
                String sourceFilename= data.getData().getPath();
                String destinationFilename = android.os.Environment.getExternalStorageDirectory().getPath()+"/UDIRECT/v5.mp4";

                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;

                try {
                    bis = new BufferedInputStream(new FileInputStream(sourceFilename));
                    bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
                    byte[] buf = new byte[1024];
                    bis.read(buf);
                    do {
                        bos.write(buf);
                    } while(bis.read(buf) != -1);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (bis != null) bis.close();
                        if (bos != null) bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            publicvar g = publicvar.getInstance();
            g.setData(1);
            Intent intent3 = new Intent(this, mergeractivity.class);//ACTIVITY_NUM = 2
            startActivity(intent3);

/*
            try {


                String f1,f2,f3,f4;


                f1 =android.os.Environment.getExternalStorageDirectory().getPath()+"/UDIRECT/v1.mp4";


                f2 = android.os.Environment.getExternalStorageDirectory().getPath()+"/UDIRECT/v2.mp4";
                f3 = android.os.Environment.getExternalStorageDirectory().getPath()+"/UDIRECT/v3.mp4";
                f4 = android.os.Environment.getExternalStorageDirectory().getPath()+"/UDIRECT/v4.mp4";




                com.googlecode.mp4parser.authoring.Movie[] inMovies = new com.googlecode.mp4parser.authoring.Movie[]{
                        MovieCreator.build(f1),
                        MovieCreator.build(f2),
                        MovieCreator.build(f3),
                        MovieCreator.build(f4),

                };
                List<Track> videoTracks = new LinkedList<Track>();
                List<Track> audioTracks = new LinkedList<Track>();
                for (com.googlecode.mp4parser.authoring.Movie m : inMovies) {
                    for (Track t : m.getTracks()) {
                        if (t.getHandler().equals("soun")) {
                            audioTracks.add(t);
                        }
                        if (t.getHandler().equals("vide")) {
                            videoTracks.add(t);
                        }
                    }
                }
                com.googlecode.mp4parser.authoring.Movie result = new com.googlecode.mp4parser.authoring.Movie();
                if (audioTracks.size() > 0) {
                    result.addTrack(new AppendTrack(audioTracks
                            .toArray(new Track[audioTracks.size()])));
                }
                if (videoTracks.size() > 0) {
                    result.addTrack(new AppendTrack(videoTracks
                            .toArray(new Track[videoTracks.size()])));
                }
                //Environment.getExternalStorageDirectory()+
                BasicContainer out = (BasicContainer) new DefaultMp4Builder().build(result);
                WritableByteChannel fc = new RandomAccessFile(
                        String.format(android.os.Environment.getExternalStorageDirectory().getPath()+"/UDIRECT/final.mp4"), "rw").getChannel();
                out.writeContainer(fc);
                fc.close();

            } catch (Exception e) {
                Log.d("Rvg", "exeption" + e);
                Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_LONG)
                        .show();
            }
*/
/*
            Intent intent3 = new Intent(this, udirect.com.yoshow.edit.class);//ACTIVITY_NUM = 2
            startActivity(intent3);*/

         //   startActivity(intent);

              /*  if(data.hasExtra(MaterialCamera.DELETE_UPLOAD_FILE_EXTRA)){
                    setResult(
                            RESULT_ADD_NEW_STORY,
                            getIntent()
                                    .putExtra(MaterialCamera.DELETE_UPLOAD_FILE_EXTRA, true)
                                    .putExtra(MaterialCamera.STATUS_EXTRA, MaterialCamera.STATUS_RECORDED)
                                    .setDataAndType(data.getData(), data.getType()));
                    finish();
                }
                else{
                    setResult(
                            RESULT_ADD_NEW_STORY,
                            getIntent()
                                    .putExtra(MaterialCamera.DELETE_UPLOAD_FILE_EXTRA, false)
                                    .putExtra(MaterialCamera.STATUS_EXTRA, MaterialCamera.STATUS_RECORDED)
                                    .setDataAndType(data.getData(), data.getType()));
                    finish();
                }*/


        }
        else if(data != null) {
            Log.d(TAG, "onActivityResult: something went wrong.");
            Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(this,NewStoryActivity4.class);
        startActivity(intent);
    }
}
























