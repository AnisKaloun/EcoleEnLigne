package com.example.ecoleenligne.ui.Cours;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CoursViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CoursViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the Courses fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}