package com.unah.data.mock;

import com.unah.hermes.objects.Producto;

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

    public ProductoMock(Producto prod){
        this.productoID = prod.productoID != null && !prod.productoID.isEmpty()? prod.productoID : "";
        this.nombre = prod.nombre != null && !prod.nombre.isEmpty()? prod.nombre : "";
        this.unidad = prod.unidad != null && !prod.unidad.isEmpty()? prod.unidad : "";
        this.cantEntregada = prod.cantEntregada;
        this.cantPedida = prod.cantPedida;
        this.cantPendiente = prod.cantPendiente;
        this.comentario = prod.comentario != null && !prod.comentario.isEmpty()? prod.comentario : "";
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