package com.unah.hermes.objects;


public class Producto {
   public String productoID;
   public String nombre;
   public String unidad;
   
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
   }
}