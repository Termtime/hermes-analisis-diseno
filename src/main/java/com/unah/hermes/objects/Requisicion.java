package com.unah.hermes.objects;

import java.util.List;

public class Requisicion {
    public String reqID;
    public String estado;
    public String area;
    public String autorizador;
    public Boolean estaAutorizado;
    public String nombreDisplay;
    public String solicitante;
    public List<Producto> productos;
    
    public Requisicion(String reqID, String nombreDisplay, String estado, String area, String autorizador, Boolean estaAutorizado, String solicitante)
    {
        this.reqID = reqID;
        this.nombreDisplay = nombreDisplay;
        this.estado = estado;
        this.area = area;
        this.autorizador = autorizador;
        this.estaAutorizado = estaAutorizado;
        this.solicitante = solicitante;
    }
}