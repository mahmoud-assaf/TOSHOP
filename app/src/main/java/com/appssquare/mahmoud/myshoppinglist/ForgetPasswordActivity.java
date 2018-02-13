package com.appssquare.mahmoud.myshoppinglist;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    EditText editEmail; Button resetPassword;
    ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        editEmail=findViewById(R.id.editTextforgetEemail);
        resetPassword=findViewById(R.id.buttonresertpassword);
        bar=findViewById(R.id.progressBarforget);

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Utils.isValidEmail(editEmail.getText().toString())){
                    editEmail.setError(getString(R.string.email_error));
                    return;
                }else {
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    bar.setVisibility(View.VISIBLE);
                    FirebaseAuth.getInstance().sendPasswordResetEmail(editEmail.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    AlertDialog alertDialog = new AlertDialog.Builder(ForgetPasswordActivity.this).create();
                                    if (task.isSuccessful()) {

                                    bar.setVisibility(View.INVISIBLE);
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        alertDialog.setMessage(getString(R.string.check_ur_mail));
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                        //startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
                                                        finish();
                                                    }
                                                });
                                        alertDialog.show();
                                        Log.e("reset email", "Email sent.");
                                    }else{
                                        Log.e("reset email", "wrong");
                                        bar.setVisibility(View.INVISIBLE);
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        alertDialog.setMessage(getString(R.string.check_mail_error));
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok_btn),
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                        //startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
                                                       // finish();
                                                    }
                                                });
                                        alertDialog.show();
                                    }
                                }
                            });
                }
            }
        });
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
        Log.e("mainactivity attatch", "attachBaseContext");
    }
}
