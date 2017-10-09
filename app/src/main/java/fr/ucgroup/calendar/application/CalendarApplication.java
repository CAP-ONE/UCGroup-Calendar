package fr.ucgroup.calendar.application;

import android.app.Application;

import io.realm.Realm;


public class CalendarApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
    }
}
