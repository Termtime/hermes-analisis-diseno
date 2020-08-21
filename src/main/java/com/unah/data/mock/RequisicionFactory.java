package com.unah.data.mock;
import java.util.ArrayList;
import java.util.Date;

public class RequisicionFactory {

    public static ArrayList<RequisicionMock> load(){
        ArrayList<RequisicionMock> data = new ArrayList<>();
        ArrayList<ProductoMock> prods = new ArrayList<>();
        
        prods.add(new ProductoMock("2", "Mantequilla", "Libra", 1, 1, 0, "Mas azucar"));
        prods.add(new ProductoMock("3", "Pescado", "Libra", 10, 2, 0, "Mucho pescado"));
        prods.add(new ProductoMock("4", "Arroz", "Libra", 5, 4, 1, "Lo pendiente se puede cambiar por asparragos"));

        data.add(new RequisicionMock("1", "Cocina / 28-07-2020 / 11:41:41", "Entregada", "Cocina", "Mario", true, "Edgardo", new Date(), prods));
        data.add(new RequisicionMock("2", "Bar / 28-07-2020 / 11:41:41", "Entregada", "Bar", "Edgardo", true, "Mario", new Date(), prods));
        data.add(new RequisicionMock("3", "Cocina / 28-07-2020 / 11:41:41", "Entregada", "Cocina", "Boris", true, "Edgardo", new Date(), prods));

        return data;
    }
}