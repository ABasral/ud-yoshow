package udirect.com.yoshow.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import udirect.com.yoshow.Home.HomeActivity;
import udirect.com.yoshow.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final Boolean CHECK_IF_VERIFIED = false;

    private static final int RC_SIGN_IN = 234;

    //Tag for the logs optional


    //creating a GoogleSignInClient object
    GoogleSignInClient mGoogleSignInClient;
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Context mContext;
    private ProgressBar mProgressBar;
    private EditText mEmail, mPassword;
    private TextView mPleaseWait;

   // private static final String TAG = "AndroidClarified";

    private SignInButton googleSignInButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        //mPleaseWait = (TextView) findViewById(R.id.pleaseWait);
        mEmail = (EditText) findViewById(R.id.input_email);
        mPassword = (EditText) findViewById(R.id.input_password);
        mContext = LoginActivity.this;
        Log.d(TAG, "onCreate: started.");

        googleSignInButton = findViewById(R.id.sign_in_button);

 //     mPleaseWait.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);

        FirebaseApp.initializeApp(this);
        setupFirebaseAuth();
        init();

    }

    private boolean isStringNull(String string){
        Log.d(TAG, "isStringNull: checking string if null.");

        if(string.equals("")){
            return true;
        }
        else{
            return false;
        }
    }

     /*
    ------------------------------------ Firebase ---------------------------------------------
     */

     private void init(){

         //initialize the button for logging in
         Button btnLogin = (Button) findViewById(R.id.btn_login);
         btnLogin.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Log.d(TAG, "onClick: attempting to log in.");

                 String email = mEmail.getText().toString();
                 String password = mPassword.getText().toString();

                 if(isStringNull(email) && isStringNull(password)){
                     Toast.makeText(mContext, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                 }else{
                     mProgressBar.setVisibility(View.VISIBLE);
                     //mPleaseWait.setVisibility(View.VISIBLE);

                     mAuth.signInWithEmailAndPassword(email, password)
                             .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                 @Override
                                 public void onComplete(@NonNull Task<AuthResult> task) {
                                     Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                                     FirebaseUser user = mAuth.getCurrentUser();

                                     // If sign in fails, display a message to the user. If sign in succeeds
                                     // the auth state listener will be notified and logic to handle the
                                     // signed in user can be handled in the listener.
                                     if (!task.isSuccessful()) {
                                         Log.w(TAG, "signInWithEmail:failed", task.getException());

                                         Toast.makeText(LoginActivity.this, getString(R.string.auth_failed),
                                                 Toast.LENGTH_SHORT).show();
                                         mProgressBar.setVisibility(View.GONE);
                                         //mPleaseWait.setVisibility(View.GONE);
                                     }
                                     else{
                                         try{
                                             if(CHECK_IF_VERIFIED){
                                                 if(user.isEmailVerified()){
                                                     Log.d(TAG, "onComplete: success. email is verified.");
                                                     Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                     startActivity(intent);
                                                 }else{
                                                     Toast.makeText(mContext, "Email is not verified \n check your email inbox.", Toast.LENGTH_SHORT).show();
                                                     mProgressBar.setVisibility(View.GONE);
                                                     //mPleaseWait.setVisibility(View.GONE);
                                                     mAuth.signOut();
                                                 }
                                             }
                                             else{
                                                 Log.d(TAG, "onComplete: success. email is verified.");
                                                 Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                 startActivity(intent);
                                             }

                                         }catch (NullPointerException e){
                                             Log.e(TAG, "onComplete: NullPointerException: " + e.getMessage() );
                                         }
                                     }

                                     // ...
                                 }
                             });
                 }

             }
         });

         TextView linkSignUp = (TextView) findViewById(R.id.link_signup);
         linkSignUp.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Log.d(TAG, "onClick: navigating to register screen");
                 Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                 startActivity(intent);
             }
         });

         /*
         If the user is logged in then navigate to HomeActivity and call 'finish()'
          */
         if(mAuth.getCurrentUser() != null){
             Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
             startActivity(intent);
             finish();
         }

         GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                 .requestIdToken(getString(R.string.default_web_client_id))
                 .requestEmail()
                 .build();

         //Then we will get the GoogleSignInClient object from GoogleSignIn class
         mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

         //Now we will attach a click listener to the sign_in_button
         //and inside onClick() method we are calling the signIn() method that will open
         //google sign in intent
         googleSignInButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 signIn();
             }
         });
     }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                //authenticating with firebase
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                //Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(LoginActivity.this, "error reading account", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        //Now using firebase we are signing in the user here
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(LoginActivity.this, "User Signed In", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }


    //this method is called on click
    private void signIn() {
        //getting the google signin intent
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();

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


    @Override
    public void onBackPressed(){

    }


}
























