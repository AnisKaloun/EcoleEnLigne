package com.example.ecoleenligne.ui.Tchat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TchatViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TchatViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Tchat fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}