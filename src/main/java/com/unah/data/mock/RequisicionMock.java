package com.unah.data.mock;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    public JRBeanCollectionDataSource productos;
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
            this.productos = new JRBeanCollectionDataSource(prods);
        }catch(Exception e){
            e.printStackTrace();
        }
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

    public JRBeanCollectionDataSource getProductos() {
        return productos;
    }

    public void setProductos(JRBeanCollectionDataSource productos) {
        this.productos = productos;
    }

}