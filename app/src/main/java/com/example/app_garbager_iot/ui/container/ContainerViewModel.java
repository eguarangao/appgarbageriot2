package com.example.app_garbager_iot.ui.container;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ContainerViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ContainerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Container fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}