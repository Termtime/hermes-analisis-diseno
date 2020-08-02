package com.unah.hermes.objects;

public class RequisicionEntregadaRow {
    String producto;
    String unidad;
    int cantidadPedida;
    int cantidadEntregada;
    int cantidadPendiente;
    String comentario;

    public RequisicionEntregadaRow(String producto, String unidad, int cantidadPedida, int cantidadEntregada, int cantidadPendiente, String comentario){
        this.producto = producto;
        this.unidad = unidad;
        this.cantidadPedida = cantidadPedida;
        this.cantidadEntregada = cantidadEntregada;
        this.cantidadPedida = cantidadPedida;
        this.cantidadPendiente = cantidadPendiente;
        this.comentario = comentario;
    }

    public String getProducto() {
        return producto;
    }
    
    public String getUnidad() {
        return unidad;
    }

    public int getCantidadPedida() {
        return cantidadPedida;
    }

    public int getCantidadEntregada() {
        return cantidadEntregada;
    }

    public int getCantidadPendiente() {
        return cantidadPendiente;
    }

    public String getComentario() {
        return comentario;
    }
}