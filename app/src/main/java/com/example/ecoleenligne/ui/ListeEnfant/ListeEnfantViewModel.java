package com.example.ecoleenligne.ui.ListeEnfant;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListeEnfantViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ListeEnfantViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the ListeEnfant fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}