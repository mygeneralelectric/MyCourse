package com.quantum.app.mycourse.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by hua on 2015/10/29.
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
