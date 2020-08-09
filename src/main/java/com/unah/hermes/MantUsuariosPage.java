package com.unah.hermes;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Function;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.ListenerRegistration;
import com.google.cloud.firestore.ListenerRegistration;
import com.unah.hermes.provider.FirebaseConnector;
import com.unah.hermes.provider.FirestoreRoutes;
import com.unah.hermes.utils.EventListeners;
import com.unah.hermes.utils.Navigation;

import io.opencensus.metrics.export.Summary.Snapshot;

import com.google.cloud.firestore.ListenerRegistration;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FirestoreException;
import com.unah.hermes.objects.User;
import com.unah.hermes.utils.Navigation;
import com.unah.hermes.utils.EventListeners;
import com.google.cloud.firestore.EventListener;
import com.unah.hermes.objects.Area;
import com.unah.hermes.objects.Requisicion;
import com.unah.hermes.provider.FirebaseConnector;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.stage.Modality;


import javafx.fxml.Initializable;

public class MantUsuariosPage implements Initializable {

    @FXML
    TableView<User> tablaUsuarios;

    @FXML
    private void btnAgregarUsuarioClick(ActionEvent event) {
        Navigation.pushRoute("MantUsuariosModalAgregarUsuario", event, false, true);
    }

    @FXML
    private void btnModificarUsuarioClick(ActionEvent event) {
        if(tablaUSelectedItem != null)
            Navigation.pushRouteWithParameter("MantUsuarioPage", event, false, true, MantUsuariosModalModificarUsuario.class, tablaUSelectedItem );
        else{
            Alert alert = new Alert(AlertType.ERROR,"Debe seleccionar un Usuario antes", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    private void btnEliminarUsuarioClick(ActionEvent event) {

    }

    @FXML
    private void btnPermisosClick(ActionEvent event) {

    }
    @FXML TextField txtFiltro;

    @FXML
    AnchorPane MantUsuario;

     FirebaseConnector db;
     User tablaUSelectedItem;
     ObservableList<User> usuarios = FXCollections.observableArrayList();
     
     
     


    @Override
    public void initialize(URL url, ResourceBundle rb) {
  
        EventListeners.onWindowOpened(MantUsuario, new Function<Window,Void>(){
            @Override
            public Void apply(Window parent) {
                iniciarEstructuraTablas();     
                
                db=FirebaseConnector.getInstance();                
                List<QueryDocumentSnapshot> documentos = db.getAllDocumentsFrom(FirestoreRoutes.USUARIOS);
                List<QueryDocumentSnapshot> docsUsuarios = db.getAllDocumentsFrom(FirestoreRoutes.USUARIOS);
                List<QueryDocumentSnapshot> docsAreas = db.getAllDocumentsFrom(FirestoreRoutes.AREAS);
                List<Area> areas = new ArrayList();

                for(DocumentSnapshot doc : docsAreas){
                    Area tmp = new Area(doc.getId(), doc.getString("Area"));
                    areas.add(tmp);
                }
                System.out.println(areas);

                for (DocumentSnapshot doc : documentos) {
                    System.out.println(doc);
                    User tmp;
                     if(doc.exists()){
                    List<String> arregloIDAreas = (List<String>) doc.get("areas");
                    System.out.println(arregloIDAreas);
                        // // ["8DjeIqzY7Sw0PGRyZBXC","10DjwsdaGRyZBXC"]
                    List<String> areasConNombre = new ArrayList();

                      for(Area area : areas){
                        for(String areaID : arregloIDAreas){
                            System.out.println(area.areaID);
                                if(areaID.equals(area.areaID.trim()))
                                 {
                                    System.out.println("Nombre del area agregada");
                                    areasConNombre.add(area.nombre);
                                    break;
                                 }   
                             }
                         }
                        // areasConNombre = ["Cocina", "Meseros"]
                        //List<String> prueba = new ArrayList();
                        //prueba.add("hola");
                        //prueba.add("hola2");

                        tmp = new User(doc.getId(), doc.getString("Nombre"),
                        doc.getString("nivelAcceso"), areasConNombre);
    
                        System.out.println(tmp.nombre);
                        System.out.println(doc.getData());
                        usuarios.add(tmp);
                        System.out.println(usuarios);
                        }
                    }
                    tablaUsuarios.getItems().addAll(usuarios);

                 return null;
            }


        });

        MantUsuario.widthProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                recalcularColumnWidth();
               
            }
        });

        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>(){
            @Override
            public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                tablaUSelectedItem = newValue;
            }
        });
        MantUsuario.widthProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                recalcularColumnWidth();
            }
        });

  
    }
    
    
       
    
  
    private void recalcularColumnWidth(){
                       
        ObservableList columnasUsuario = tablaUsuarios.getColumns();

        ((TableColumn)( columnasUsuario.get(0) )).setPrefWidth(tablaUsuarios.getWidth()*0.20);
        ((TableColumn)( columnasUsuario.get(1) )).setPrefWidth(tablaUsuarios.getWidth()*0.30);
        ((TableColumn)( columnasUsuario.get(2) )).setPrefWidth(tablaUsuarios.getWidth()*0.20);
        ((TableColumn)( columnasUsuario.get(3) )).setPrefWidth(tablaUsuarios.getWidth()*0.20);
        
        
    }    

    

    private void iniciarEstructuraTablas(){
       // Colocar nombre de columnas y obtener campos

        //Tabla de usuarios
        tablaUsuarios.getItems().clear();
        tablaUsuarios.getColumns().clear();
        TableColumn columnaCodigo = new TableColumn<>("Codigo");
        columnaCodigo.setCellValueFactory(new PropertyValueFactory<>("userID"));
        columnaCodigo.setPrefWidth(tablaUsuarios.getWidth()*0.15);

        TableColumn columnaUsuario = new TableColumn<>("Nombre");
        columnaUsuario.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaUsuario.setPrefWidth(tablaUsuarios.getWidth()*0.30);

        TableColumn columnaNivelAcceso = new TableColumn<>("Nivel de acceso");
        columnaNivelAcceso.setCellValueFactory(new PropertyValueFactory<>("nivelAcceso"));
        columnaNivelAcceso.setPrefWidth(tablaUsuarios.getWidth()*0.20);

        TableColumn columnaArea = new TableColumn<>("Areas");
        columnaArea.setCellValueFactory(new PropertyValueFactory<>("stringDeArea"));
        columnaArea.setPrefWidth(tablaUsuarios.getWidth()*0.15);

        
        tablaUsuarios.getColumns().addAll(columnaCodigo, columnaUsuario, columnaNivelAcceso, columnaArea);

       
    }
/*
    private void popularTablaMantUsuarios(TableView<User> tabla, ObservableList<User> Usuarios) {
        try{
            tabla.getItems().clear();
            for (User usuario : Usuarios) {
                User row = new User(usuario.userID, usuario.nombre, usuario.nivelAcceso, usuario.area, usuario.grupo);
                tabla.getItems().add(row);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
*/


}
