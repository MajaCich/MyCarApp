package com.example.common;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.car.app.Screen;
import androidx.car.app.Session;
import com.example.common.screen.MainScreen;

public final class MyCarAppSession extends Session {
    @Override
    @NonNull
    public Screen onCreateScreen(@NonNull Intent intent)
    {
        return new MainScreen(getCarContext());
    }
}

