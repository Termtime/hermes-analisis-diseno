package com.unah.data.mock;


public class ProductoMock {
    public String productoID = null;
    public String nombre = null;
    public String unidad = null;
    public int cantEntregada = 0;
    public int cantPendiente = 0;
    public int cantPedida = 0;
    public String comentario = null;

    //constructor para las tablas de requisiciones
    public ProductoMock(String productoID, String nombre, String unidad, int cantEntregada, int cantPedida, int cantPendiente, String comentario)
    {
        this.productoID = productoID;
        this.nombre = nombre;
        this.unidad = unidad;
        this.cantEntregada = cantEntregada;
        this.cantPedida = cantPedida;
        this.cantPendiente = cantPendiente;
        this.comentario = comentario;
    }

    public String getProductoID() {
        return productoID;
    }

    public void setProductoID(String productoID) {
        this.productoID = productoID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public int getCantEntregada() {
        return cantEntregada;
    }

    public void setCantEntregada(int cantEntregada) {
        this.cantEntregada = cantEntregada;
    }

    public int getCantPendiente() {
        return cantPendiente;
    }

    public void setCantPendiente(int cantPendiente) {
        this.cantPendiente = cantPendiente;
    }

    public int getCantPedida() {
        return cantPedida;
    }

    public void setCantPedida(int cantPedida) {
        this.cantPedida = cantPedida;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    
}