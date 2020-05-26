package com.example.ecoleenligne;

import java.util.ArrayList;

public class EnfantInfo {
    private String Nom;
    private String Prenom;
    private String Niveau;
    private String TypeAbo;
    private int EnfantRang;
    private String Formule;


    public EnfantInfo()
    {

    }

    public EnfantInfo(String nom, String prenom, String niveau, String typeAbo) {
        Nom = nom;
        Prenom = prenom;
        Niveau = niveau;
        TypeAbo = typeAbo;
    }

    public EnfantInfo(int i) {
     this.EnfantRang=i;
    }


    public String getNom() {
        return Nom;
    }

    public String getPrenom() {
        return Prenom;
    }

    public String getNiveau() {
        return Niveau;
    }

    public String getTypeAbo() {
        return TypeAbo;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public void setPrenom(String prenom) {
        Prenom = prenom;
    }

    public void setNiveau(String niveau) {
        Niveau = niveau;
    }

    public void setTypeAbo(String typeAbo) {
        TypeAbo = typeAbo;
    }

    public int getEnfantRang() {
        return EnfantRang;
    }

    public void setEnfantRang(int enfantRang) {
        EnfantRang = enfantRang;
    }

    public String getFormule() {
        return Formule;
    }

    public void setFormule(String formule) {
        Formule = formule;
    }




}
