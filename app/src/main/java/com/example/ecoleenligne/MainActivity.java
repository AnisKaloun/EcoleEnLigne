package com.example.ecoleenligne;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.ecoleenligne.Model.*;

import com.example.ecoleenligne.Model.Eleve;
import com.example.ecoleenligne.Model.Parent;
import com.example.ecoleenligne.ui.ListeEnfant.ListeEnfantFragment;
import com.example.ecoleenligne.ui.Recommandation.RecommandationFragment;
import com.google.android.gms.measurement.module.Analytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //recupere les information du profile d'un eleve
    TextView tvnamecomplet, tvprenom, tvemail, tvniveau,tvabonnement, tvformule;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String TAG="MainActivity";
    private boolean parent;
    private Parent UserParent;
    private Eleve UserEnfant;
    private int nbrEnfant;
    private FirebaseAnalytics mFirebaseAnalytics;

    private long start;

    @Override
    protected void onResume() {
        super.onResume();
        if(!parent)
        {
            //pour les durées
            start = System.currentTimeMillis();
            //pour les moment de connexion
            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            mAuth=FirebaseAuth.getInstance();
            db=FirebaseFirestore.getInstance();
            String InstantConnexion=currentDate+" "+currentTime;
            DocumentReference docRef = db.collection("Users").document("user "+mAuth.getCurrentUser().getUid());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Eleve eleve = documentSnapshot.toObject(Eleve.class);
                    HashMap<String, Integer> momentConnexion = eleve.getMomentConnexion();
                    if(momentConnexion!=null) {
                        //il existe

                        momentConnexion.put(""+InstantConnexion, 1);
                            db.collection("Users").document("user " + mAuth.getCurrentUser().getUid())
                                    .update("momentConnexion", momentConnexion);
                    }
                    else
                    {
                        HashMap<String,Integer>TempsConnexionNew=new HashMap<String, Integer>();
                        TempsConnexionNew.put(""+InstantConnexion,1);
                        db.collection("Users").document("user "+mAuth.getCurrentUser().getUid())
                                .update("momentConnexion",momentConnexion);
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "onDestroy: "+mAuth.getCurrentUser());
        if(!parent) {
            String currentTime = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            Log.d(TAG, "onPause: " + currentTime);
            mAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("Users").document("user " + mAuth.getCurrentUser().getUid());
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        long elapsedTime = System.currentTimeMillis() - start;
                        Log.d(TAG, "onPause: Parent" + parent);
                        double Minute = elapsedTime / 1000;
                        Minute = Minute / 60.0;
                        Log.d(TAG, "onPause: " + Minute); // some storing procedure
                        Eleve eleve = documentSnapshot.toObject(Eleve.class);
                        HashMap<String, Double> TempsConnexion = eleve.getTempsConnexion();
                        if (TempsConnexion != null) {
                            //il existe
                            if (TempsConnexion.get("" + currentTime) == null) {
                                //premiere connexion de la journée
                                TempsConnexion.put("" + currentTime, Minute);
                                db.collection("Users").document("user " + mAuth.getCurrentUser().getUid())
                                        .update("tempsConnexion", TempsConnexion);

                            } else {
                                //pas la premiere connexion de la journée
                                Log.d(TAG, "Temps Passé dans cet session" + Minute);
                                Double TempsDejapasse = TempsConnexion.get("" + currentTime);
                                Log.d(TAG, "onSuccess: TempsPassé Dans les anciennces sessions" + TempsDejapasse);
                                Minute = Minute + TempsDejapasse;
                                Log.d(TAG, "Nouveau temps " + Minute);
                                TempsConnexion.put("" + currentTime, Minute);
                                db.collection("Users").document("user " + mAuth.getCurrentUser().getUid())
                                        .update("tempsConnexion", TempsConnexion);
                            }
                        } else {
                            HashMap<String, Double> TempsConnexionNew = new HashMap<String, Double>();
                            TempsConnexionNew.put("" + currentTime, Minute);
                            db.collection("Users").document("user " + mAuth.getCurrentUser().getUid())
                                    .update("tempsConnexion", TempsConnexionNew);
                        }
                        GotoLogin();
                    }
                });
            }
        else
        {
            GotoLogin();
        }


    }

    private void GotoLogin()
    {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
        int seletedItemId = bottomNavigationView.getSelectedItemId();

        if (R.id.navigation_notifications==seletedItemId) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        parent =true;
        Intent intent1 = new Intent(this, NotificationService.class);
        startService(intent1);
        Intent intent= getIntent();
        Bundle b = intent.getExtras();
        if(b!=null)
        {
            parent=b.getBoolean("Parent");
            nbrEnfant=b.getInt("nombreEnfant");
        }



        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        String user="user "+mAuth.getCurrentUser().getUid();
        Log.d(TAG, "onCreate: user "+user);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        String menuFragment = getIntent().getStringExtra("RecommandationFragment");


        if(!parent && menuFragment==null) {
            //içi Enfant

            mFirebaseAnalytics.setUserId("user "+mAuth.getUid());
            Log.d(TAG, "onCreate: Je rentre dans le Fils");
            navView.getMenu().removeItem(R.id.navigation_liste_enfant);
            navView.getMenu().removeItem(R.id.navigation_Recommandation);

            readDataEnfant(new MyCallback2() {
                @Override
                public void onCallback(Eleve User) {
                    UserEnfant=User;
                }
            });


        }
        else
        {
            //içi Parent
            //frame_container is the id of the container for the fragment
            readDataParent(new MyCallback() {
                @Override
                public void onCallback(Parent User) {
                    UserParent=User;
                }
            });
            Log.d(TAG, "onCreate: Parent "+UserParent);
            navView.getMenu().removeItem(R.id.navigation_home);
            navView.getMenu().removeItem(R.id.navigation_dashboard);
            navView.setSelectedItemId(R.id.navigation_liste_enfant);


        }

        if(menuFragment!=null)
        {
            if (menuFragment.equals("goToRecommandation")) {

                mAuth=FirebaseAuth.getInstance();
                Log.d(TAG, "onCreate: Navigation"+mAuth.getCurrentUser().getEmail());
                parent=true;
                navView.getMenu().removeItem(R.id.navigation_home);
                navView.getMenu().removeItem(R.id.navigation_dashboard);
                navView.setSelectedItemId(R.id.navigation_Recommandation);
            }
        }




    }

    @Override
    public void onBackPressed() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
        int seletedItemId = bottomNavigationView.getSelectedItemId();

        if (R.id.navigation_Recommandation == seletedItemId || (R.id.navigation_notifications==seletedItemId &&parent)) {
            setHomeItem(MainActivity.this);
        }
        else if (R.id.navigation_liste_enfant==seletedItemId) {
            moveTaskToBack(true);
        }
        else {
           // onPause();
            super.onBackPressed();
        }
    }

    public static void setHomeItem(Activity activity) {
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                activity.findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_liste_enfant);
    }


    public Boolean getTypeUser() {
        return parent;
    }

    public interface MyCallback {
        void onCallback(Parent User);

    }

    public interface MyCallback2 {
        void onCallback(Eleve User);
    }

    public void readDataParent(MyCallback myCallback) {
       String id=mAuth.getCurrentUser().getUid();
        DocumentReference docRef = db.collection("Users").document("user "+id);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Parent User = documentSnapshot.toObject(Parent.class);
                myCallback.onCallback(User);
            }
        });
    }

    public void readDataEnfant(MyCallback2 myCallback) {
        String id=mAuth.getCurrentUser().getUid();
        DocumentReference docRef = db.collection("Users").document("user "+id);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Eleve User = documentSnapshot.toObject(Eleve.class);
                myCallback.onCallback(User);
            }
        });
    }

    public Parent getParentUser() {
        Log.d(TAG, "getParentUser: "+UserParent);
        return UserParent;
    }

    public int getNombreEnfant()
    {
        return nbrEnfant;
    }
    public Eleve getEnfantUser()
    {
        return UserEnfant;
    }




}
