package com.unah.hermes.objects;

public class Categoria {
    public String CategoriaID;
    public String Nombre;

    public Categoria(String categoriaID, String nombre) {
        CategoriaID = categoriaID;
        Nombre = nombre;
    }

    public Categoria(Categoria categoria){
        this.CategoriaID=categoria.CategoriaID;
        this.Nombre=categoria.Nombre;
    }
    public Categoria(String categoria){
        this.Nombre=categoria;
    }
    public String getCategoriaID() {
        return CategoriaID;
    }

    public void setCategoriaID(String categoriaID) {
        CategoriaID = categoriaID;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }
}