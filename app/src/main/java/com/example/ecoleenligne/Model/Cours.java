package com.example.ecoleenligne.Model;

import android.graphics.pdf.PdfDocument;
import android.widget.VideoView;

public class Cours {
      private String IdCours, NomCours, Presentation,TypeDeCours,NiveauDuCours;
      private VideoView CoursVideo;
      private PdfDocument courspdf;


      public Cours(String IdCours,String NomCours, String Presnetation, String TypeDeCours, String NiveauDuCours, VideoView CoursVideo, PdfDocument courspdf ){
          this.IdCours=IdCours;
          this.NomCours=NomCours;
          this.Presentation=Presnetation;
          this.TypeDeCours=TypeDeCours;
          this.NiveauDuCours=NiveauDuCours;
          this.CoursVideo=CoursVideo;
          this.courspdf=courspdf;
      }
    public Cours(String IdCours,String NomCours, String Presnetation, String TypeDeCours, String NiveauDuCours ){
        this.IdCours=IdCours;
        this.NomCours=NomCours;
        this.Presentation=Presnetation;
        this.TypeDeCours=TypeDeCours;
        this.NiveauDuCours=NiveauDuCours;
    }
    public Cours(String NomCours, String Presnetation, String TypeDeCours, String NiveauDuCours ){
        this.NomCours=NomCours;
        this.Presentation=Presnetation;
        this.TypeDeCours=TypeDeCours;
        this.NiveauDuCours=NiveauDuCours;
    }
    public Cours(){}

    public String getIdCours() {
        return IdCours;
    }

    public String getNomCours() {
        return NomCours;
    }

    public String getPresentation() {
        return Presentation;
    }

    public String getTypeDeCours() {
        return TypeDeCours;
    }

    public String getNiveauDuCours() {
        return NiveauDuCours;
    }

    public VideoView getCoursVideo() {
        return CoursVideo;
    }

    public PdfDocument getCourspdf() {
        return courspdf;
    }

    public void setIdCours(String idCours) {
        IdCours = idCours;
    }

    public void setNomCours(String nomCours) {
        NomCours = nomCours;
    }

    public void setPresentation(String presentation) {
        Presentation = presentation;
    }

    public void setTypeDeCours(String typeDeCours) {
        TypeDeCours = typeDeCours;
    }

    public void setNiveauDuCours(String niveauDuCours) {
        NiveauDuCours = niveauDuCours;
    }

    public void setCoursVideo(VideoView coursVideo) {
        CoursVideo = coursVideo;
    }

    public void setCourspdf(PdfDocument courspdf) {
        this.courspdf = courspdf;
    }
}

