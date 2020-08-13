package com.unah.hermes.objects;

public class Area {
    public String areaID;
    public String nombre;

    public Area(String areaID, String nombre) {
        this.areaID = areaID;
        this.nombre = nombre;
    }

    public Area(String nombre){
        this.nombre=nombre;
    }
    public String getNombre() {
        return nombre;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAreaID() {
        return areaID;
    }

    public void setAreaID(String areaID) {
        this.areaID = areaID;
    }

    @Override
    public String toString(){
        return nombre;
    }
}