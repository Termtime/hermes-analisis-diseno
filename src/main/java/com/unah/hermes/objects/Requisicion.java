package com.unah.hermes.objects;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Requisicion {
    public String reqID;
    public String estado;
    public String area;
    public String autorizador;
    public Boolean estaAutorizado;
    public String nombreDisplay;
    public String solicitante;
    public ObservableList<Producto> productos = FXCollections.observableArrayList();
    public Date fecha;
    public String fechaString;
    public String hora;
    
    
    public Requisicion(String reqID, String nombreDisplay, String estado, String area, String autorizador, Boolean estaAutorizado, String solicitante, Date fecha, Object prods)
    {
        try{
            List<Map<String,Object>> productosLista = (List<Map<String,Object>>) prods;
            this.reqID = reqID;
            this.nombreDisplay = nombreDisplay;
            this.estado = estado;
            this.area = area;
            this.autorizador = autorizador;
            this.estaAutorizado = estaAutorizado;
            this.solicitante = solicitante;
            this.fecha = fecha;
            this.fechaString = fecha.toString().substring(0,(fecha.toString().indexOf(":")-2));
            this.hora = fecha.toString().substring((fecha.toString().indexOf(":")-2));
            for (Map<String,Object> producto : productosLista) {
                //crear un nuevo producto
                Producto tmp = new Producto(
                producto.get("productoID").toString(),
                producto.get("producto").toString(),
                producto.get("unidad").toString(),
                producto.containsKey("cantidadEntregada")? Integer.parseInt(producto.get("cantidadEntregada").toString()): 0,
                producto.containsKey("cantidadPedida")? Integer.parseInt(producto.get("cantidadPedida").toString()): 0,
                producto.containsKey("cantidadPendiente")? Integer.parseInt(producto.get("cantidadPendiente").toString()) : 0,
                producto.containsKey("comentario")? producto.get("comentario").toString() : null
                );
                //agregar el producto al arreglo de productos
                productos.add(tmp);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        return nombreDisplay;
    }
}