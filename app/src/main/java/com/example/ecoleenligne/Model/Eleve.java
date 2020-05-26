package com.example.ecoleenligne.Model;

import android.widget.ImageView;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Eleve {
    private String nomEleve, prenomEleve, mailEleve, motdepasseEleve, niveau, typeAbonnement,modeDeFormation,phoneNumber,facebookPage,twitterPage;
    private ImageView imageEleve;
    private List<String> cours=new ArrayList<String>();
    private HashMap<String,Integer> scoreQuizz=new HashMap<String,Integer>();
    private HashMap<String,Double> tempsConnexion=new HashMap<String,Double>();
    private HashMap<String,Integer> momentConnexion=new HashMap<String, Integer>();


    public Eleve( String nomEleve,String prenomEleve, String mailEleve,String motdepasseEleve, String Niveau, String TypeAbonnemnt,String ModeDeFormation) {
        super();
        this.nomEleve = nomEleve;
        this.prenomEleve = prenomEleve;
        this.mailEleve = mailEleve;
        this.motdepasseEleve=motdepasseEleve;
        this.niveau =Niveau;
        this.typeAbonnement=TypeAbonnemnt;
        this.modeDeFormation=ModeDeFormation;
    }
    public Eleve(String mailEleve,String motdepasseEleve){
        this.mailEleve = mailEleve;
        this.motdepasseEleve=motdepasseEleve;
    }

    public Eleve( String nomEleve,String prenomEleve, String mailEleve, String Niveau, String TypeAbonnemnt,String ModeDeFormation) {
        super();
        this.nomEleve = nomEleve;
        this.prenomEleve = prenomEleve;
        this.mailEleve = mailEleve;
        this.niveau =Niveau;
        this.typeAbonnement=TypeAbonnemnt;
        this.modeDeFormation=ModeDeFormation;
    }


    public Eleve( String nomEleve,String prenomEleve, String mailEleve,String motdepasseEleve, String Niveau, String TypeAbonnemnt,String ModeDeFormation, ImageView ImageEnfant) {
        super();
        this.nomEleve = nomEleve;
        this.prenomEleve = prenomEleve;
        this.mailEleve = mailEleve;
        this.motdepasseEleve=motdepasseEleve;
        this.niveau =Niveau;
        this.typeAbonnement=TypeAbonnemnt;
        this.modeDeFormation=ModeDeFormation;
        this.imageEleve=ImageEnfant;
    }

    public Eleve( String nomEleve,String prenomEleve, String mailEleve,String motdepasseEleve) {
        super();
        this.nomEleve = nomEleve;
        this.prenomEleve = prenomEleve;
        this.mailEleve = mailEleve;
        this.motdepasseEleve=motdepasseEleve;
    }
    public  Eleve(){}
    public Eleve( String nomEleve,String prenomEleve, String mailEleve) {
        super();
        this.nomEleve = nomEleve;
        this.prenomEleve = prenomEleve;
        this.mailEleve = mailEleve;
    }
    public Eleve( String nomEleve,String prenomEleve, String mailEleve, ImageView imageEnfant) {
        super();
        this.nomEleve = nomEleve;
        this.prenomEleve = prenomEleve;
        this.mailEleve = mailEleve;
        this.imageEleve= imageEnfant;
    }

    public String getNomEleve() {
        return nomEleve;
    }

    public String getPrenomEleve() {
        return prenomEleve;
    }

    public String getMailEleve() {
        return mailEleve;
    }


    public String getNiveau() {
        return niveau;
    }

    public String getTypeAbonnement() {
        return typeAbonnement;
    }

    public String getModeDeFormation() {
        return modeDeFormation;
    }

    public ImageView getImageEleve() {
        return imageEleve;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String numeroDeTel) {
        this.phoneNumber = numeroDeTel;
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

    public void setTwitterPage(String twiterPage) {
        this.twitterPage = twiterPage;
    }

    public List<String> getCours() {
        return cours;
    }

    public void setCours(List<String> cours) {
        this.cours = cours;
    }

    public HashMap<String, Integer> getScoreQuizz() {
        return scoreQuizz;
    }

    public void setScoreQuizz(HashMap<String, Integer> scoreQuizz) {
        this.scoreQuizz = scoreQuizz;
    }

    public HashMap<String, Double> getTempsConnexion() {
        return tempsConnexion;
    }

    public void setTempsConnexion(HashMap<String, Double> tempsConnexion) {
        this.tempsConnexion = tempsConnexion;
    }

    public HashMap<String, Integer> getMomentConnexion() {
        return momentConnexion;
    }

    public void setMomentConnexion(HashMap<String, Integer> momentConnexion) {
        this.momentConnexion = momentConnexion;
    }

}
