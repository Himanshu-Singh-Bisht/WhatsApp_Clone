package com.himanshu.whatsappclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("CwsMPjkHYjNwwviAj9UVAf4FPz0TexYjvffVMwqB")
                // if defined
                .clientKey("zXsDPMW21GMqlC1OZXshltZWmFqjzabX5u0cIDy0")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
