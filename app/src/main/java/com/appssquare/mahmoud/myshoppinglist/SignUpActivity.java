package com.appssquare.mahmoud.myshoppinglist;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    Button signUpBtn;
    ProgressBar bar;
    EditText emailEdit,passwordEdit,confirmPasswordEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_sign_up);

        emailEdit=(EditText)findViewById(R.id.editTextEmail);
        passwordEdit=(EditText)findViewById(R.id.editTextPassword);
        confirmPasswordEdit=(EditText)findViewById(R.id.edittextconfirmpassword);
        signUpBtn=(Button)findViewById(R.id.createaccountbtn);
        bar=(ProgressBar)findViewById(R.id.progressBarsignup);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Utils.isValidEmail(emailEdit.getText().toString())) {
                    emailEdit.setError(getString(R.string.email_error));
                    return;
                }
                if(!Utils.isValidPassword(passwordEdit.getText().toString())){
                    passwordEdit.setError(getString(R.string.password_error));
                    return;
                }
                if(!Utils.isMatchPassword(passwordEdit.getText().toString(),confirmPasswordEdit.getText().toString())){
                    confirmPasswordEdit.setError(getString(R.string.password_match_error));
                    return;
                }

                //validated .try create account
                bar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                mAuth.createUserWithEmailAndPassword(emailEdit.getText().toString(), passwordEdit.getText().toString())
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.e("sign up result", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String displayName=mAuth.getCurrentUser().getDisplayName();
                                    if(displayName==null)
                                        displayName=mAuth.getCurrentUser().getEmail();
                                    Log.e("sign up result",displayName);

                                    Intent main=new Intent(SignUpActivity.this,MainActivity.class);
                                    Preferences preferences=Preferences.getInstance(SignUpActivity.this);
                                    preferences.saveKey("userId",displayName);
                                    finish();
                                   // updateUI(user);
                                } else {
                                    bar.setVisibility(View.GONE);
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                    // If sign in fails, display a message to the user.
                                    Log.e("sign up result", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignUpActivity.this, getString(R.string.auth_error),
                                            Toast.LENGTH_SHORT).show();

                                }

                                // ...
                            }
                        });


                }

        });


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
        Log.e("mainactivity attatch", "attachBaseContext");
    }



}
