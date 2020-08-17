package com.unah.hermes.objects;
import java.util.List;

public class User {
    public String userID;
    public String nombre;
    public String nivelAcceso;
    public String stringDeArea;
    public List<String> areas;
    public String uid;
    public Boolean desactivado;
    //agregue lista de String areasNombre
    public List<String> areasNombre;
    //public ObservableList<User> Usuarios = FXCollections.observableArrayList();

    public User(String userID, String nombre, String nivelAcceso, String uid, List<String> areasNombre,List<String> areas, Boolean desactivado)
    {
        this.userID = userID;
        this.nombre = nombre;
        this.nivelAcceso = nivelAcceso;
        this.areas = areas;
        this.areasNombre = areasNombre;
        this.uid = uid;
        this.desactivado = desactivado;
        stringDeArea = "";
        for(String area : areasNombre){
            stringDeArea += area + ", ";
        }
        if(!areasNombre.isEmpty()) stringDeArea = stringDeArea.substring(0,stringDeArea.length()-2);
    }
    public User(String userID, String nombre, String nivelAcceso,List<String> areas)
    {
        this.userID = userID;
        this.nombre = nombre;
        this.nivelAcceso = nivelAcceso;
        this.areas = areas;
        this.stringDeArea = "";
        for(String area : areas){
            stringDeArea += area + ", ";
        }
        if(!areas.isEmpty()) this.stringDeArea = stringDeArea.substring(0,stringDeArea.length()-2);
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    } 
    
   

    public String getNivelAcceso() {
        return nivelAcceso;
    }

    public void setNivelAcceso(String nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }

    public String getStringDeArea() {
        
         
        return stringDeArea;
    }

    public void setStringDeArea(String stringDeArea) {
        this.stringDeArea = stringDeArea;
    }

    
}