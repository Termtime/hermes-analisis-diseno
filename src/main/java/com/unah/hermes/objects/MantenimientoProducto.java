package com.unah.hermes.objects;

public class MantenimientoProducto {
    public String producto = null;
    public String categoria = null;
    public String unidad = null;

    public MantenimientoProducto(String producto, String categoria, String unidad) {
        this.producto = producto;
        this.categoria = categoria;
        this.unidad = unidad;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    
}
