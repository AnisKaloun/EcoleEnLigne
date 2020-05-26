package com.example.ecoleenligne.Model;

public class Quiz {
    private String Decription, NombreDeQuestion,Note,idQuiz,niveau;

    public Quiz(String decription, String nombreDeQuestion, String note, String idQuiz, String niveau) {
        Decription = decription;
        NombreDeQuestion = nombreDeQuestion;
        Note = note;
        this.idQuiz = idQuiz;
        this.niveau = niveau;
    }

    public Quiz(String decription, String idQuiz) {
        Decription = decription;
        this.idQuiz = idQuiz;
    }

    public Quiz(String decription, String note, String idQuiz) {
        Decription = decription;
        Note = note;
        this.idQuiz = idQuiz;
    }

    public Quiz() {

    }

    public void setDecription(String decription) {
        Decription = decription;
    }

    public void setNombreDeQuestion(String nombreDeQuestion) {
        NombreDeQuestion = nombreDeQuestion;
    }

    public void setNote(String note) {
        Note = note;
    }

    public void setIdQuiz(String idQuiz) {
        this.idQuiz = idQuiz;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public String getDecription() {
        return Decription;
    }

    public String getNombreDeQuestion() {
        return NombreDeQuestion;
    }

    public String getNote() {
        return Note;
    }

    public String getIdQuiz() {
        return idQuiz;
    }

    public String getNiveau() {
        return niveau;
    }
}
