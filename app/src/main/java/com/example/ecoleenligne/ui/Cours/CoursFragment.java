package com.example.ecoleenligne.ui.Cours;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.ecoleenligne.Model.Module;
import com.example.ecoleenligne.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class CoursFragment extends Fragment {
    private DocumentReference DocumentModules = FirebaseFirestore.getInstance().collection("Modules").document();
    private DocumentReference DocumentCours = FirebaseFirestore.getInstance().document("Modules/IdModuleMaths/Cours/idCoursDivisionsEtProblèmes");
    private MaterialCardView MathCard;
    private FirebaseFirestore db,db1;
    private String TAG="CoursFragment";

    private CoursViewModel coursViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        db=FirebaseFirestore.getInstance();
        db1=FirebaseFirestore.getInstance();
        coursViewModel =
                ViewModelProviders.of(this).get(CoursViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cours, container, false);
        MathCard = root.findViewById(R.id.MathCard);
        MathCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Modules/IdModuleMaths/Cours")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isComplete()) {
                                    Log.d(TAG, "onComplete: Jesuisla");
                                    ArrayList<String> list = new ArrayList<String>();

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, "onComplete: "+document.get("NomCours"));
                                         list.add((String) document.get("NomCours"));
                                    }

                                    String[] myArray = new String[list.size()];
                                    list.toArray(myArray);
                                    new AlertDialog.Builder(getContext())
                                            .setTitle("Les Cours Disponibles")
                                            .setItems(myArray, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String id=myArray[which];
                                                    db.collection("Modules/IdModuleMaths/Cours").whereEqualTo("NomCours",id).
                                                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if(task.isSuccessful()) {
                                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                                        if (document.getData() != null) {
                                                                            Log.d(TAG, "onComplete: "+document.getData());
                                                                            String NiveauCours = document.getString("NiveauDuCours");
                                                                            Log.d(TAG, "onComplete: NiveauCours"+NiveauCours);
                                                                            String NomCours = document.getString("NomCours");
                                                                            Log.d(TAG, "onComplete: NomCours"+NomCours);
                                                                            String TypeCours = document.getString("TypeDeCours");
                                                                            Log.d(TAG, "onComplete: TypeDeCours"+TypeCours);
                                                                            if (NiveauCours.equals("6ème") && NomCours.equals("Divisions Et Problèmes") && TypeCours.equals("cours normale")) {
                                                                                startActivity(new Intent(getActivity(), CoursMathActivity.class));
                                                                            }
                                                                        }
                                                                    }
                                                            }
                                                        }
                                                    });
                                                }
                                            }).show();
                                }
                                 else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

            }


        });

        return root;
    }
}
