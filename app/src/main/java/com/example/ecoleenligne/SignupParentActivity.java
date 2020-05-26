package com.example.ecoleenligne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.text.TextUtils;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.example.ecoleenligne.Model.Parent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tiper.MaterialSpinner;

import android.text.TextWatcher;
import android.widget.Toast;

public class SignupParentActivity extends AppCompatActivity {
    private static final String TAG ="SignupParentActivity" ;
    private TextInputEditText inputName,inputPrenom, inputEmail, inputmotdepassef,inputconfirmermotdepasse;
    private TextInputLayout inputLayoutName,inputLayoutPrenom, inputLayoutEmail, inputLayoutmotdepasse, inputLayoutconfirmermotdepasse;
    private Button bsuv;
    private MaterialSpinner spinnerEnfant;
    private FirebaseAuth auth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_parent);
        auth=FirebaseAuth.getInstance();
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

        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputPrenom.addTextChangedListener(new MyTextWatcher(inputName));
        inputmotdepassef.addTextChangedListener(new MyTextWatcher(inputmotdepassef));
        inputconfirmermotdepasse.addTextChangedListener(new MyTextWatcher(inputconfirmermotdepasse));

        spinnerEnfant=findViewById(R.id.nbrEnfant_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.nombre_enfant_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEnfant.setAdapter(adapter);

        bsuv .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        });

    }
    private void check() {
        if (!validateName() || !validateEmail() ||  !validateName() || !validatemotdepasset() ||
                !validatePrenom() || ! validateconfirmemotdepasse() || !checkmotdepasse() ||!checkNombreEnfant()) {
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
                                FirebaseUser user=auth.getCurrentUser();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(inputName.getText().toString()+" "+inputPrenom.getText().toString()).build();
                                user.updateProfile(profileUpdates);
                                Toast.makeText(getApplicationContext(), "Thank You! number of child "+spinnerEnfant.getSelectedItem(), Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(getApplicationContext(),SignupParentEnfantInfoActivity.class);
                                intent.putExtra("number_of_child",spinnerEnfant.getSelectedItem().toString());
                                intent.putExtra("password",motDePasse);
                                Parent parent=new Parent(inputName.getText().toString(),inputPrenom.getText().toString(),
                                        inputEmail.getText().toString(),Integer.parseInt(spinnerEnfant.getSelectedItem().toString()));
                                parent.AddParent(parent,auth.getCurrentUser().getUid());
                                startActivity(intent);
                                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                            } else {
                                // If sign in fails
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignupParentActivity.this, "Authentication failed.",
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
            inputLayoutconfirmermotdepasse.setErrorEnabled(false);
            return true;
        }else {
            inputLayoutconfirmermotdepasse.setError(getString(R.string.err_msg_confirmermotdepasse));
            requestFocus(inputconfirmermotdepasse);

            return false;
        }


    }

    private boolean checkNombreEnfant()
    {
        if (spinnerEnfant.getSelectedItem()==null)
        {
            spinnerEnfant.setError(getString(R.string.err_msg_nbr_enfant));
            return false;
        }
        else
        {
            return true;
        }

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
