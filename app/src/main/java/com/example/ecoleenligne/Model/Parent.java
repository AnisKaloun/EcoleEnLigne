package com.example.ecoleenligne.Model;

import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.List;

public class Parent {

    private static final String TAG ="ParentClass";
    private String nom, prenom, mail,phoneNumber,facebookPage,twitterPage;
    private int nombreEnfant;
    private ImageView image;
    private List<String> listEnfant=new ArrayList<String>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Parent( String nom,String prenom, String mail, int nombreEnfant) {
        super();
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.nombreEnfant=nombreEnfant;
    }

    public Parent( String nom,String prenom){
        super();
        this.nom = nom;
        this.prenom = prenom;
    }
    public Parent( String nom,String prenom, String mail){
        super();
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
    }

    public Parent( String nom,String prenom, String mail, int nombreEnfant, ImageView image){
        super();
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.nombreEnfant=nombreEnfant;
        this.image=image;
    }

    public Parent() {
        super();
    }


    public String getNom() {
        return nom;
    }
    public void setNom (String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }
    public void setPrenom (String prenom) {
        this.prenom = prenom;
    }

    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {

        this.mail= mail;
    }
    public void setMotdepasse(String motdepasse) {

        this.mail= motdepasse;
    }
    public int getNombreEnfant() {
        return nombreEnfant;
    }
    public void setNombreEnfant(int nombreEnfant) {

        this.nombreEnfant= nombreEnfant;
    }
    public ImageView getImage() {
        return image;
    }
    public void setImage(ImageView image){
        this.image=image;
    }

    public List<String> getListEnfant() {
        return listEnfant;
    }

    public void setListEnfant(List<String> ListEnfant) {
        this.listEnfant = ListEnfant;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFacebookPage() {
        return facebookPage;
    }

    public void setFacebookPage(String facebookPage) {
        this.facebookPage = facebookPage;
    }

    public String getTwitterPage() {
        return twitterPage;
    }

    public void setTwitterPage(String twitterPage) {
        this.twitterPage = twitterPage;
    }

    public void AddParent(Parent parent, String Id_User)
    {
        Log.d(TAG, "AddParent: "+Id_User);
        this.listEnfant=new ArrayList<>();
         db.collection("Users").document("user "+Id_User).set(parent);
    }

}

