package com.example.ecoleenligne.ui.Recommandation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecommandationViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RecommandationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the Recommandation fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}