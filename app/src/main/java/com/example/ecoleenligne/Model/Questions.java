package com.example.ecoleenligne.Model;

public class Questions {
    String IdQuestion, TextQuestion, Option1, Option2, Reponse;

    public Questions(String idQuestion, String textQuestion) {
        IdQuestion = idQuestion;
        TextQuestion = textQuestion;
    }

    public Questions(String idQuestion, String textQuestion, String option1, String option2, String reponse) {
        IdQuestion = idQuestion;
        TextQuestion = textQuestion;
        Option1 = option1;
        Option2 = option2;
        Reponse = reponse;
    }

    public Questions(String idQuestion, String textQuestion, String option1, String option2) {
        IdQuestion = idQuestion;
        TextQuestion = textQuestion;
        Option1 = option1;
        Option2 = option2;
    }

    public Questions(String idQuestion, String textQuestion, String reponse) {
        IdQuestion = idQuestion;
        TextQuestion = textQuestion;
        Reponse = reponse;
    }

    public String getIdQuestion() {
        return IdQuestion;
    }

    public void setIdQuestion(String idQuestion) {
        IdQuestion = idQuestion;
    }

    public String getTextQuestion() {
        return TextQuestion;
    }

    public void setTextQuestion(String textQuestion) {
        TextQuestion = textQuestion;
    }

    public String getOption1() {
        return Option1;
    }

    public void setOption1(String option1) {
        Option1 = option1;
    }

    public String getOption2() {
        return Option2;
    }

    public void setOption2(String option2) {
        Option2 = option2;
    }

    public String getReponse() {
        return Reponse;
    }

    public void setReponse(String reponse) {
        Reponse = reponse;
    }
}
