package com.example.ecoleenligne.ui.Cours;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.ecoleenligne.R;
import com.google.android.exoplayer2.offline.Download;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class CoursMathActivity extends AppCompatActivity {
    private DocumentReference DocumentModules = FirebaseFirestore.getInstance().document("Modules/IdModuleMaths");
    private DocumentReference DocumentCours = FirebaseFirestore.getInstance().document("Modules/IdModuleMaths/Cours/idCoursDivisionsEtProblèmes");
    private DocumentReference DocumentProfs = FirebaseFirestore.getInstance().document("Profs/IdProfMath");
    VideoView videoView;
    CardView cardviwepdf, cardviewquiz, cardViewpdfdownload, cardViewCoursenligne;
    TextView tvNomCour, tvProgramationScenaceEnLigne;
    FirebaseStorage firebaseStorage;
    FirebaseStorage storage;
    StorageReference ref;
    private static final String TAG = "CoursMathActivity";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cours_math);
        MediaController mediaController = new MediaController(this);
        videoView = findViewById(R.id.videoView);
        cardviewquiz= findViewById(R.id.QuizCard);
        cardViewCoursenligne= findViewById(R.id.CoursenligneCard);
        tvNomCour= findViewById(R.id.tvnomCourchoisi);
        tvProgramationScenaceEnLigne= findViewById(R.id.tvprogrameSneanceenligne);
        mAuth= FirebaseAuth.getInstance();
        String id=mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        db.collection("Users").document("user " + id).update("cours", FieldValue.arrayUnion("idCoursDivisionsEtProblèmes")).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "onComplete: " + id);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + id);

            }
        });
//visualiser le cours sous forme de pdf
        cardviewquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentquiz=new Intent(getApplicationContext(),QuizActivity.class);
                startActivity(intentquiz);
               // finish();
            }
        });
        cardviwepdf= findViewById(R.id.pdfCard);
        isStoragePermissionGranted();
        cardviwepdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),VisualiserpdfActivity.class);
                startActivity(intent);
                finish();
            }
        });
// download pdf
        cardViewpdfdownload= findViewById(R.id.pdfDownloadCard);
        cardViewpdfdownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download();
            }
        });
// visualiser la video du cours
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        Uri uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/ecoleenligne-1ca1a.appspot.com/o/La%20division%20euclidienne.mp4?alt=media&token=c6fc8382-a342-4b18-9a6d-fc50b3451b77");
        videoView.setVideoURI(uri);
        videoView.start();


        /// partie Seance en ligne

        DocumentModules.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    Log.w(TAG  , "I am in module"+"----------------------------------------------------------------------------------------------");
                    String NomModule=documentSnapshot.getString("NomModule");
                    Log.w(TAG  , NomModule+"----------------------------------------------------------------------------------------------");
                    if(NomModule.equals("Maths")){
                        DocumentCours.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String NiveauModulesEtudiant= documentSnapshot.getString("NiveauDuCours");
                                String NomCours=documentSnapshot.getString("NomCours");
                                Log.w(TAG  , NiveauModulesEtudiant+ ""+NomCours+" "+" "+NomModule);
                                tvNomCour.setText(NomCours);
                                // Jointure entre le nom et le niveau du Module du eleve actuel et le nom et le niveau du module du prof
                                DocumentProfs.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(documentSnapshot.exists()) {
                                            Log.w(TAG, "I am in prof" + "----------------------------------------------------------------------------------------------");
                                            String NiveauCourProf= documentSnapshot.getString("NiveaudesCours");
                                            String NomModulesProf=documentSnapshot.getString("NomModule");
                                            String urlseanceEnlignestr =documentSnapshot.getString("urlcoursenligne");
                                            String date=documentSnapshot.getString("dateDuCour");
                                            String heure=documentSnapshot.getString("heureDuCour");
                                            String Nom=documentSnapshot.getString("Nom");
                                            String Prenom=documentSnapshot.getString("Prenom");

                                            // jointure entre le prof et l'etudiant courant
                                            if(NiveauModulesEtudiant.equals(NiveauCourProf) && NomModule.equals(NomModulesProf)){
                                                tvNomCour.setText(NomCours);

                                                // dans le cas ou le prof organiser un cour en ligne en ajouter l'url  et la date et l'heure du cour en ligne sur la base de donnes des etudiants concerne
                                                if(urlseanceEnlignestr != null || urlseanceEnlignestr != "" ){
//
                                                    DocumentCours.update("urlcoursenligne",urlseanceEnlignestr);
                                                    tvProgramationScenaceEnLigne.setText("qui est program par "+Nom+" "+Prenom+" le  "+date+" à "+heure);                                                    cardViewCoursenligne.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent imain= new Intent(getApplicationContext(), WebCoursEnLigneActivity.class);
                                                            Bundle bundle = new Bundle();
                                                            imain.putExtra("url", urlseanceEnlignestr);
                                                            imain.putExtras(bundle);
                                                            startActivity(imain);

                                                        }
                                                    });


                                                }
                                            }else {
                                                tvProgramationScenaceEnLigne.setText("aucun prof s'est port");
                                            }


                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG  , "document prof was not found");

                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG  , "document cours was not found");
                            }
                        });
                    }
                }
            }
        });

    }
    public void download(){
        storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        ref= storageRef.child("cours_division.pdf");
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url= uri.toString();
               String x= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                downloadFile(CoursMathActivity.this, "cours_division",".pdf", x , url);
                // DownloadFile("cours_division",url);
                Log.d(TAG, "onSuccess: Fichier trouvé");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Pas trouvé");

            }
        });


    }

    public void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url){

        DownloadManager.Request dmr = new DownloadManager.Request(Uri.parse(url));

        dmr.setTitle(fileName);
        dmr.setDescription("Cours");
        dmr.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        dmr.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        dmr.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(dmr);

    }

    private void DownloadFile(String filename,String url) {


        FirebaseStorage storage = FirebaseStorage.getInstance();
        Log.d(TAG,"le lien est"+url);
        StorageReference storageRef = storage.getReferenceFromUrl(url);


        File rootPath = new File(Environment.getExternalStorageDirectory(),"Cours");
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }

        final File localFile = new File(rootPath,filename+".pdf");

        storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(),"Fichier téléchargé a "+localFile.getPath().toString(), Toast.LENGTH_LONG).show();
                Log.e("firebase ","local file created" +localFile.toString());
                //  updateDb(timestamp,localFile.toString(),position);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebase ","local file not created" +exception.toString());
            }
        });
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v("PERMISSIONS","Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }

}
