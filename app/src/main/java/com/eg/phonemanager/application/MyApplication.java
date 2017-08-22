package com.eg.phonemanager.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by gqliu on 17-8-22.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
