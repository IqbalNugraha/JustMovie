package com.example.justmovie.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingPreference {

    private static final String Pref_Name = "setting_pref";
    private static final String Release_Reminder = "isRelease";
    private static final String DAILY_REMINDER = "isDaily";
    private final SharedPreferences sharedPreferences;


    public SettingPreference (Context context){
        sharedPreferences = context.getSharedPreferences(Pref_Name, Context.MODE_PRIVATE);
    }

    public void setDailyReminder(boolean isActive) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(DAILY_REMINDER, isActive);
        editor.apply();
    }

    public void setRelease_Reminder (boolean isActive){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Release_Reminder, isActive);
        editor.apply();
    }

    public boolean getDailyReminder() {
        return sharedPreferences.getBoolean(DAILY_REMINDER, false);
    }

    public boolean getRelease_Reminder(){
        return sharedPreferences.getBoolean(Release_Reminder, false);
    }
}
