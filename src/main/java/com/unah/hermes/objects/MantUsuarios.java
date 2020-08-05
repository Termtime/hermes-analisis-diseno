package com.unah.hermes.objects;

public class MantUsuarios{
    String codigo;
    String nombre;
    String nivelAcceso;
    String grupo;
    String area;

    public MantUsuarios(String codigo, String nombre, String nivelAcceso, String grupo, String area){
        this.codigo=codigo;
        this.nombre=nombre;
        this.nivelAcceso=nivelAcceso;
        this.grupo=grupo;
        this.area=area;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
    
}
