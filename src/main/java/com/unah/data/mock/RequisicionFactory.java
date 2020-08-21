package com.unah.data.mock;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class RequisicionFactory {

    public static List<RequisicionMock> load(){
        ArrayList<RequisicionMock> data = new ArrayList<>();
        ArrayList<ProductoMock> prods = new ArrayList<>();
        ArrayList<ProductoMock> prods2 = new ArrayList<>();
        ArrayList<ProductoMock> prods3 = new ArrayList<>();
        
        prods.add(new ProductoMock("2", "Mantequilla", "Libra", 1, 1, 0, "Mas azucar"));
        prods.add(new ProductoMock("3", "Pescado", "Libra", 10, 2, 0, "Mucho pescado"));
        prods.add(new ProductoMock("4", "Arroz", "Libra", 5, 4, 1, "se puede cambiar por asparragos"));

        prods2.add(new ProductoMock("4", "Arroz", "Libra", 5, 4, 1, "se puede cambiar por asparragos"));
        prods2.add(new ProductoMock("2", "Mantequilla", "Libra", 1, 1, 0, "Mas azucar"));
        prods2.add(new ProductoMock("3", "Pescado", "Libra", 10, 2, 0, "Mucho pescado"));

        prods3.add(new ProductoMock("2", "Mantequilla", "Libra", 1, 1, 0, "Mas azucar"));
        prods3.add(new ProductoMock("3", "Pescado", "Libra", 10, 2, 0, "Mucho pescado"));

        data.add(new RequisicionMock("1", "Cocina / 25-07-2020 / 11:41:41", "Entregada", "Cocina", "Mario", true, "Edgardo", new Date(2020,7,25), prods));
        data.add(new RequisicionMock("2", "Bar / 29-07-2020 / 11:41:41", "Entregada", "Bar", "Edgardo", true, "Mario", new Date(2020,7,28), prods2));
        data.add(new RequisicionMock("3", "Cocina / 30-07-2020 / 11:41:41", "Entregada", "Cocina", "Boris", true, "Edgardo", new Date(2020,7,29), prods3));

        return data;
    }
}