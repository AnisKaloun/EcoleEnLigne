package com.example.ecoleenligne.ui.Cours;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecoleenligne.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class VisualiserpdfActivity extends AppCompatActivity {
    private PDFView pdfView;
    private TextView text1;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private static final String TAG = "VisualiserpdfActivity";

    private DocumentReference DocumentCours = FirebaseFirestore.getInstance().document("Modules/IdModuleMaths/Cours/idCoursDivisionsEtProblèmes");


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //on doit ajouter dans la BDD que l'éleve a lue le cours
        mAuth = FirebaseAuth.getInstance();
        String id = mAuth.getInstance().getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        isStoragePermissionGranted();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualiserpdf);
        pdfView = findViewById(R.id.pdfview);
        text1 = findViewById(R.id.text1);




        // new RetirePdfStrem().execute("http://www.clg-lurcat-sarcelles.ac-versailles.fr/IMG/pdf/6_cours_division");
        DocumentCours.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.w(TAG, "I am in Cours" + "----------------------------------------------------------------------------------------------");
                String url = documentSnapshot.getString("CoursPdf");
                //Toast.makeText(VisualiserpdfActivity.this, url, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onSuccess: Pas de connexion" + url);
                new RetirePdfStrem().execute(url);
                // new RetirePdfStrem().execute("http://www.clg-lurcat-sarcelles.ac-versailles.fr/IMG/pdf/6_cours_division");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "document cours was not found");
            }
        });
    }


    class RetirePdfStrem extends AsyncTask<String, Void, InputStream> {

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (IOException e) {
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "cours_division");

                //Uri path = Uri.fromFile(file);
                /*
                String filename = "cours_division";
                File rootPath = new File(Environment.getExternalStorageDirectory(Environment.DIRECTORY_DOWNLOADS));
                File localFile = new File(rootPath,filename+".pdf");

                */
                if(file.exists()) {
                    try {
                        InputStream fileInputStream = new FileInputStream(file);
                        Log.d(TAG, "doInBackground: j'ai trouvé le fichier en local");
                        return fileInputStream;
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
                else
                {
                    Log.d(TAG, "doInBackground: Fichier not found");
                }

                return null;
            }

            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream).load();
        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERMISSIONS", "Permission is granted");
                return true;
            } else {

                Log.v("PERMISSIONS","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("PERMISSIONS","Permission is granted");
            return true;
        }


    }
}
