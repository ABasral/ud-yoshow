package udirect.com.yoshow.Share;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
/*
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;*/
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import udirect.com.yoshow.R;
import udirect.com.yoshow.Utils.FirebaseMethods;

public class NextActivity extends AppCompatActivity {

    private static final String TAG = "NextActivity";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;

    //widgets
    private EditText mCaption;

    //vars
    private String mAppend = "file:/";
    private int imageCount = 0;
    private String imgUrl;
    private Bitmap bitmap;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        mFirebaseMethods = new FirebaseMethods(NextActivity.this);
        mCaption = (EditText) findViewById(R.id.caption) ;

        setupFirebaseAuth();

        ImageView backArrow = (ImageView) findViewById(R.id.ivBackArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing the activity");
                finish();
            }
        });


        TextView share = (TextView) findViewById(R.id.tvShare);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to the final share screen.");
                //upload the image to firebase
                Toast.makeText(NextActivity.this, "Attempting to upload new Video", Toast.LENGTH_SHORT).show();
                String caption = mCaption.getText().toString();


                ProgressDialog progressDialog = new ProgressDialog(NextActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                //Without this user can hide loader by tapping outside screen
                progressDialog.setCancelable(false);
                //Setting Title
                progressDialog.setTitle("Uploading your video");
                progressDialog.setMessage("Hold on a bit, your video will be uploaded shortly");
                progressDialog.show();

               // if(intent.hasExtra(getString(R.string.selected_image))){
                    imgUrl = android.os.Environment.getExternalStorageDirectory().getPath()+"/UDIRECT/final.mp4";
                    mFirebaseMethods.uploadNewPhoto(getString(R.string.new_photo), caption, imageCount, imgUrl,null);
                //}
              //else if(intent.hasExtra(getString(R.string.selected_bitmap))){
                //bitmap = (Bitmap) intent.getParcelableExtra(getString(R.string.selected_bitmap));
              //mFirebaseMethods.uploadNewPhoto(getString(R.string.new_photo), caption, imageCount, null,bitmap);
            //  }



            }
        });

        setImage();
    }

    private void someMethod(){
        /*
            Step 1)
            Create a data model for Photos

            Step 2)
            Add properties to the Photo Objects (caption, date, imageUrl, photo_id, tags, user_id)

            Step 3)
            Count the number of photos that the user already has.

            Step 4)
            a) Upload the photo to Firebase Storage
            b) insert into 'photos' node
            c) insert into 'user_photos' node

         */

    }


    /**
     * gets the image url from the incoming intent and displays the chosen image
     */
    private void setImage() {
        ImageView image = (ImageView) findViewById(R.id.imageShare);


        String path =  android.os.Environment.getExternalStorageDirectory().getPath()+"/UDIRECT/final.mp4";
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path,
                MediaStore.Images.Thumbnails.MINI_KIND);
            Log.d(TAG, "setImage: got new bitmap");
            image.setImageBitmap(thumb);

        File myDir = new File(android.os.Environment.getExternalStorageDirectory().getPath()+"/UDIRECT/thumb");
        File file = new File(myDir, "thumb.jpeg");
        Log.i(TAG, "" + file);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            thumb.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }



        ///String path = Environment.getExternalStorageDirectory().toString();
      /*  OutputStream fOutputStream = null;
        File file = new File(android.os.Environment.getExternalStorageDirectory().getPath()+"/UDIRECT/thumb" ,"thumb.JPEG");
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            fOutputStream = new FileOutputStream(file);

            thumb.compress(Bitmap.CompressFormat.JPEG, 100, fOutputStream);

            fOutputStream.flush();
            fOutputStream.close();

           // MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Save Failed", Toast.LENGTH_SHORT).show();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Save Failed", Toast.LENGTH_SHORT).show();
            return;
        }

*/

    }

     /*
     ------------------------------------ Firebase ---------------------------------------------
     */

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        Log.d(TAG, "onDataChange: image count: " + imageCount);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                imageCount = mFirebaseMethods.getImageCount(dataSnapshot);
                Log.d(TAG, "onDataChange: image count: " + imageCount);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
