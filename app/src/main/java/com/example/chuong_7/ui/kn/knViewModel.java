package com.example.chuong_7.ui.kn;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class knViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public knViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is kn fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
