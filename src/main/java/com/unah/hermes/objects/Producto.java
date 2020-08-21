package com.unah.hermes.objects;


public class Producto {
    public String productoID = null;
    public String nombre = null;
    public String unidad = null;
    public int cantEntregada = 0;
    public int cantPendiente = 0;
    public int cantPedida = 0;
    public String comentario = null;
    public String categoria = null;
   
    //constructor para el mantenimiento de productos
    public Producto(String productoID, String nombre, String unidad, String categoria)
    {
        this.productoID = productoID;
        this.nombre = nombre;
        this.unidad = unidad;
        this.categoria = categoria;
    }

    public Producto(Producto producto)
    {
        this.productoID = producto.productoID;
        this.nombre = producto.nombre;
        this.unidad = producto.unidad;
        try{
            this.cantEntregada = producto.cantEntregada;
            this.cantPedida = producto.cantPedida;
            this.cantPendiente = producto.cantPendiente;
        }catch(Exception e){}
    }
    
    //constructor para el reporte
    public Producto(String nombre, int cantEntregada){
        this.nombre = nombre;
        this.cantEntregada = cantEntregada;
    }
    //constructor para las tablas de requisiciones
    public Producto(String productoID, String nombre, String unidad, int cantEntregada, int cantPedida, int cantPendiente, String comentario)
    {
        this.productoID = productoID;
        this.nombre = nombre;
        this.unidad = unidad;
        this.cantEntregada = cantEntregada;
        this.cantPedida = cantPedida;
        this.cantPendiente = cantPendiente;
        this.comentario = comentario;
    }

    public int getCantEntregada() {
        return cantEntregada;
    }

    public int getCantPedida() {
        return cantPedida;
    }
    
    public int getCantPendiente() {
        return cantPendiente;
    }

    public String getComentario() {
        return comentario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getProductoID() {
        return productoID;
    }

    public String getUnidad() {
        return unidad;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public void setCantEntregada(int cantEntregada) {
        this.cantEntregada = cantEntregada;
    }

    public void setCantPedida(int cantPedida) {
        this.cantPedida = cantPedida;
    }

    public void setCantPendiente(int cantPendiente) {
        this.cantPendiente = cantPendiente;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setProductoID(String productoID) {
        this.productoID = productoID;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

}