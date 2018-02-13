package com.appssquare.mahmoud.myshoppinglist;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    TextView toolBarTitle;
    public Toolbar toolbar;
    Preferences preferences;
    RadioButton radioButtonEN,radioButtonAR;
    RadioGroup radioGroupLang;
    Button btnSave;
    String lng="en";
    boolean langchanged=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences=Preferences.getInstance(this);
        setContentView(R.layout.activity_settings);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolBarTitle=(TextView)findViewById(R.id.toolbartext);
        toolBarTitle.setText(R.string.settings);

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        radioButtonEN=findViewById(R.id.radioButtonEn);
        radioButtonAR=findViewById(R.id.radioButtonAr);
        if(preferences.getKey("language_key").equals("en"))
            radioButtonEN.setChecked(true);
        else if (preferences.getKey("language_key").equals("ar"))
            radioButtonAR.setChecked(true);
        radioGroupLang=findViewById(R.id.radioGroupLang);
        radioGroupLang.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                langchanged=true;
                // checkedId is the RadioButton selected
                if(checkedId==R.id.radioButtonEn)
                    lng="en";
                else if (checkedId==R.id.radioButtonAr)
                    lng="ar";

            }
        });
        btnSave=findViewById(R.id.btnSaveSettings);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(langchanged){
                    preferences.saveKey("language_key",lng);
                    Toast.makeText(SettingsActivity.this, R.string.settings_change,Toast.LENGTH_LONG).show();

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
