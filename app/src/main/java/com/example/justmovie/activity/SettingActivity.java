package com.example.justmovie.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.justmovie.Notification.NotificationDailyReceiver;
import com.example.justmovie.Notification.NotificationReleaseReceiver;
import com.example.justmovie.R;
import com.example.justmovie.preference.SettingPreference;

public class SettingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Switch switchReminder;
    private Switch switchRelease;
    private NotificationDailyReceiver notificationDailyReceiver;
    private NotificationReleaseReceiver notificationReleaseReceiver;
    private SettingPreference settingPreference;
    String[] language = {"English","Indonesia","Germany","Japan"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        switchRelease = findViewById(R.id.swRealeseToday);
        switchReminder = findViewById(R.id.swDailyToday);

        notificationDailyReceiver = new NotificationDailyReceiver();
        notificationReleaseReceiver = new NotificationReleaseReceiver();
        settingPreference = new SettingPreference(this);

        setSwitchRelease();
        setSwitchReminder();

        Spinner spinner = findViewById(R.id.comboBox);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_item, language);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

        switchRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switchRelease.isChecked()) {
                    notificationReleaseReceiver.setReleaseAlarm(getApplicationContext());
                    settingPreference.setRelease_Reminder(true);
                    Toast.makeText(getApplicationContext(), "Release Reminder is Active", Toast.LENGTH_SHORT).show();
                } else {
                    notificationReleaseReceiver.cancelAlarm(getApplicationContext());
                    settingPreference.setRelease_Reminder(false);
                    Toast.makeText(getApplicationContext(), "Release Reminder is NonActive", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Switch Reminder OnClick
        switchReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchReminder.isChecked()) {
                    notificationDailyReceiver.setDailyAlarm(getApplicationContext());
                    settingPreference.setDailyReminder(true);
                    Toast.makeText(getApplicationContext(), "Daily Reminder is Active", Toast.LENGTH_SHORT).show();
                } else {
                    notificationDailyReceiver.cancelAlarm(getApplicationContext());
                    settingPreference.setDailyReminder(false);
                    Toast.makeText(getApplicationContext(), "Daily Reminder is NonActive", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView ig = findViewById(R.id.ig);
        ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/justmovie21/"));
                startActivity(intent);
            }
        });

        ImageView github = findViewById(R.id.github);
        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/IqbalNugraha"));
                startActivity(intent);
            }
        });

        ImageView gmail = findViewById(R.id.gmail);
        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://mail.google.com/mail/u/0/#inbox?compose=new"));
                startActivity(intent);
            }
        });
    }

    private void setSwitchReminder() {
        if (settingPreference.getDailyReminder()) switchReminder.setChecked(true);
        else switchReminder.setChecked(false);
    }

    private void setSwitchRelease() {
        if (settingPreference.getRelease_Reminder()) switchRelease.setChecked(true);
        else switchRelease.setChecked(false);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i){
            case 0:
                Toast.makeText(getApplicationContext(),
                                "Language is "+language[i],
                                Toast.LENGTH_SHORT)
                        .show();
                break;
            case 1:
                Toast.makeText(getApplicationContext(),
                                "Language is "+language[i],
                                Toast.LENGTH_SHORT)
                        .show();
                break;
            case 2:
                Toast.makeText(getApplicationContext(),
                                "Language is "+language[i],
                                Toast.LENGTH_SHORT)
                        .show();
                break;
            case 3:
                Toast.makeText(getApplicationContext(),
                                "Language is "+language[i],
                                Toast.LENGTH_SHORT)
                        .show();
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}