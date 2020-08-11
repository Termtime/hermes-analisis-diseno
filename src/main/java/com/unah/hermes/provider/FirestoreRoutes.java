package com.unah.hermes.provider;

public class FirestoreRoutes{

    public static final String REQUISICIONES = "Requisiciones";
    public static final String USUARIOS = "Usuarios";
    public static final String AREAS = "Areas";
    public static final String PRODUCTOS = "Productos";
    public static final String CATEGORIAS = "Categorias";

    public static final String categoriaPath(String categoriaID){
        return CATEGORIAS + "/" + categoriaID;
    }
    public static final String userPath(String userID){
        return USUARIOS + "/" + userID;
    }

    public static final String productPath(String productID){
        return PRODUCTOS + "/" + productID;
    }

    public static final String areaPath(String areaID){
        return AREAS + "/" + areaID;
    }

    public static final String reqPath(String reqID){
        return REQUISICIONES + "/" + reqID;
    }

    public static final String reqProductsPath(String reqID){
        return PRODUCTOS + "/" + reqID + "/Productos";
    }

    private FirestoreRoutes(){}
}