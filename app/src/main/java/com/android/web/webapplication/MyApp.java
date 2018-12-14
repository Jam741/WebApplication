package com.android.web.webapplication;

import android.app.Application;
import android.webkit.WebView;

/**
 * Created by hejiaming on 2018/12/14.
 *
 * @desciption:
 */
public class MyApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        WebView.setWebContentsDebuggingEnabled(true);
    }
}
