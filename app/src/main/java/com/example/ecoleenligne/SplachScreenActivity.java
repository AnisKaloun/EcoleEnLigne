package com.example.ecoleenligne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class SplachScreenActivity extends AppCompatActivity {
    Animation topAnim, bottomAnim;
    ImageView image;
    TextView slogan;
    FirebaseAuth mAuth;
    private FirebaseFirestore db;
    String TAG="SplachScreenActivity";
    private static int SPLACH_screen= 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splach_screen);
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        image= findViewById(R.id.imageView);

        slogan= findViewById(R.id.textView2);
        image.setAnimation(topAnim);

        slogan.setAnimation(bottomAnim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mAuth.getCurrentUser()==null) {
                    Log.d(TAG, "run: je suis dans le lancement du login");
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
                    if(!previouslyStarted) {
                        SharedPreferences.Editor edit = prefs.edit();
                        edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
                        edit.commit();
                        Intent intent = new Intent(SplachScreenActivity.this, PresentationActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(SplachScreenActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }


                }
                else{
                    Log.d(TAG, "run: "+mAuth.getCurrentUser().getEmail());
                    FirebaseUser user = mAuth.getCurrentUser();
                    DocumentReference docRef = db.collection("Users").document("user "+user.getUid());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    if (document.get("nombreEnfant") != null) {
                                        //il y a que le pere qui as des enfant
                                        Intent intent = new Intent(SplachScreenActivity.this, MainActivity.class);
                                        intent.putExtra("Parent", true);
                                        intent.putExtra("nombreEnfant", Integer.parseInt(document.get("nombreEnfant").toString()));
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        //ici c le fils
                                        Intent intent = new Intent(SplachScreenActivity.this, MainActivity.class);
                                        intent.putExtra("Parent", false);
                                        startActivity(intent);
                                        finish();


                                    }
                                }
                            }
                        }
                });
                }
                finish();
            }
        },SPLACH_screen);
    }
}
