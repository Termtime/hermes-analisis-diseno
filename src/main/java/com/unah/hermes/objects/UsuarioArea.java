package com.unah.hermes.objects;

import java.util.List;

public class UsuarioArea {
    public String usuarioID;
    public String nombre;
    public List<String> area;
    public String stringDeArea;

    public UsuarioArea(String usuarioID, String nombre, List<String> area) {
        this.usuarioID = usuarioID;
        this.nombre = nombre;
        this.area = area;
        System.out.println(area);
        for (String areas : area) {
            stringDeArea += area + ", ";
        }
        stringDeArea = stringDeArea.substring(0, stringDeArea.length() - 2);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuarioID() {
        return usuarioID;
    }

    public void setAreaID(String usuarioID) {
        this.usuarioID = usuarioID;
    }

    public String getstringDeArea() {
        return stringDeArea;
    }

    public void setstringDeArea(String stringDeArea) {
        this.stringDeArea = stringDeArea;
    }

}