package com.unah.data.mock;

import java.util.ArrayList;
import java.util.List;


public class ProductoFactory {
    
    public static List<ProductoMock> load(){
        List<ProductoMock> prods = new ArrayList<>();
        prods.add(new ProductoMock("2", "Mantequilla", "Libra", 1, 4, 0, "Mas azucar"));
        prods.add(new ProductoMock("3", "Pescado", "Libra", 10, 5, 0, "Mucho pescado"));
        prods.add(new ProductoMock("4", "Arroz", "Libra", 5, 3, 0, "se puede cambiar por asparragos"));
        
        return prods;
    }
}