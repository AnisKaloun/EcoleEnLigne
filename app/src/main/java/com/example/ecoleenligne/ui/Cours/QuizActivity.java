package com.example.ecoleenligne.ui.Cours;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecoleenligne.Model.Eleve;
import com.example.ecoleenligne.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivitty";
    private DocumentReference DocumentModules = FirebaseFirestore.getInstance().document("Modules/IdModuleMaths");
    private DocumentReference DocumentCours = FirebaseFirestore.getInstance().document("Modules/IdModuleMaths/Cours/idCoursDivisionsEtProblèmes");
    private DocumentReference DocumentQuiz= FirebaseFirestore.getInstance().document("Modules/IdModuleMaths/Cours/idCoursDivisionsEtProblèmes/Quiz/IdQuizDivision");
    private DocumentReference DocumentQuestion1= FirebaseFirestore.getInstance().document("Modules/IdModuleMaths/Cours/idCoursDivisionsEtProblèmes/Quiz/IdQuizDivision/Questions/IdQuestion1");
    private DocumentReference DocumentQuestion2= FirebaseFirestore.getInstance().document("Modules/IdModuleMaths/Cours/idCoursDivisionsEtProblèmes/Quiz/IdQuizDivision/Questions/IdQuestion2");
    private DocumentReference DocumentQuestion3= FirebaseFirestore.getInstance().document("Modules/IdModuleMaths/Cours/idCoursDivisionsEtProblèmes/Quiz/IdQuizDivision/Questions/IdQuestion3");
    private DocumentReference DocumentQuestion4= FirebaseFirestore.getInstance().document("Modules/IdModuleMaths/Cours/idCoursDivisionsEtProblèmes/Quiz/IdQuizDivision/Questions/IdQuestion4");
    private DocumentReference DocumentQuestion5= FirebaseFirestore.getInstance().document("Modules/IdModuleMaths/Cours/idCoursDivisionsEtProblèmes/Quiz/IdQuizDivision/Questions/IdQuestion5");
    private DocumentReference DocumentQuestion6= FirebaseFirestore.getInstance().document("Modules/IdModuleMaths/Cours/idCoursDivisionsEtProblèmes/Quiz/IdQuizDivision/Questions/IdQuestion6");
    private FirebaseAuth mauth;
    private FirebaseFirestore db;
    TextView TvModule,TvCours,TvNiveauQUiz,TvNote,Q1,Q2,Q3,Q4,Q5,Q6,option1Q1,option2Q1,option1Q2,option2Q2,option1Q3,option2Q3,option1Q4,option2Q4,option1Q5,option2Q5,option1Q6,option2Q6, txterreur;
    Button bvalider;
    RadioButton rboption1Q1, rbboption2Q1, rboption1Q2, rboption2Q2, rboption1Q3,rboption2Q3,rboption1Q4,rboption2Q4, rboption1Q5, rboption2Q5, rboption1Q6, rboption2Q6;
    ArrayList<String> ListReponseCoorrecte= new ArrayList<String>();
    ArrayList<String> ListReponseChoisi= new ArrayList<String>();
    int cmp=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mauth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Users").document("user "+mauth.getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Eleve eleve = documentSnapshot.toObject(Eleve.class);
                HashMap<String, Integer> scoreQuizz = eleve.getScoreQuizz();
                if (eleve.getScoreQuizz() != null) {
                    if (scoreQuizz.get("IdQuizDivision") != null) {
                        int score = scoreQuizz.get("IdQuizDivision");
                        Log.d(TAG, "onSuccess: " + score);
                        TvNote.setText("" + score);
                    } else {
                        TvNote.setText("0");
                    }
                }
            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        TvModule=findViewById(R.id.NomModule);
        TvCours=findViewById(R.id.NomCours);
        TvNiveauQUiz=findViewById(R.id.NiveauDuQUiz);
        TvNote=findViewById(R.id.tvNote);
        Q1=findViewById(R.id.question1);
        Q2=findViewById(R.id.question2);
        Q3=findViewById(R.id.question3);
        Q4=findViewById(R.id.question4);
        Q5=findViewById(R.id.question5);
        Q6=findViewById(R.id.question6);
        option1Q1=findViewById(R.id.option1Q1);
        option2Q1= findViewById(R.id.option2Q1);
        option1Q2=findViewById(R.id.option1Q2);
        option2Q2=findViewById(R.id.option2Q2);
        option1Q3=findViewById(R.id.option1Q3);
        option2Q3= findViewById(R.id.option2Q3);
        option1Q4=findViewById(R.id.option1Q4);
        option2Q4=findViewById(R.id.option2Q4);
        option1Q5=findViewById(R.id.option1Q5);
        option2Q5=findViewById(R.id.option2Q5);
        option1Q6=findViewById(R.id.option1Q6);
        option2Q6= findViewById(R.id.option2Q6);
        txterreur= findViewById(R.id.txterreur);
        bvalider= findViewById(R.id.ButtonValider);

        rboption1Q1= findViewById(R.id.radioOption1Q1);
        rbboption2Q1=  findViewById(R.id.radioOpon2Q1);
        rboption1Q2= findViewById(R.id.radioOption1Q2);
        rboption2Q2= findViewById(R.id.radioOption2Q2);
        rboption1Q3= findViewById(R.id.radioOption1Q3);
        rboption2Q3= findViewById(R.id.radioOption2Q3);
        rboption1Q4= findViewById(R.id.radioOption1Q4);
        rboption2Q4= findViewById(R.id.radioOption2Q4);
        rboption1Q5= findViewById(R.id.radioOption1Q5);
        rboption2Q5= findViewById(R.id.radioOption2Q5);
        rboption1Q6= findViewById(R.id.radioOption1Q6);
        rboption2Q6= findViewById(R.id.radioOption2Q6);

        DocumentModules.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Log.w(TAG  , "I am in module"+"----------------------------------------------------------------------------------------------");
                    String NomModule=documentSnapshot.getString("NomModule");
                    TvModule.setText(NomModule);
                    DocumentCours.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){
                                Log.w(TAG  , "I am in Cours"+"----------------------------------------------------------------------------------------------");
                                String NomCours=documentSnapshot.getString("NomCours");
                                TvCours.setText(NomCours);
                                DocumentQuiz.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Log.w(TAG  , "I am in Quiz"+"----------------------------------------------------------------------------------------------");
                                        String niveau = documentSnapshot.getString("niveau");
                                        TvNiveauQUiz.setText(niveau);
                                        DocumentQuestion1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                Log.w(TAG  , "I am in Question 1"+"----------------------------------------------------------------------------------------------");
                                                String strquestion1= documentSnapshot.getString("TextQuestion");
                                                String stroption1Q1= documentSnapshot.getString("Option1");
                                                String stroption2Q1= documentSnapshot.getString("Option2");
                                                String Reponse1=documentSnapshot.getString("Reponse");
                                                Q1.setText(strquestion1);
                                                option1Q1.setText(stroption1Q1);
                                                option2Q1.setText(stroption2Q1);
                                                DocumentQuestion2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        Log.w(TAG  , "I am in Question 2"+"----------------------------------------------------------------------------------------------");
                                                        String strquestion2= documentSnapshot.getString("TextQuestion");
                                                        String stroption1Q2= documentSnapshot.getString("Option1");
                                                        String stroption2Q2= documentSnapshot.getString("Option2");
                                                        String Reponse2=documentSnapshot.getString("Reponse");
                                                        Q2.setText(strquestion2);
                                                        option1Q2.setText(stroption1Q2);
                                                        option2Q2.setText(stroption2Q2);
                                                        DocumentQuestion3.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                Log.w(TAG  , "I am in Question 3"+"----------------------------------------------------------------------------------------------");
                                                                String strquestion3= documentSnapshot.getString("TextQuestion");
                                                                String stroption1Q3= documentSnapshot.getString("Option1");
                                                                String stroption2Q3= documentSnapshot.getString("Option2");
                                                                String Reponse3=documentSnapshot.getString("Reponse");
                                                                Q3.setText(strquestion3);
                                                                option1Q3.setText(stroption1Q3);
                                                                option2Q3.setText(stroption2Q3);

                                                                DocumentQuestion4.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                        Log.w(TAG  , "I am in Question 4"+"----------------------------------------------------------------------------------------------");
                                                                        String strquestion4= documentSnapshot.getString("TextQuestion");
                                                                        String stroption1Q4= documentSnapshot.getString("Option1");
                                                                        String stroption2Q4= documentSnapshot.getString("Option2");
                                                                        String Reponse4=documentSnapshot.getString("Reponse");
                                                                        Q4.setText(strquestion4);
                                                                        option1Q4.setText(stroption1Q4);
                                                                        option2Q4.setText(stroption2Q4);
                                                                        DocumentQuestion5.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                Log.w(TAG  , "I am in Question 5"+"----------------------------------------------------------------------------------------------");
                                                                                String strquestion5= documentSnapshot.getString("TextQuestion");
                                                                                String stroption1Q5= documentSnapshot.getString("Option1");
                                                                                String stroption2Q5= documentSnapshot.getString("Option2");
                                                                                String Reponse5=documentSnapshot.getString("Reponse");
                                                                                Q5.setText(strquestion5);
                                                                                option1Q5.setText(stroption1Q5);
                                                                                option2Q5.setText(stroption2Q5);
                                                                                DocumentQuestion6.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                    @Override
                                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                        Log.w(TAG  , "I am in Question 6"+"----------------------------------------------------------------------------------------------");
                                                                                        String strquestion6= documentSnapshot.getString("TextQuestion");
                                                                                        String stroption1Q6= documentSnapshot.getString("Option1");
                                                                                        String stroption2Q6= documentSnapshot.getString("Option2");
                                                                                        String Reponse6=documentSnapshot.getString("Reponse");
                                                                                        Q6.setText(strquestion6);
                                                                                        option1Q6.setText(stroption1Q6);
                                                                                        option2Q6.setText(stroption2Q6);
                                                                                        bvalider.setOnClickListener(new View.OnClickListener() {
                                                                                            @Override
                                                                                            public void onClick(View v) {

                                                                                                if(!checkQuestion1() || !checkQuestion2()|| !checkQuestion3()|| !checkQuestion4()|| !checkQuestion5()|| !checkQuestion6()){
                                                                                                    Toast.makeText(getApplicationContext(), "Complete votre Exercice", Toast.LENGTH_SHORT).show();
                                                                                                }else {


                                                                                                    String choixEtudiantQuestion1 = OptiOnChoisiPourLaQuestion1();
                                                                                                    String choixEtudiantQuestion2 = OptiOnChoisiPourLaQuestion2();
                                                                                                    String choixEtudiantQuestion3 = OptiOnChoisiPourLaQuestion3();
                                                                                                    String choixEtudiantQuestion4 = OptiOnChoisiPourLaQuestion4();
                                                                                                    String choixEtudiantQuestion5 = OptiOnChoisiPourLaQuestion5();
                                                                                                    String choixEtudiantQuestion6 = OptiOnChoisiPourLaQuestion6();

                                                                                                    String essai= rboption1Q3.getText().toString();

   /* if (choixEtudiantQuestion1.equals(Reponse1)) {
        ListReponseCoorrecte.add(choixEtudiantQuestion1);
    } else if (choixEtudiantQuestion2.equals(Reponse2)) {
        ListReponseCoorrecte.add(choixEtudiantQuestion2);
    } else if (choixEtudiantQuestion3.equals(Reponse3)) {
        ListReponseCoorrecte.add(choixEtudiantQuestion3);
    } else if (choixEtudiantQuestion3.equals(Reponse3)) {
        ListReponseCoorrecte.add(choixEtudiantQuestion3);
    } else if (choixEtudiantQuestion4.equals(Reponse4)) {
        ListReponseCoorrecte.add(choixEtudiantQuestion4);
    } else if (choixEtudiantQuestion5.equals(Reponse5)) {
        ListReponseCoorrecte.add(choixEtudiantQuestion5);
    } else if (choixEtudiantQuestion6.equals(Reponse6)) {
        ListReponseCoorrecte.add(choixEtudiantQuestion6);
    }*/

                                                                                                    ListReponseCoorrecte.add(Reponse1);
                                                                                                    ListReponseCoorrecte.add(Reponse2);
                                                                                                    ListReponseCoorrecte.add(Reponse3);
                                                                                                    ListReponseCoorrecte.add(Reponse4);
                                                                                                    ListReponseCoorrecte.add(Reponse5);
                                                                                                    ListReponseCoorrecte.add(Reponse6);



                                                                                                    if(rboption1Q1.isChecked()==true ||rboption2Q2.isChecked()==true ||rboption1Q3.isChecked()==true ||  rboption1Q4.isChecked()==true || rboption2Q5.isChecked()==true||  rboption1Q6.isChecked()==true  ){

                                                                                                    }


                                                                                                    for (int i =0; i<= ListReponseCoorrecte.size(); i++ ){
                                                                                                        if(rboption1Q1.isChecked()==true)
                                                                                                            cmp++;
                                                                                                        if (rboption2Q2.isChecked()==true)
                                                                                                            cmp++;
                                                                                                        if(rboption1Q3.isChecked()==true)
                                                                                                            cmp++;
                                                                                                        if(rboption1Q4.isChecked()==true)
                                                                                                            cmp++;
                                                                                                        if(rboption2Q5.isChecked()==true)
                                                                                                            cmp++;
                                                                                                        if(rboption1Q6.isChecked()==true)
                                                                                                            cmp++;


                                                                                                    }

                                                                                                    if(rboption1Q1.isChecked()==true){
                                                                                                        ListReponseCoorrecte.add(Reponse1);
                                                                                                    }else if (rboption2Q2.isChecked()==true){
                                                                                                        ListReponseCoorrecte.add(Reponse2);
                                                                                                    }else if(rboption1Q3.isChecked()==true){
                                                                                                        ListReponseCoorrecte.add(Reponse3);
                                                                                                    }else if(rboption1Q4.isChecked()==true){
                                                                                                        ListReponseCoorrecte.add(Reponse4);
                                                                                                    }else if(rboption2Q5.isChecked()==true){
                                                                                                        ListReponseCoorrecte.add(Reponse5);
                                                                                                    }else if(rboption1Q6.isChecked()==true){
                                                                                                        ListReponseCoorrecte.add(Reponse6);
                                                                                                    }
                                                                                                    int resultatNote= cmp/7;
                                                                                                    String aString = Integer.toString(resultatNote);
                                                                                                    TvNote.setText(aString);
                                                                                                    //si le map existe on ajoute la valeur
                                                                                                    //sinon on crée le map
                                                                                                        DocumentReference docRef = db.collection("Users").document("user "+mauth.getCurrentUser().getUid());
                                                                                                        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                                            @Override
                                                                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                                                Eleve eleve = documentSnapshot.toObject(Eleve.class);
                                                                                                                HashMap<String, Integer> scoreQuizz = eleve.getScoreQuizz();
                                                                                                                if(scoreQuizz!=null) {
                                                                                                                    //il existe
                                                                                                                    scoreQuizz.put("IdQuizDivision",resultatNote);
                                                                                                                    db.collection("Users").document("user "+mauth.getCurrentUser().getUid())
                                                                                                                            .update("scoreQuizz",scoreQuizz);
                                                                                                                }
                                                                                                                else
                                                                                                                {
                                                                                                                    HashMap<String,Integer>scoreQuizzNew=new HashMap<String, Integer>();
                                                                                                                    scoreQuizzNew.put("IdQuizDivision",resultatNote);
                                                                                                                    db.collection("Users").document("user "+mauth.getCurrentUser().getUid())
                                                                                                                            .update("scoreQuizz",scoreQuizzNew);
                                                                                                                }
                                                                                                                Toast.makeText(getApplicationContext(),"Votre score est de "+resultatNote+" vous allez etre rediriger vers la page du cours",Toast.LENGTH_LONG).show();
                                                                                                                onBackPressed();
                                                                                                            }
                                                                                                        });



                                                                                                   // DocumentQuiz.update("Note",aString);

                                                                                                    Log.w(TAG  , "Modification de la note sur serveur avec success "+"*******************************************************************************************----------------------------------------------------------------------------------------------");


                                                                                                }
                                                                                            }
                                                                                        });

                                                                                    }
                                                                                });
                                                                            }
                                                                        }) ;

                                                                    }
                                                                });
                                                            }
                                                        });

                                                    }
                                                });
                                            }

                                        }) ;





                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG  , "document Quiz was not found");
                                    }
                                });

                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG  , "document cours was not found");
                        }
                    });
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG  , "document Modules was not found");
            }
        });

    }




    private boolean checkQuestion1(){
        if(rbboption2Q1.isChecked()|| rboption1Q1.isChecked()){
            return true;
        }{
            txterreur.setError(" Voulliez Complte votre exercice");
            txterreur.setText("question 1");
            return false;
        }
    }
    private boolean checkQuestion2(){
        if(rboption1Q2.isChecked()|| rboption2Q2.isChecked()){
            return true;
        }{
            txterreur.setError(" Voulliez Complte votre exercice");
            txterreur.setText("question 2");
            return false;
        }
    }
    private boolean checkQuestion3(){
        if(rboption1Q3.isChecked()|| rboption2Q3.isChecked()){
            return true;
        }{
            txterreur.setError(" Voulliez Complte votre exercice");
            txterreur.setText("question 3");
            return false;
        }
    }
    private boolean checkQuestion4(){
        if(rboption1Q4.isChecked()|| rboption2Q4.isChecked()){
            return true;
        }{
            txterreur.setError(" Voulliez Complte votre exercice");
            txterreur.setText("question 4");
            return false;
        }
    }
    private boolean checkQuestion5(){
        if(rboption1Q5.isChecked()|| rboption2Q5.isChecked()){
            return true;
        }{
            txterreur.setError(" Voulliez Complte votre exercice");
            txterreur.setText("question 5");
            return false;
        }
    }
    private boolean checkQuestion6(){
        if(rboption1Q6.isChecked()|| rboption2Q6.isChecked()){
            return true;
        }{
            txterreur.setError(" Voulliez Complte votre exercice");
            txterreur.setText("question 6");
            return false;
        }
    }

    private String OptiOnChoisiPourLaQuestion1(){
        String Ressult="";
        String Option1Q1 =  rboption1Q1.getText().toString();
        String Option2Q1 =  rbboption2Q1.getText().toString();
        if(checkQuestion1()==true){
            if(rboption1Q1.isChecked()==true){Ressult= Option1Q1;}
            else if (rbboption2Q1.isChecked()==true){Ressult=Option2Q1;}
        }
        return  Ressult;
    }

    private String OptiOnChoisiPourLaQuestion2(){
        String Ressult="";
        String Option1Q2 =  rboption1Q2.getText().toString();
        String Option2Q2 =  rboption2Q2.getText().toString();
        if(checkQuestion2()==true){
            if(rboption1Q2.isChecked()==true){Ressult= Option1Q2;}
            else if (rboption2Q2.isChecked()==true){Ressult=Option2Q2;}
        }
        return  Ressult;
    }


    private String OptiOnChoisiPourLaQuestion3(){
        String Ressult="";
        String Option1Q3 =  rboption1Q3.getText().toString();
        String Option2Q3 =  rboption2Q3.getText().toString();
        if(checkQuestion3()==true){
            if(rboption1Q3.isChecked()==true){Ressult= Option1Q3;}
            else if (rboption2Q3.isChecked()==true){Ressult=Option2Q3;}
        }
        return  Ressult;
    }


    private String OptiOnChoisiPourLaQuestion4(){
        String Ressult="";
        String Option1Q4 =  rboption1Q4.getText().toString();
        String Option2Q4 =  rboption2Q4.getText().toString();
        if(checkQuestion4()==true){
            if(rboption1Q4.isChecked()==true){Ressult= Option1Q4;}
            else if (rboption2Q4.isChecked()==true){Ressult=Option2Q4;}
        }
        return  Ressult;
    }

    private String OptiOnChoisiPourLaQuestion5(){
        String Ressult="";
        String Option1Q5 =  rboption1Q5.getText().toString();
        String Option2Q5 =  rboption2Q5.getText().toString();
        if(checkQuestion5()==true){
            if(rboption1Q5.isChecked()==true){Ressult= Option1Q5;}
            else if (rboption2Q5.isChecked()==true){Ressult=Option2Q5;}
        }
        return  Ressult;
    }
    private String OptiOnChoisiPourLaQuestion6(){
        String Ressult="";
        String Option1Q6 =  rboption1Q6.getText().toString();
        String Option2Q6 =  rboption1Q6.getText().toString();
        if(checkQuestion6()==true){
            if(rboption1Q6.isChecked()== true){Ressult= Option1Q6;}
            else if (rboption1Q6.isChecked()== true){Ressult=Option2Q6;}
        }
        return  Ressult;
    }
}
