package com.unah.hermes.objects;

public class Categoria {
    public String categoriaID;
    public String nombre;

    public Categoria(String categoriaID, String nombre) {
        this.categoriaID = categoriaID;
        this.nombre = nombre;
    }

    public Categoria(Categoria categoria){
        this.categoriaID=categoria.categoriaID;
        this.nombre=categoria.nombre;
    }
    public Categoria(String categoria){
        this.nombre=categoria;
    }
    public String getCategoriaID() {
        return categoriaID;
    }

    public void setCategoriaID(String categoriaID) {
        this.categoriaID = categoriaID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString(){
        return nombre;
    }
}