package com.example.chuong_7.ui.audio;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class audioViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public audioViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is audio fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
