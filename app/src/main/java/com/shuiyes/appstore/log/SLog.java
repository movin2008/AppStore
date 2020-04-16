package com.shuiyes.appstore.log;

import android.util.Log;

import java.io.File;

public class SLog {

    private static final boolean DEBUG_E = new File("/sdcard/.appstore/debug").exists();

    public static void e(String tag, String msg){
        if(DEBUG_E){
            Log.e(tag, msg);
        }
    }

}
