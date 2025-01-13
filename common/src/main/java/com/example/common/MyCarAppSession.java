package com.example.common;

import static com.example.common.MyCarAppService.INTENT_ACTION_MY_CUSTOM;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.car.app.CarContext;
import androidx.car.app.CarToast;
import androidx.car.app.Screen;
import androidx.car.app.ScreenManager;
import androidx.car.app.Session;
import androidx.car.app.media.CarAudioRecord;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.example.common.screen.CarInfoScreen;
import com.example.common.screen.MainScreen;
import com.example.common.utils.AudioRecorder;

import android.media.AudioFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public final class MyCarAppSession extends Session {


    //Coś intentowe
//    @Override
//    public void onNewIntent(@NonNull Intent intent) {
//        // Process various deeplink intents.
//        ScreenManager screenManager = getCarContext().getCarService(ScreenManager.class);
//
//        Uri uri = intent.getData();
//        if (uri != null
//                && URI_SCHEME.equals(uri.getScheme())
//                && URI_HOST.equals(uri.getSchemeSpecificPart())) {
//
//            Screen top = screenManager.getTop();
//
//            if (INTENT_ACTION_MY_CUSTOM.equals(uri.getFragment())) {
//                if (!(top instanceof CarInfoScreen)) {
//                    screenManager.push(new CarInfoScreen(getCarContext()));
//                }
//            }
//        }
//    }

    @Override
    @NonNull
    public Screen onCreateScreen(@NonNull Intent intent)
    {
        return new MainScreen(getCarContext(), this);
    }
}

