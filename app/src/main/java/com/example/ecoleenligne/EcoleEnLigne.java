package com.example.ecoleenligne;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class EcoleEnLigne extends Application {

    private FirebaseFirestore db;

    @Override
    public void onCreate(){
    super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build();
        db=FirebaseFirestore.getInstance();
        db.setFirestoreSettings(settings);

    }
}
