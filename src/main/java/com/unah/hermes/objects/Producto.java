package com.unah.hermes.objects;


public class Producto {
    public String productoID = null;
    public String nombre = null;
    public String unidad = null;
    public int cantEntregada = 0;
    public int cantPendiente = 0;
    public int cantPedida = 0;
    public String comentario = null;
   
    //constructor para el mantenimiento de productos
    public Producto(String productoID, String nombre, String unidad)
    {
        this.productoID = productoID;
        this.nombre = nombre;
        this.unidad = unidad;
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
}