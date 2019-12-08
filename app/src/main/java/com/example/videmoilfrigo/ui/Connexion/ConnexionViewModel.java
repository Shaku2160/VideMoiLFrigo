package com.example.videmoilfrigo.ui.Connexion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConnexionViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ConnexionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
