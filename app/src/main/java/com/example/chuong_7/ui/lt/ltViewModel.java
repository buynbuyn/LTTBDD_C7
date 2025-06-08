package com.example.chuong_7.ui.lt;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ltViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public ltViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is lt fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
