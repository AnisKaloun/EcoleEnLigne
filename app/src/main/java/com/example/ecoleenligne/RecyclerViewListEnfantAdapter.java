package com.example.ecoleenligne;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tiper.MaterialSpinner;
import java.util.ArrayList;

public class RecyclerViewListEnfantAdapter extends RecyclerView.Adapter<RecyclerViewListEnfantAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<EnfantInfo> mEnfant;
    private Context mContext;
    private int nbrEnfant;

    public RecyclerViewListEnfantAdapter(Context mContext, ArrayList<EnfantInfo> mEnfant, int nbrEnfant) {

        this.mContext = mContext;
        this.mEnfant = mEnfant;
        this.nbrEnfant=nbrEnfant;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_signup_parent_enfant,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        holder.EnfantRang.setText("Enfant "+mEnfant.get(position).getEnfantRang());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext,
                R.array.niveaux_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerniveau.setAdapter(adapter);
        ArrayAdapter<CharSequence> adapterabon = ArrayAdapter.createFromResource(mContext,
                R.array.abonement_array, android.R.layout.simple_spinner_item);
        adapterabon.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerabonn.setAdapter(adapterabon);


    }
    public ArrayList<EnfantInfo> getArrayList() {
        return  mEnfant;
    }
    public EnfantInfo getItem(int position) {
        return mEnfant.get(position);
    }

    @Override
    public int getItemCount() {
        return nbrEnfant;
    }




    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextInputEditText inputNameEnfant, inputPrenomEnfant;
        private TextInputLayout inputLayoutNameEnfant, inputLayoutPrenomEnfant;
        private TextView t,EnfantRang;
        private MaterialSpinner spinnerniveau,spinnerabonn;
        private RadioGroup formuleButton;
        private RadioButton rbformuleprogression, rbformuleoptionaccompagnement;
        private RelativeLayout parentLayout;


        public ViewHolder( View itemView) {
            super(itemView);
            inputLayoutNameEnfant =  itemView.findViewById(R.id.input_layout_nameEnfant);
            inputLayoutPrenomEnfant =  itemView.findViewById(R.id.input_layout_prenomEnfant);

            inputNameEnfant =  itemView.findViewById(R.id.input_NomEnfant);
            inputPrenomEnfant = itemView.findViewById(R.id.input_prenomEnfant);

            spinnerniveau =  itemView.findViewById(R.id.Niveau_spinner);

            t= itemView.findViewById(R.id.txtformuP);
            spinnerabonn = itemView.findViewById(R.id.Abonement_spinner);

            formuleButton= itemView.findViewById(R.id.radioGroup);
            rbformuleoptionaccompagnement=itemView.findViewById(R.id.optionaccompagnement);
            rbformuleprogression=itemView.findViewById(R.id.optionprogression);
            parentLayout=itemView.findViewById(R.id.ParentLayout);
            EnfantRang=itemView.findViewById(R.id.enfantid);


            inputNameEnfant.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {



                        mEnfant.get(getAdapterPosition()).setNom(charSequence.toString());
                    Log.d(TAG, "onTextChanged: nom"+mEnfant.get(getAdapterPosition()).getNom());

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            inputPrenomEnfant.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        mEnfant.get(getAdapterPosition()).setPrenom(charSequence.toString());


                }

                @Override
                public void afterTextChanged(Editable editable) {


                }
            });


            spinnerabonn.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MaterialSpinner materialSpinner, View view, int i, long l) {

                    Toast.makeText(mContext,"item "+materialSpinner.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                    mEnfant.get(getAdapterPosition()).setTypeAbo(materialSpinner.getSelectedItem().toString());

                }

                @Override
                public void onNothingSelected(MaterialSpinner materialSpinner) {

                }
            });


            spinnerniveau.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MaterialSpinner materialSpinner, View view, int i, long l) {

                    mEnfant.get(getAdapterPosition()).setNiveau(materialSpinner.getSelectedItem().toString());

                }

                @Override
                public void onNothingSelected(MaterialSpinner materialSpinner) {


                }
            });

            formuleButton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(checkedId==R.id.optionaccompagnement)
                    {
                        mEnfant.get(getAdapterPosition()).setFormule("accompagnement");
                        Toast.makeText(mContext,"item "+ mEnfant.get(getAdapterPosition()).getFormule(),Toast.LENGTH_SHORT).show();

                    }
                    else if(checkedId==R.id.optionprogression)
                    {
                        mEnfant.get(getAdapterPosition()).setFormule("progression");
                        Toast.makeText(mContext,"item "+ mEnfant.get(getAdapterPosition()).getFormule(),Toast.LENGTH_SHORT).show();


                    }
                }
            });




        }



    }


}
