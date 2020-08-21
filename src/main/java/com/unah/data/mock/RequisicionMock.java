package com.unah.data.mock;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.unah.hermes.objects.Producto;
import com.unah.hermes.objects.Requisicion;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class RequisicionMock {
    public String reqID;
    public String estado;
    public String area;
    public String autorizador;
    public Boolean estaAutorizado;
    public String nombreDisplay;
    public String solicitante;
    public List<ProductoMock> productos;
    // public JRBeanCollectionDataSource productos;
    public Date fecha;
    public String fechaString;
    public String hora;
    
    
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

    public RequisicionMock(Requisicion req){
        this.reqID = req.reqID != null && !req.reqID.isEmpty()? req.reqID : "";
        // this.reqID = req.reqID != null && !req.reqID.isEmpty()? req.reqID : "";
        this.nombreDisplay = req.nombreDisplay != null && !req.nombreDisplay.isEmpty()? req.nombreDisplay : "";
        this.estado = req.estado != null && !req.estado.isEmpty()? req.estado : "";
        this.area = req.area != null && !req.area.isEmpty()? req.area : "";
        this.autorizador = req.autorizador != null && !req.autorizador.isEmpty()? req.autorizador : "";
        this.estaAutorizado = req.estaAutorizado != null? req.estaAutorizado : false;
        this.solicitante = req.solicitante != null && !req.solicitante.isEmpty()? req.solicitante : "";
        //TODO revisar esto
        this.fecha = req.fecha != null? req.fecha : new Date();
        this.fechaString = req.fechaString != null && !req.fechaString.isEmpty()? req.fechaString : "";
        this.hora = req.hora != null && !req.hora.isEmpty()? req.hora : "";
        List<ProductoMock> productosConvertidos = new ArrayList();
        for(Producto prod : req.productos){
            ProductoMock tmp = new ProductoMock(prod);
            productosConvertidos.add(tmp);
            System.out.println("producto convertido");
        }
        this.productos = productosConvertidos;
        // this.productos = new JRBeanCollectionDataSource(productosConvertidos);
    }
    @Override
    public String toString(){
        return nombreDisplay;
    }

    public String getReqID() {
        return reqID;
    }

    public void setReqID(String reqID) {
        this.reqID = reqID;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAutorizador() {
        return autorizador;
    }

    public void setAutorizador(String autorizador) {
        this.autorizador = autorizador;
    }

    public Boolean getEstaAutorizado() {
        return estaAutorizado;
    }

    public void setEstaAutorizado(Boolean estaAutorizado) {
        this.estaAutorizado = estaAutorizado;
    }

    public String getNombreDisplay() {
        return nombreDisplay;
    }

    public void setNombreDisplay(String nombreDisplay) {
        this.nombreDisplay = nombreDisplay;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }


    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getFechaString() {
        return fechaString;
    }

    public void setFechaString(String fechaString) {
        this.fechaString = fechaString;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public List<ProductoMock> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoMock> productos) {
        this.productos = productos;
    }

    // public JRBeanCollectionDataSource getProductos() {
    //     return productos;
    // }

    // public void setProductos(JRBeanCollectionDataSource productos) {
    //     this.productos = productos;
    // }
    

}