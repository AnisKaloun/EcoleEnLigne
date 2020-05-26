package com.example.ecoleenligne.ui.ListeEnfant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.ecoleenligne.EnfantInfo;

import com.example.ecoleenligne.Model.Eleve;
import com.example.ecoleenligne.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.firestore.SetOptions;

public class DetailEnfantActivity extends AppCompatActivity {

    private String EnfantId=null;
    private String TAG="DetailEnfantActivity";
    private TextView txt1,txt2,txt3,txt4,txt5;
    private FirebaseFirestore db;
    private Eleve eleve;
    private ListView listV;
    private ListView listVmoment;
    private ListView listVcours;
    private BarChart barChart;
    private LinearLayout dateRappelLayout, heureRappelLayout;
    private TextView txtdaterapple, txtheureRappel;
    private FirebaseAuth mAuth;
    private DatePickerDialog.OnDateSetListener mDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_enfant);
        Intent intent= getIntent();
        Bundle b = intent.getExtras();
        db=FirebaseFirestore.getInstance();
        txt1=findViewById(R.id.EmailEnfantTxt);
        txt2=findViewById(R.id.Nom_text);
        txt3=findViewById(R.id.PrenomEnfant);
        txt4=findViewById(R.id.Niveau_text);
        txt5=findViewById(R.id.Abonement_text);
        txtdaterapple= findViewById(R.id.dateRapelle_text);
        mAuth=FirebaseAuth.getInstance();
        String id=mAuth.getInstance().getCurrentUser().getUid();
        DocumentReference Ref = db.collection("Users").document("user "+id);

        barChart= findViewById(R.id.bargraph);
        dateRappelLayout=findViewById(R.id.dateRappel_layout);

        ArrayList<String> itemListnote= new ArrayList<>();
        ArrayAdapter<String> adapternote;

       adapternote=new ArrayAdapter<String>(this,R.layout.list_itemnote,R.id.txtviewnote,itemListnote);
       listV=(ListView)findViewById(R.id.listnotequiz);
       listV.setAdapter(adapternote);



        ArrayList<String> itemListmoment= new ArrayList<>();
        ArrayAdapter<String> adaptermoment;

        adaptermoment=new ArrayAdapter<String>(this,R.layout.list_itemmoment,R.id.txtviewmoment,itemListmoment);
        listVmoment=(ListView)findViewById(R.id.listmoment);
        listVmoment.setAdapter(adaptermoment);


        ArrayList<String> itemListcours= new ArrayList<>();
        ArrayAdapter<String> adaptercours;

        adaptercours=new ArrayAdapter<String>(this,R.layout.ithem_listcours,R.id.txtviewncours,itemListcours);
        listVcours=(ListView)findViewById(R.id.listcours);
        listVcours.setAdapter(adaptercours);

        if(b!=null)
        {
            EnfantId=b.getString("IdEnfant");
            db.collection("Users").document(EnfantId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.getResult()!=null)
                    {
                        DocumentSnapshot documentSnapshot=task.getResult();
                        if(documentSnapshot!=null)
                        {
                            //içi j'ai recuperer l'éleve
                            //tu peux recuper les infos avec les get des methods
                            //Attention faut vérifier si c'est des nulls avant d'afficher pour les moments de connexion etc...
                            eleve=documentSnapshot.toObject(Eleve.class);
                            txt1.setText(eleve.getMailEleve());
                            txt2.setText(eleve.getNomEleve());
                            txt3.setText(eleve.getPrenomEleve());
                            txt4.setText(eleve.getNiveau());
                            txt5.setText(eleve.getModeDeFormation());


                            String date, result, strresult;
                            Double duree;
                            for (String key: eleve.getScoreQuizz().keySet()) {
                              String  chaine= key.toString();
                                chaine= chaine.substring(2);
                                String str= "la note du "+chaine +" : "+eleve.getScoreQuizz().get(key).toString()+"/7";
                                itemListnote.add(str);
                                adapternote.notifyDataSetChanged();

                            }

                            String  chaine="";
                            if(eleve.getMomentConnexion()!=null) {
                                //recuperer le dernier moment de connexion
                                TreeMap<String, Integer> recordingsTreeMap = new TreeMap<>();
                                recordingsTreeMap.putAll(eleve.getMomentConnexion());
                                try {
                                    chaine=recordingsTreeMap.lastKey();
                                    Log.d(TAG, "dernier moment de connexion " + recordingsTreeMap.lastKey());
                                    String moment = "Dernier Moment " + chaine + " ";
                                    itemListmoment.add(moment);
                                    adaptermoment.notifyDataSetChanged();
                                }catch (Exception e){
                                    Log.d(TAG, "onComplete: "+e);
                                }
                            }

                            // recupere les cours visualiser par l'enfant

                            List<String> listCours= new ArrayList<>();
                            for ( String cour:eleve.getCours()) {
                                String str= cour;
                                Log.d(TAG, "onComplete: "+str);
                                itemListcours.add(str);
                                adaptercours.notifyDataSetChanged();
                                listCours.add(cour) ;
                            }

                            ArrayList<String> datedeconnection= new ArrayList<String>();
                            List<Double> duredeconnection = new ArrayList<>();
                            ArrayList<BarEntry> barEntries = new ArrayList<>();

                            TreeMap<String,Double> dureeTreeMap = new TreeMap<>();
                            dureeTreeMap.putAll(eleve.getTempsConnexion());
                            int i=0;

                            for (Map.Entry<String, Double> entry : dureeTreeMap.entrySet()) {
                                Log.d(TAG, "temps connexion "+entry.getKey() + "/" + entry.getValue());
                                barEntries.add(new BarEntry(i, entry.getValue().floatValue()));
                                datedeconnection.add(entry.getKey());
                                duredeconnection.add(entry.getValue());
                                i++;

                            }

                            BarDataSet barDataSet=new BarDataSet(barEntries,"Durée de connexion en minute");
                            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                            Description description=new Description();
                            description.setText("date");
                            barChart.setDescription(description);
                            BarData barData=new BarData(barDataSet);
                            barChart.setData(barData);
                            XAxis XAxis=barChart.getXAxis();
                            XAxis.setValueFormatter(new IndexAxisValueFormatter(datedeconnection));
                            XAxis.setPosition(com.github.mikephil.charting.components.XAxis.XAxisPosition.TOP);
                            XAxis.setDrawGridLines(false);
                            XAxis.setDrawAxisLine(false);
                            XAxis.setGranularity(1f);
                            XAxis.setLabelCount(datedeconnection.size());
                            barChart.animateY(2000);
                            barChart.invalidate();
                            barChart.setTouchEnabled(true);
                            barChart.setDragEnabled(true);
                            barChart.setScaleEnabled(true);


                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: ");
                }
            });

            dateRappelLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(
                            DetailEnfantActivity.this,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            mDateSetListener,
                            year,month,day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                }


            });

            mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month = month + 1;
                    Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "/" + month + "/" + year);

                    String date = day  + "/" + month + "/" + year;

                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(DetailEnfantActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                            txtdaterapple.setText(date+" "+ selectedHour + ":" + selectedMinute);

                            DocumentReference Ref = db.collection("Users").document(EnfantId).collection("ReminderNotification").
                                    document("notification");
                            Map<String, Object> data = new HashMap<>();
                            data.put("heureRappel", selectedHour + ":" + selectedMinute);
                            data.put("dateRappel", date);
                            data.put("ShownNotif",false);
                            Ref
                                    .set(data, SetOptions.merge())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error updating document", e);
                                        }
                                    });

                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();




                }
            };


        }





    }
}
