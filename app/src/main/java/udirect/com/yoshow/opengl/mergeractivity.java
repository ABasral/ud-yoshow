package udirect.com.yoshow.opengl;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.googlecode.mp4parser.BasicContainer;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;


import java.io.RandomAccessFile;
import java.nio.channels.WritableByteChannel;
import java.util.LinkedList;
import java.util.List;

import udirect.com.yoshow.R;


public class mergeractivity extends Activity {
    private Button merger1,merger2,merger3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mergeractivity);

        merger1 = (Button) findViewById(R.id.merge1);
        merger2 = (Button) findViewById(R.id.merge2);
        merger3 = (Button) findViewById(R.id.merge3);

        merger1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {


                    String f1,f2,f3,f4;


                    f1 =android.os.Environment.getExternalStorageDirectory().getPath()+"/UDIRECT/v1.mp4";


                    f2 = android.os.Environment.getExternalStorageDirectory().getPath()+"/UDIRECT/v3.mp4";
                    f3 = android.os.Environment.getExternalStorageDirectory().getPath()+"/UDIRECT/v5.mp4";



                    com.googlecode.mp4parser.authoring.Movie[] inMovies = new com.googlecode.mp4parser.authoring.Movie[]{
                            MovieCreator.build(f1),
                            MovieCreator.build(f2),
                            MovieCreator.build(f3),
                          //  MovieCreator.build(f4),

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




                Intent intent3 = new Intent(mergeractivity.this, udirect.com.yoshow.edit.class);//ACTIVITY_NUM = 2
                startActivity(intent3);
            }
        });


        merger2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {


                    String f1,f2,f3,f4;


                    f1 =android.os.Environment.getExternalStorageDirectory().getPath()+"/UDIRECT/v1.mp4";


                    f2 = android.os.Environment.getExternalStorageDirectory().getPath()+"/UDIRECT/v2.mp4";
                    f3 = android.os.Environment.getExternalStorageDirectory().getPath()+"/UDIRECT/v4.mp4";
                   // f4 = android.os.Environment.getExternalStorageDirectory().getPath()+"/UDIRECT/v4.mp4";




                    com.googlecode.mp4parser.authoring.Movie[] inMovies = new com.googlecode.mp4parser.authoring.Movie[]{
                            MovieCreator.build(f1),
                            MovieCreator.build(f2),
                            MovieCreator.build(f3),
                           // MovieCreator.build(f4),

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

                Intent intent3 = new Intent(mergeractivity.this, udirect.com.yoshow.edit.class);//ACTIVITY_NUM = 2
                startActivity(intent3);

            }
        });

        merger3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {


                    String f1,f2,f3,f4,f5;


                    f1 =android.os.Environment.getExternalStorageDirectory().getPath()+"/UDIRECT/v1.mp4";


                    f2 = android.os.Environment.getExternalStorageDirectory().getPath()+"/UDIRECT/v2.mp4";
                    f3 = android.os.Environment.getExternalStorageDirectory().getPath()+"/UDIRECT/v3.mp4";
                    f4 = android.os.Environment.getExternalStorageDirectory().getPath()+"/UDIRECT/v4.mp4";
                    f5 = android.os.Environment.getExternalStorageDirectory().getPath()+"/UDIRECT/v5.mp4";




                    com.googlecode.mp4parser.authoring.Movie[] inMovies = new com.googlecode.mp4parser.authoring.Movie[]{
                            MovieCreator.build(f1),
                            MovieCreator.build(f2),
                            MovieCreator.build(f3),
                            MovieCreator.build(f4),
                            MovieCreator.build(f5)

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

                Intent intent3 = new Intent(mergeractivity.this, udirect.com.yoshow.edit.class);//ACTIVITY_NUM = 2
                startActivity(intent3);


            }
        });


    }


    @Override
    public void onBackPressed(){
Intent intent = new Intent(this, udirect.com.yoshow.Home.HomeActivity.class);
startActivity(intent);
    }
}