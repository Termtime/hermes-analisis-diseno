package com.unah.hermes.objects;

public class ProductoEntregado extends Producto {
    public int cantEntregada;
    public int cantPendiente;
    public int cantPedida;

    public ProductoEntregado(Producto producto, int cantEntregada, int cantPedida, int cantPendiente)
    {
        super(producto);
        this.cantEntregada = cantEntregada;
        this.cantPedida = cantPedida;
        this.cantPendiente = cantPendiente;
    }
}