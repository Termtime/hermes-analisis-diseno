package com.unah.hermes.objects;

public class RequisicionRow {
    String producto;
    String unidad;
    int cantidadPedida;

    public RequisicionRow(String producto, String unidad, int cantidadPedida){
        this.producto = producto;
        this.unidad = unidad;
        this.cantidadPedida = cantidadPedida;
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
}