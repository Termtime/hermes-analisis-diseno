package com.unah.hermes;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TableColumn;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.ListenerRegistration;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.unah.hermes.provider.FirebaseConnector;
import com.unah.hermes.provider.FirestoreRoutes;
import com.unah.hermes.utils.EventListeners;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.ListenerRegistration;
import com.unah.hermes.objects.Requisicion;
import com.unah.hermes.provider.FirebaseConnector;
import com.unah.hermes.utils.EventListeners;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
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

import com.unah.hermes.objects.Area;
import com.unah.hermes.objects.User; //Consulta objeto
import com.unah.hermes.utils.Navigation;

public class MantAreasModalAgregarUsuarioArea implements Initializable {

    @FXML
    TableView<User> tablaUsuariosArea;

    ObservableList<User> usuarios = FXCollections.observableArrayList();

    @FXML
    private void btnAgregarClick(ActionEvent event) {

    }

    @FXML
    private void btnCancelarClick(ActionEvent event) {

    }

    @FXML
    private void txtBuscarInput(ActionEvent event) {

    }

    User usuariosNoArea;
    Area areaSelected;

    public void initData(Object obj) {
        areaSelected = (Area) obj;
        for (User usuario : usuarios) {
            for (int i = 0; i < usuario.areas.size(); i++) {
                if (usuario.areas.get(i).equals(areaSelected.areaID)) {
                    break;
                }
                else{
                    tablaUsuariosArea.getItems().add(usuario);
                }

            }
        }
    }
    FirebaseConnector db;
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
        EventListeners.onWindowOpened(MantAreasModalAgregarUsuarioArea, new Function<Window, Void>() {
            @Override
            public Void apply(Window parent) {
                    //iniciarEstructuraTablas();
                    db = FirebaseConnector.getInstance();
                    
                    // Usuarios inicio (Prueba)
                    List<QueryDocumentSnapshot> usuariosFirebase = db
                                    .getAllDocumentsFrom(FirestoreRoutes.USUARIOS);
                    List<QueryDocumentSnapshot> docsAreas = db.getAllDocumentsFrom(FirestoreRoutes.AREAS);
                    for (DocumentSnapshot doc : usuariosFirebase) {
                            User tmp;

                            if (doc.exists()) {
                                    List<String> arregloIDAreas = (List<String>) doc.get("areas");
                                    
                                    tmp = new User(doc.getId(), doc.getString("Nombre"),
                                                    doc.getString("nivelAcceso"), arregloIDAreas);

                                    usuarios.add(tmp);
 
                            }
                    }
                    // tablaUsuario.getItems().addAll(usuariosArea);
                    return null;
            }
    });




    }

}