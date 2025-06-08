package com.example.chuong_7.ui.sm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class smViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public smViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is sm fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
