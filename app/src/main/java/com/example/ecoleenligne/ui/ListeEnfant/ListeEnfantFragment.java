package com.example.ecoleenligne.ui.ListeEnfant;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.ecoleenligne.MainActivity;
import com.example.ecoleenligne.Model.Eleve;
import com.example.ecoleenligne.Model.Parent;
import com.example.ecoleenligne.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ListeEnfantFragment extends Fragment {

    private ListeEnfantViewModel listeEnfantViewModel;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private MaterialCardView Card1,Card2,Card3,Card4,Card5;
    private MaterialTextView txt1,txt2,txt3,txt4,txt5;
    private String idEnfant1,idEnfant2,idEnfant3,idEnfant4,idEnfant5;
    private String TAG="ListEnfantFragment";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MainActivity activity = (MainActivity) getActivity();
        Boolean b=activity.getTypeUser();
        mAuth=FirebaseAuth.getInstance();
        String id=FirebaseAuth.getInstance().getUid();
        db=FirebaseFirestore.getInstance();

        listeEnfantViewModel =ViewModelProviders.of(this).get(ListeEnfantViewModel.class);
        View root = inflater.inflate(R.layout.fragment_listeenfant, container, false);
        Card1=root.findViewById(R.id.StudentCard1);
        Card1.setVisibility(View.GONE);
        Card2=root.findViewById(R.id.StudentCard2);
        Card2.setVisibility(View.GONE);
        Card3=root.findViewById(R.id.StudentCard3);
        Card3.setVisibility(View.GONE);
        Card4=root.findViewById(R.id.StudentCard4);
        Card4.setVisibility(View.GONE);
        Card5=root.findViewById(R.id.StudentCard5);
        Card5.setVisibility(View.GONE);
        txt1=root.findViewById(R.id.TextEnfant1);
        txt2=root.findViewById(R.id.TextEnfant2);
        txt3=root.findViewById(R.id.TextEnfant3);
        txt4=root.findViewById(R.id.TextEnfant4);
        txt5=root.findViewById(R.id.TextEnfant5);


        if(b)
        {
            int j=activity.getNombreEnfant();
            Log.d(TAG, "onCreateView: je Suis un Parent "+j);
                ShowListEnfant(j);
            Log.d(TAG, "je suis "+id);
                db.collection("Users").document("user "+id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult()!=null)
                        {
                            DocumentSnapshot documentSnapshot=task.getResult();
                            if(documentSnapshot.getData()!=null)
                            {
                                Parent parent=documentSnapshot.toObject(Parent.class);
                                List<String>ListEnfant=parent.getListEnfant();
                                for (int i=0;i<ListEnfant.size();i++)
                                {

                                    Log.d(TAG, "id Enfant"+ListEnfant.get(i));
                                    db.collection("Users").document(ListEnfant.get(i)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if(documentSnapshot.getData()!=null) {
                                                Eleve eleve = documentSnapshot.toObject(Eleve.class);
                                                Log.d(TAG, "onSuccess: "+eleve.getPrenomEleve());
                                                WritePrenom(eleve.getPrenomEleve(),documentSnapshot.getId());
                                            }
                                        }
                                    });
                                }

                                Card1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                                Intent intent=new Intent(getContext(),DetailEnfantActivity.class);
                                                Log.d(TAG, "onClick: "+idEnfant1);
                                                intent.putExtra("IdEnfant",idEnfant1);
                                                startActivity(intent);
                                    }
                                });
                                Card2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent=new Intent(getContext(),DetailEnfantActivity.class);
                                        intent.putExtra("IdEnfant",idEnfant2);
                                        startActivity(intent);

                                    }
                                });

                                Card3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent=new Intent(getContext(),DetailEnfantActivity.class);
                                        intent.putExtra("IdEnfant",idEnfant3);
                                        startActivity(intent);
                                    }
                                });

                                Card4.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent=new Intent(getContext(),DetailEnfantActivity.class);
                                        intent.putExtra("IdEnfant",idEnfant4);
                                        startActivity(intent);
                                    }
                                });

                                Card5.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        Intent intent=new Intent(getContext(),DetailEnfantActivity.class);
                                        intent.putExtra("IdEnfant",idEnfant5);
                                        startActivity(intent);

                                    }
                                });
                            }

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Failure getting data");
                    }
                });

        }


        return root;
    }



    private void WritePrenom(String prenomEleve,String EnfantId) {
        if(txt1.getText().toString().isEmpty())
        {
            Card1.setVisibility(View.VISIBLE);
            txt1.setText(prenomEleve);
            idEnfant1=EnfantId;

        }
        else if(txt2.getText().toString().isEmpty())
        {
            Card2.setVisibility(View.VISIBLE);
            txt2.setText(prenomEleve);
            idEnfant2=EnfantId;
        }
        else if (txt3.getText().toString().isEmpty())
        {
            Card3.setVisibility(View.VISIBLE);
            txt3.setText(prenomEleve);
            idEnfant3=EnfantId;
        }
        else if(txt4.getText().toString().isEmpty())
        {
            Card4.setVisibility(View.VISIBLE);
            txt4.setText(prenomEleve);
            idEnfant4=EnfantId;
        }
        else if(txt5.getText().toString().isEmpty())
        {
            Card5.setVisibility(View.VISIBLE);
            txt5.setText(prenomEleve);
            idEnfant5=EnfantId;

        }

    }

    private void ShowListEnfant(int j ){

        if(j==1)
        {
            Card5.setVisibility(View.GONE);
            Card4.setVisibility(View.GONE);
            Card3.setVisibility(View.GONE);
            Card2.setVisibility(View.GONE);

        }
        else if(j==2)
        {
            Card5.setVisibility(View.GONE);
            Card4.setVisibility(View.GONE);
            Card3.setVisibility(View.GONE);
        }
        else if(j==3)
        {
            Card5.setVisibility(View.GONE);
            Card4.setVisibility(View.GONE);
        }
        else if(j==4)
        {
            Card5.setVisibility(View.GONE);

        }
    }

/*
    @Override
    public void onResume() {
        super.onResume();

        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    // handle back button's click listener

                    System.exit(0);
                    return true;
                }
                return false;
            }
        });
    }
*/


}
