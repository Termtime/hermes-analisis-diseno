package com.unah.data.mock;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RequisicionMock {
    public String reqID;
    public String estado;
    public String area;
    public String autorizador;
    public Boolean estaAutorizado;
    public String nombreDisplay;
    public String solicitante;
    public ArrayList<ProductoMock> productos = new ArrayList<>();
    public Date fecha;
    public String fechaString;
    public String hora;
    
    
    public RequisicionMock(String reqID, String nombreDisplay, String estado, String area, String autorizador, Boolean estaAutorizado, String solicitante, Date fecha, Object prods)
    {
        try{
            List<Map<String,Object>> productosLista = (List<Map<String,Object>>) prods;
            System.out.println("ESTOS VIENE ORIGINAL:");
            System.out.println(prods);
            System.out.println("Esto lo coniverto:");
            System.out.println(productosLista);
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
                System.out.println("hay producto");
                ProductoMock tmp = new ProductoMock(
                producto.get("productoID").toString(),
                producto.get("producto").toString(),
                producto.get("unidad").toString(),
                producto.containsKey("cantEntregada")? Integer.parseInt(producto.get("cantEntregada").toString()): 0,
                producto.containsKey("cantPedida")? Integer.parseInt(producto.get("cantPedida").toString()): 0,
                producto.containsKey("cantPendiente")? Integer.parseInt(producto.get("cantPendiente").toString()) : 0,
                producto.containsKey("comentario")? producto.get("comentario").toString() : null
                );
                //agregar el producto al arreglo de productos
                productos.add(tmp);
                System.out.println("Agregado el producto");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public RequisicionMock(String reqID, String nombreDisplay, String estado, String area, String autorizador, Boolean estaAutorizado, String solicitante, Date fecha, ArrayList<ProductoMock> prods)
    {
        try{
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
            this.productos = prods;
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public String toString(){
        return nombreDisplay;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setAutorizador(String autorizador) {
        this.autorizador = autorizador;
    }

    public void setEstaAutorizado(Boolean estaAutorizado) {
        this.estaAutorizado = estaAutorizado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setFechaString(String fechaString) {
        this.fechaString = fechaString;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setNombreDisplay(String nombreDisplay) {
        this.nombreDisplay = nombreDisplay;
    }

    public void setProductos(ArrayList<ProductoMock> productos) {
        this.productos = productos;
    }

    public void setReqID(String reqID) {
        this.reqID = reqID;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public String getArea() {
        return area;
    }

    public String getAutorizador() {
        return autorizador;
    }
    public Boolean getEstaAutorizado() {
        return estaAutorizado;
    }
    public String getEstado() {
        return estado;
    }
    public Date getFecha() {
        return fecha;
    }
    public String getFechaString() {
        return fechaString;
    }
    public String getHora() {
        return hora;
    }
    public String getNombreDisplay() {
        return nombreDisplay;
    }
    public ArrayList<ProductoMock> getProductos() {
        return productos;
    }
    public String getReqID() {
        return reqID;
    }
    public String getSolicitante() {
        return solicitante;
    }
}