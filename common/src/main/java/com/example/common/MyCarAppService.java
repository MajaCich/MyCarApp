package com.example.common;

import androidx.annotation.NonNull;
import androidx.car.app.CarAppService;
import androidx.car.app.Session;
import androidx.car.app.validation.HostValidator;


public final class MyCarAppService extends CarAppService {
    public static final String INTENT_ACTION_MY_CUSTOM =
            "com.example.mycarapp.INTENT_ACTION_MY_CUSTOM";
    @NonNull
    @Override
    public HostValidator createHostValidator() {
        return HostValidator.ALLOW_ALL_HOSTS_VALIDATOR;
    }

    @Override
    @NonNull
    public Session onCreateSession() {

        return new MyCarAppSession();
    }
}
