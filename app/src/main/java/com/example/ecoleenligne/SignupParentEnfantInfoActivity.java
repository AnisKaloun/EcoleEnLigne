package com.example.ecoleenligne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecoleenligne.Model.Eleve;
import com.example.ecoleenligne.Model.Parent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class SignupParentEnfantInfoActivity extends AppCompatActivity {
    private static final String TAG = "SignupParentEnfantInfo";
    private ArrayList<EnfantInfo> mEnfant=new ArrayList<EnfantInfo>();
    private MaterialButton button;
    private FirebaseAuth auth,auth2;
    private int number=0;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_parent_enfant_info);
        Intent intent= getIntent();
        Parent p=new Parent();
        Bundle b = intent.getExtras();
        button=findViewById(R.id.button);
        auth= FirebaseAuth.getInstance();
        String ParentId=auth.getCurrentUser().getUid();

        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setDatabaseUrl("[Database_url_here]")
                .setApiKey("AIzaSyDb7WZ2qNSvFjZGHqV5PrFKF6X8PYoGCeU")
                .setApplicationId("ecoleenligne-1ca1a").build();

        try { FirebaseApp myApp = FirebaseApp.initializeApp(getApplicationContext(), firebaseOptions, "AnyAppName");
            auth2 = FirebaseAuth.getInstance(myApp);
        } catch (IllegalStateException e){
            auth2 = FirebaseAuth.getInstance(FirebaseApp.getInstance("AnyAppName"));
        }



        if(b!=null)
        {
            String nbrChild = (String) b.get("number_of_child");
            String motDePasse=(String) b.get("password");
            int j=Integer.parseInt(nbrChild);
            for(int i=0;i<j;i++)
            {
                mEnfant.add(new EnfantInfo(i+1));
            }
            RecyclerView recyclerView=findViewById(R.id.recyclerView);
            RecyclerViewListEnfantAdapter adapter=new RecyclerViewListEnfantAdapter(this,mEnfant,j);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<EnfantInfo> List = adapter.getArrayList();
                boolean flag=true;
                for (int i = 0; i < List.size(); i++) {
                    if (List.get(i).getNom()!= null && List.get(i).getNom().isEmpty()) {
                        flag=false;
                    }

                    if (List.get(i).getPrenom()!= null && List.get(i).getPrenom().isEmpty()) {
                        flag=false;
                    }

                    if(List.get(i).getNiveau()== null || (List.get(i).getNiveau()!= null && List.get(i).getNiveau().isEmpty())) {
                        flag=false;
                    }

                    if (List.get(i).getTypeAbo()== null || (List.get(i).getTypeAbo()!= null && List.get(i).getTypeAbo().isEmpty())) {
                        flag=false;
                    }

                    if (List.get(i).getFormule()== null || (List.get(i).getFormule()!= null && List.get(i).getFormule().isEmpty())) {
                        flag=false;
                    }




                }

                if (flag){
                    Toast.makeText(SignupParentEnfantInfoActivity.this, "information inséré avec succés", Toast.LENGTH_SHORT).show();


                    for(int i=0;i<List.size();i++)
                    {
                        String email=List.get(i).getPrenom()+"."+List.get(i).getNom()+"@EcoleDesAnges.com";
                        String StrinputName= List.get(i).getNom();
                        String StrinputPrenom =List.get(i).getPrenom();
                        String StrinputEmail= email;
                        String StrNiveau=List.get(i).getNiveau();
                        String StrAbonnement=List.get(i).getTypeAbo();
                        String StrFormule=List.get(i).getFormule();
                        Log.d(TAG, "onClick: Creation"+email);
                        auth2.createUserWithEmailAndPassword(email, motDePasse)
                                .addOnCompleteListener(SignupParentEnfantInfoActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success
                                            Log.d(TAG, "createUserWithEmail:success");
                                            //on crée un Enfant içi
                                            FirebaseUser user = auth2.getCurrentUser();
                                            String parentId="user "+ParentId;
                                            String Id_User="user "+user.getUid();
                                            Log.d(TAG, "AddEnfant: Enfant "+Id_User);
                                            Log.d(TAG, "AddEnfant: ParentId "+parentId);
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(StrinputName+" "+StrinputPrenom).build();
                                            user.updateProfile(profileUpdates);
                                            Eleve eleve= new Eleve(StrinputName, StrinputPrenom,StrinputEmail,StrNiveau,StrAbonnement,StrFormule );
                                            db.collection("Users").document("user "+user.getUid()).set(eleve).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    db.collection("Users").document(parentId).update("listEnfant", FieldValue.arrayUnion(Id_User))
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    Log.d(TAG, "onComplete: j'ai ajouté listEnfant");
                                                                    auth2.signOut();

                                                                    Intent intent=new Intent(getApplicationContext(),PayementActivity.class);
                                                                    intent.putExtra("Parent",true);
                                                                    intent.putExtra("nombreEnfant",nbrChild);
                                                                    startActivity(intent);
                                                                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w(TAG, "Error adding document", e);
                                                        }
                                                    });

                                                }
                                            });
                                        } else {
                                            // If sign in fails
                                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(SignupParentEnfantInfoActivity.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                });



                    }

                    sendEmail(auth.getCurrentUser().getEmail(),List);


                }else {
                    Toast.makeText(SignupParentEnfantInfoActivity.this, "vous avez oublié des champs veuillez réessayer", Toast.LENGTH_SHORT).show();
                }

            }
        });

        }

    }

    public void hideKeyborad(View view){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    protected void sendEmail(String Destination,ArrayList<EnfantInfo> ListEnfantLogin) {

        String LISTEMail="";
        for (int i=0;i<ListEnfantLogin.size();i++)
        {
            LISTEMail=LISTEMail.concat(ListEnfantLogin.get(i).getPrenom()+"."+ListEnfantLogin.get(i).getNom()+"@EcoleDesAnges.com <br/>");
        }
        String fromEmail = "EcolesDesAnges@gmail.com";
        String fromPassword = "@Anis1996";
        String toEmails =Destination;
        List toEmailList = Arrays.asList(toEmails
                .split("\\s*,\\s*"));
        Log.i("SendMailActivity", "To List: " + toEmailList);
        String emailSubject = "Creation de compte pour vos enfants";
        String emailBody = "<p>Bienvenue sur EcolesDesAnges, Ecole de la réussite!!</p>\n " +
                "<p>Voici les comptes de vos Enfants. <br/>" +LISTEMail+
                "<br/> Leur mot de passe est le même que le votre";
        new SendMailTask(SignupParentEnfantInfoActivity.this).execute(fromEmail,
                fromPassword, toEmailList, emailSubject, emailBody);
    }

}
