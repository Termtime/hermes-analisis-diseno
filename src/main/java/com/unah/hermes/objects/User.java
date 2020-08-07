package com.unah.hermes.objects;

public class User{
    public String userID;
    public String nombre;
    public String nivelAcceso;
    public String grupo;
    public String area;

    public User(String userID, String nombre, String nivelAcceso, String grupo, String area)
    {
        this.userID = userID;
        this.nombre = nombre;
        this.nivelAcceso = nivelAcceso;
        this.grupo = grupo;
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


    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
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