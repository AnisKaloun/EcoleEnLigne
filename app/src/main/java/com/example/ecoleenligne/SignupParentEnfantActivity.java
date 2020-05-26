package com.example.ecoleenligne;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tiper.MaterialSpinner;

public class SignupParentEnfantActivity extends AppCompatActivity {
    private TextInputEditText inputNameEnfant, inputPrenomEnfant;
    private TextInputLayout inputLayoutNameEnfant, inputLayoutPrenomEnfant;
    private Button bsuv;
    private TextView t;
    private MaterialSpinner spinnerniveau,spinnerabonn;
    private RadioGroup formuleButton;
    private RadioButton rbformuleprogression, rbformuleoptionaccompagnement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_parent_enfant);
        inputLayoutNameEnfant =  findViewById(R.id.input_layout_nameEnfant);
        inputLayoutPrenomEnfant =  findViewById(R.id.input_layout_prenomEnfant);

        inputNameEnfant =  findViewById(R.id.input_NomEnfant);
        inputPrenomEnfant = findViewById(R.id.input_prenomEnfant);

        spinnerniveau =  findViewById(R.id.Niveau_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.niveaux_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerniveau.setAdapter(adapter);

        t= findViewById(R.id.txtformuP);
        spinnerabonn = findViewById(R.id.Abonement_spinner);
        ArrayAdapter<CharSequence> adapterabon = ArrayAdapter.createFromResource(this,
                R.array.abonement_array, android.R.layout.simple_spinner_item);
        adapterabon.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerabonn.setAdapter(adapterabon);

        formuleButton= findViewById(R.id.radioGroup);
        rbformuleoptionaccompagnement=findViewById(R.id.optionaccompagnement);
        rbformuleprogression=findViewById(R.id.optionprogression);

        inputNameEnfant.addTextChangedListener(new SignupParentEnfantActivity.MyTextWatcher(inputNameEnfant));
        inputPrenomEnfant.addTextChangedListener(new SignupParentEnfantActivity.MyTextWatcher(inputPrenomEnfant));

        bsuv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"je suis la",Toast.LENGTH_SHORT).show();
                check();
            }
        });
    }

    private void check() {
        if(!validateNameEnfant() || !validateprenomEnfant()  ||!checkNiveau() ||!checkAbonnement() || !checkformule())
        {
            Toast.makeText(getApplicationContext(), "Erreur lors de l'inscription", Toast.LENGTH_SHORT).show();
        }
        else
        {

            Intent intent=new Intent(getApplicationContext(),PayementActivity.class);
            startActivity(intent);
        }

    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateNameEnfant() {
        if (inputNameEnfant.getText().toString().trim().isEmpty()) {
            inputLayoutNameEnfant.setError(getString(R.string.err_msg_nameEnfant));
            requestFocus(inputNameEnfant);
            return false;
        } else {
            inputLayoutNameEnfant.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateprenomEnfant() {
        if (inputPrenomEnfant.getText().toString().trim().isEmpty()) {
            inputLayoutPrenomEnfant.setError(getString(R.string.err_msg_prenomEnfant));
            requestFocus(inputPrenomEnfant);
            return false;
        } else
        {
            inputLayoutPrenomEnfant.setErrorEnabled(false);
        }

        return true;
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
            t.setText("Veuillez choisir mode de formation");
            rbformuleprogression.setError("");
            rbformuleoptionaccompagnement.setError("");
            return false;
        }

    }

    public void hideKeyborad(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
                case R.id.input_NomEnfant:
                    validateNameEnfant();
                    break;
                case R.id.input_prenomEnfant:
                    validateprenomEnfant();
                    break;

            }
        }

    }

    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            Toast.makeText(parent.getContext(),
                    "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

    }
}
