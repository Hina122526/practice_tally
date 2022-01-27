package com.hina122526.tally;

import android.app.Application;

import com.hina122526.tally.db.DBManager;

//表示全局可使用的類別
public class UniteApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化資料庫
        DBManager.initDB(getApplicationContext());
    }
}
