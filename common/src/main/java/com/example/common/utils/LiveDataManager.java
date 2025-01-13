package com.example.common.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class LiveDataManager {
    private static final LiveDataManager instance = new LiveDataManager();

    private final MutableLiveData<Integer> R_value = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> G_value = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> B_value = new MutableLiveData<>(0);

    private LiveDataManager() {}

    public static LiveDataManager getInstance() {
        return instance;
    }

    public LiveData<Integer> getR_value() {
        return R_value;
    }

    public LiveData<Integer> getG_value() {
        return G_value;
    }

    public LiveData<Integer> getB_value() {
        return B_value;
    }

    public void incrementR_value(int i) {
        if (R_value.getValue() != null) {
            if(i>0) {
                R_value.setValue(Math.min(R_value.getValue() + i, 255));
            }
            else   R_value.setValue(Math.max(R_value.getValue() + i, 0));
        }
    }

    public void incrementG_value(int i) {
        if (G_value.getValue() != null) {
            if(i>0) {
                G_value.setValue(Math.min(G_value.getValue() + i, 255));
            }
            else   G_value.setValue(Math.max(G_value.getValue() + i, 0));
        }
    }

    public void incrementB_value(int i) {
        if (B_value.getValue() != null) {
            if(i>0) {
                B_value.setValue(Math.min(B_value.getValue() + i, 255));
            }
            else   B_value.setValue(Math.max(B_value.getValue() + i, 0));
        }
    }
}

