package com.android.realmdb.app;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/*
The RealmConfiguration can be saved as a default configuration.
Setting a default configuration in your custom Application class,
will ensure that it is available in the rest of your code.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {

        super.onCreate();
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

    }
}