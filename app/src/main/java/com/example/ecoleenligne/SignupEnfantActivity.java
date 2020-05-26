package com.example.ecoleenligne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecoleenligne.Model.Eleve;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tiper.MaterialSpinner;

public class SignupEnfantActivity extends AppCompatActivity {

    private static final String TAG ="SignupEnfantActivity";
    private EditText inputName,inputPrenom, inputEmail, inputmotdepassef,inputconfirmermotdepasse;
    private TextInputLayout inputLayoutName,inputLayoutPrenom, inputLayoutEmail, inputLayoutmotdepasse, inputLayoutconfirmermotdepasse;
    private Button bsuv;
    private MaterialTextView txtfor;

    MaterialSpinner spinnerniveau,spinnerabonn;
    private RadioButton rbformuleprogression, rbformuleoptionaccompagnement;
    private FirebaseAuth auth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_enfant);
        auth= FirebaseAuth.getInstance();
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPrenom= (TextInputLayout) findViewById(R.id.input_layout_prenom);
        inputLayoutmotdepasse = (TextInputLayout) findViewById(R.id.input_layout_motdepasse);
        inputLayoutconfirmermotdepasse=(TextInputLayout) findViewById(R.id.input_layout_confirmemotdepassef);
        inputName =  findViewById(R.id.input_name);
        inputEmail = findViewById(R.id.input_email);
        inputPrenom=  findViewById(R.id.input_prenom);
        inputmotdepassef=  findViewById(R.id.input_motdepassef);
        inputconfirmermotdepasse= findViewById(R.id.input_confirmemorpasse);
        bsuv = (Button) findViewById(R.id.textButton);
        inputName.addTextChangedListener(new SignupEnfantActivity.MyTextWatcher(inputName));
        inputEmail.addTextChangedListener(new SignupEnfantActivity.MyTextWatcher(inputEmail));
        inputPrenom.addTextChangedListener(new SignupEnfantActivity.MyTextWatcher(inputName));
        inputmotdepassef.addTextChangedListener(new SignupEnfantActivity.MyTextWatcher(inputmotdepassef));
        inputconfirmermotdepasse.addTextChangedListener(new SignupEnfantActivity.MyTextWatcher(inputconfirmermotdepasse));


        rbformuleoptionaccompagnement=findViewById(R.id.optionaccompagnement);
        rbformuleprogression=findViewById(R.id.optionprogression);

        txtfor=findViewById(R.id.txtformu);
        spinnerniveau =  findViewById(R.id.Niveau_spinner_enfant);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.niveaux_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerniveau.setAdapter(adapter);


        spinnerabonn = findViewById(R.id.Abonement_spinner);
        ArrayAdapter<CharSequence> adapterabon = ArrayAdapter.createFromResource(this,
                R.array.abonement_array, android.R.layout.simple_spinner_item);
        adapterabon.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerabonn.setAdapter(adapterabon);

        bsuv .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        });

    }
    private void check() {
        if (!validateName() || !validateEmail() ||  !validateName() || !validatemotdepasset() || !checkformule() ||
                !validatePrenom() || ! validateconfirmemotdepasse() || !checkmotdepasse() ||!checkNiveau() ||!checkAbonnement()) {
            Toast.makeText(getApplicationContext(), "Erreur lors de l'inscription", Toast.LENGTH_SHORT).show();
        }
        else
        {
            String email=inputEmail.getText().toString().trim();
            String motDePasse=inputconfirmermotdepasse.getText().toString().trim();
            Log.d(TAG, "check: 1 "+email);
            Log.d(TAG, "check: 2 "+motDePasse);
            auth.createUserWithEmailAndPassword(email, motDePasse)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = auth.getCurrentUser();


                                String StrinputName=  inputName.getText().toString();
                                String StrinputPrenom = inputPrenom.getText().toString();

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(StrinputName+" "+StrinputPrenom).build();
                                user.updateProfile(profileUpdates);

                                String StrinputEmail= inputEmail.getText().toString();
                                String Strinputmotdepassef= inputmotdepassef.getText().toString();
                                String StrNiveau= spinnerniveau.getSelectedItem().toString();
                                String StrAbonnement=spinnerabonn.getSelectedItem().toString();
                                String StrFormule=RetourneFormuleChoisi();
                                Eleve eleve= new Eleve(StrinputName, StrinputPrenom,StrinputEmail,Strinputmotdepassef,StrNiveau,StrAbonnement,StrFormule );
                                db.collection("Users").document("user "+user.getUid()).set(eleve).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "Document has been saved", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(getApplicationContext(),PayementActivity.class);
                                        intent.putExtra("Parent",false);
                                        startActivity(intent);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Document was not saved", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            } else {
                                // If sign in fails
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignupEnfantActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }

                        }
                    });


        }

    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePrenom() {
        if (inputPrenom.getText().toString().trim().isEmpty()) {
            inputLayoutPrenom.setError(getString(R.string.err_msg_prenom));
            requestFocus(inputPrenom);
            return false;
        } else {
            inputLayoutPrenom.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validatemotdepasset() {
        if (inputmotdepassef.getText().toString().trim().isEmpty()) {
            inputLayoutmotdepasse.setError(getString(R.string.err_msg_emptyPassword));
            requestFocus(inputmotdepassef);
            return false;
        } else {
            inputLayoutmotdepasse.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateconfirmemotdepasse() {
        if (inputconfirmermotdepasse.getText().toString().trim().isEmpty()) {
            inputLayoutconfirmermotdepasse.setError(getString(R.string.err_msg_emptyPassword));
            requestFocus(inputLayoutconfirmermotdepasse);
            return false;
        } else {
            inputLayoutconfirmermotdepasse.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean checkmotdepasse(){
        String modepasse=inputmotdepassef.getText().toString();
        String confirmer = inputconfirmermotdepasse.getText().toString();
        if(modepasse.equals(confirmer)){
            inputLayoutmotdepasse.setErrorEnabled(false);
            return true;
        }else {
            inputLayoutmotdepasse.setError(getString(R.string.err_msg_confirmermotdepasse));
            requestFocus(inputconfirmermotdepasse);

            return false;
        }

    }

    private boolean checkNiveau(){

        if (spinnerniveau.getSelectedItem()==null)
        {
            spinnerniveau.setError(getString(R.string.err_msg_niveau));
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean checkAbonnement()
    {
        if (spinnerabonn.getSelectedItem()==null)
        {
            spinnerabonn.setError(getString(R.string.err_msg_abo));
            return false;
        }
        else
        {
            return true;
        }

    }
    private boolean checkformule(){
        if(rbformuleprogression.isChecked()|| rbformuleoptionaccompagnement.isChecked()){

            return true;
        }{
            txtfor.setText(" veuille saisir le mode de formation");
            rbformuleprogression.setError("");
            rbformuleoptionaccompagnement.setError("");
            return false;
        }

    }

    private String RetourneFormuleChoisi(){

        String Ressult="";
        if(checkformule()==true){
            if(rbformuleprogression.isChecked()){Ressult= "formule progression";}
            else if (rbformuleoptionaccompagnement.isChecked()){Ressult="formule option accompagnement";}
        }
        return  Ressult;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public void hideKeyborad(View view){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }



    public class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_prenom:
                    validatePrenom();
                    break;
                case R.id.input_confirmemorpasse :
                    validateconfirmemotdepasse();
                    break;
                case R.id.input_motdepassef:
                    validatemotdepasset();

                    break;

            }
        }


    }

}
