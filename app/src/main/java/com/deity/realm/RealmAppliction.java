package com.deity.realm;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * 这里添加文件描述
 * Create by fengwenhua at 2018/9/6
 **/
public class RealmAppliction extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initDB();
    }

    private void initDB(){
        Realm.init(getApplicationContext());
        RealmConfiguration configuration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(configuration);
    }
}
