package com.unah.hermes.objects;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;

public class User{
    public String userID;
    public String nombre;
    public String nivelAcceso;
    public String area;
    //public ObservableList<User> Usuarios = FXCollections.observableArrayList();

    public User(String userID, String nombre, String nivelAcceso, String area)
    {
        this.userID = userID;
        this.nombre = nombre;
        this.nivelAcceso = nivelAcceso;
        this.area = area;
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


        public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getNivelAcceso() {
        return nivelAcceso;
    }

    public void setNivelAcceso(String nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }

    
}