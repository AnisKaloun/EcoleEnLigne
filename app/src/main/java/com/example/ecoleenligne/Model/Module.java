package com.example.ecoleenligne.Model;

public class Module {

  private   String IdModule, NomModule;
  public Module(){}
    public Module(String IdModule, String NomModule){
      this.IdModule=IdModule;
      this.NomModule=NomModule;
    }
    public String getIdModule(){
      return IdModule;
    }
    public String GetNomModule(){
      return  NomModule;
    }
    public void setIdModule(String IdModule){
      this.IdModule=IdModule;
    }

    public void setNomModule(String nomModule){
        this.NomModule=nomModule;
    }
}
