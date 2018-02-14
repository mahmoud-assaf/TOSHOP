package com.appssquare.mahmoud.myshoppinglist;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    LoginButton facebookLoginButton;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    CallbackManager mCallbackManager;
    Button loginBtn,skipBtn,signUpBtn,forgetPasswordBtn;
    EditText emailEdit,passwordEdit;
    ProgressBar bar;
    String TAG ="Hey";
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //printHASHKEY();
        setContentView(R.layout.activity_login);

        emailEdit=(EditText)findViewById(R.id.editTextEmaillogin);
        passwordEdit=(EditText)findViewById(R.id.editTextPasswordlogin);
        loginBtn=(Button)findViewById(R.id.btnLogin);
        skipBtn=(Button)findViewById(R.id.skipbtn);
        signUpBtn=(Button)findViewById(R.id.signupbtn);
        forgetPasswordBtn=(Button)findViewById(R.id.forgetPasswordbtn);
        bar=(ProgressBar)findViewById(R.id.progressBarlogin);
        loginBtn.setOnClickListener(this);
        skipBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);
        forgetPasswordBtn.setOnClickListener(this);


        facebookLoginButton= (LoginButton) findViewById(R.id.fblogin_button);
        mAuth = FirebaseAuth.getInstance();

        mCallbackManager = CallbackManager.Factory.create();
        facebookLoginButton.setReadPermissions("email");
        facebookLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.e(TAG,"Hello"+loginResult.getAccessToken().getToken());
                //  Toast.makeText(MainActivity.this, "Token:"+loginResult.getAccessToken(), Toast.LENGTH_SHORT).show();

                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


        mAuthListener = new FirebaseAuth.AuthStateListener(){


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user!=null){
                    name = user.getDisplayName();
                    Toast.makeText(LoginActivity.this,""+user.getDisplayName(),Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(LoginActivity.this, R.string.something_wrong,Toast.LENGTH_LONG).show();
                }


            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null) {
            //navigate to main activity ,,for now just log out again
           /* Log.e("logged in", currentUser.getDisplayName());
            FirebaseAuth.getInstance().signOut();*/
            // updateUI(currentUser);
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.e(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Log.e(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Success",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent main=new Intent(LoginActivity.this,MainActivity.class);
                            Preferences preferences=Preferences.getInstance(LoginActivity.this);
                            preferences.saveKey("userId",user.getDisplayName());
                            startActivity(main);
                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this, R.string.auth_error,
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });
    }


    public void  printHASHKEY(){
       
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:

                String email=emailEdit.getText().toString();
                String password=passwordEdit.getText().toString();
                if(!Utils.isValidEmail(email)) {
                    Toast.makeText(LoginActivity.this, R.string.email_error,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!Utils.isValidPassword(password)){
                    Toast.makeText(LoginActivity.this, R.string.password_error,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                bar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.e(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent main=new Intent(LoginActivity.this,MainActivity.class);
                                    Preferences preferences=Preferences.getInstance(LoginActivity.this);
                                    preferences.saveKey("userId",user.getEmail());
                                    startActivity(main);
                                    finish();
                                   // updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.e(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, R.string.auth_error,
                                            Toast.LENGTH_SHORT).show();
                                    bar.setVisibility(View.INVISIBLE);
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                    //updateUI(null);
                                }

                                // ...
                            }
                        });


                //test and login
                break;

            case R.id.signupbtn:
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));

                break;
            case R.id.forgetPasswordbtn:
                startActivity(new Intent(LoginActivity.this,ForgetPasswordActivity.class));
                //launch reset password activity
                break;
            case R.id.skipbtn:
                //skipp to guest user
                Intent main=new Intent(LoginActivity.this,MainActivity.class);
                main.putExtra("ISUSER",false);
                startActivity(main);

                break;

        }
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
        Log.e("mainactivity attatch", "attachBaseContext");
    }
}

