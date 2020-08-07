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
import com.unah.hermes.objects.Requisicion;
import com.unah.hermes.provider.FirebaseConnector;

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

public class MantUsuariosPageController implements Initializable {

    @FXML
    TableView<User> tablaUsuarios;

    @FXML
    private void btnAgregarUsuarioClick(ActionEvent event) {
        Navigation.pushRoute("agregarUsuario", event, false, true);
    }

    @FXML
    private void btnModificarUsuarioClick(ActionEvent event) {
        Navigation.pushRoute("modificarUsuario", event, false, true);
    }

    @FXML
    private void btnEliminarUsuarioClick(ActionEvent event) {

    }

    @FXML
    private void btnPermisosClick(ActionEvent event) {

    }

    @FXML
    AnchorPane MantUsuario;

   // List<QueryDocumentSnapshot> db;
    //User tablaPSelectedItem;
    //public static ObservableList<User> Usuarios = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
  
   /*
        
        EventListeners.onWindowOpened(MantUsuario, new Function<Window,Void>(){
            @Override
            public Void apply(Window parent) {
                iniciarEstructuraTablas();                            
                 return null;
            }
        });

        MantUsuario.widthProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                recalcularColumnWidth();
            }
        });

        MantUsuario.widthProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                recalcularColumnWidth();
            }
        });

       // db = FirebaseConnector.getInstance().getAllDocumentsFrom("/Usuarios");

        /*
        tablaUsuarios.getItems().addListener( (ListChangeListener<? super User>) new ChangeListener<User>(){
            @Override
            public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                tablaPSelectedItem = newValue;
                System.out.println(newValue);
                popularTablaMantUsuarios(tablaUsuarios , newValue.Usuarios);
            }
        });

        */
    }
    
    
       
    
/*    
    private void recalcularColumnWidth(){
                       
        ObservableList columnasUsuario = tablaUsuarios.getColumns();

        ((TableColumn)( columnasUsuario.get(0) )).setPrefWidth(tablaUsuarios.getWidth()*0.15);
        ((TableColumn)( columnasUsuario.get(1) )).setPrefWidth(tablaUsuarios.getWidth()*0.30);
        ((TableColumn)( columnasUsuario.get(2) )).setPrefWidth(tablaUsuarios.getWidth()*0.20);
        ((TableColumn)( columnasUsuario.get(3) )).setPrefWidth(tablaUsuarios.getWidth()*0.15);
        ((TableColumn)( columnasUsuario.get(4) )).setPrefWidth(tablaUsuarios.getWidth()*0.20);
        
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

        TableColumn columnaArea = new TableColumn<>("Area");
        columnaArea.setCellValueFactory(new PropertyValueFactory<>("area"));
        columnaArea.setPrefWidth(tablaUsuarios.getWidth()*0.15);

        TableColumn columnaGrupo = new TableColumn<>("Grupo");
        columnaGrupo.setCellValueFactory(new PropertyValueFactory<>("grupo"));
        columnaGrupo.setPrefWidth(tablaUsuarios.getWidth()*0.20);

        tablaUsuarios.getColumns().addAll(columnaCodigo, columnaUsuario, columnaNivelAcceso, columnaArea, columnaGrupo);

       
    }

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
