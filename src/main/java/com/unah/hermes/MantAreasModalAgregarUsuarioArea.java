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

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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

    @FXML
    AnchorPane MantAreasModalAgregarUsuarioArea;

    ObservableList<User> usuarios = FXCollections.observableArrayList();

    @FXML
    private void btnAgregarClick(ActionEvent event) {
        if (!usuarioID.isEmpty() && !areasUsuarioArray.isEmpty()) {
            Map<String, Object> datos = new HashMap();

            datos.put("areas", areasUsuarioArray);
            db.updateDocument(FirestoreRoutes.USUARIOS, usuarioID, datos);

        }
    }

    @FXML
    private void btnCancelarClick(ActionEvent event) {

    }

    @FXML
    private void txtBuscarInput(ActionEvent event) {

    }

    User usuariosNoArea;
    Area areaSelected;
    User selectedUser;
    String areaID;
    String usuarioID;
    String usuarioEmail;
    String usuarioName;
    String usuarioAcces;
    List<String> areasUsuarioArray;
    List<Area> areas;

    public void initData(Object area) {
        areaSelected = (Area) area;
        areaID = areaSelected.areaID;
    }

    FirebaseConnector db;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
        EventListeners.onWindowOpened(MantAreasModalAgregarUsuarioArea, new Function<Window, Void>() {
            @Override
            public Void apply(Window parent) {
                iniciarEstructuraTablas();
                db = FirebaseConnector.getInstance();

                // Usuarios inicio (Prueba)
                List<QueryDocumentSnapshot> usuariosFirebase = db.getAllDocumentsFrom(FirestoreRoutes.USUARIOS);
                List<QueryDocumentSnapshot> docsAreas = db.getAllDocumentsFrom(FirestoreRoutes.AREAS);
                tablaUsuariosArea.getItems().clear();
                for (DocumentSnapshot doc : usuariosFirebase) {
                    User tmp;

                    if (doc.exists()) {
                        List<String> arregloIDAreas = (List<String>) doc.get("areas");
                        if (!arregloIDAreas.contains(areaSelected.areaID)) {
                            tmp = new User(doc.getId(), doc.getString("Nombre"), doc.getString("nivelAcceso"),
                                    arregloIDAreas);
                            System.out.println(tmp.nombre);
                            usuarios.add(tmp);
                        }

                    }
                }
                tablaUsuariosArea.getItems().addAll(usuarios);
                // Platform.runLater(new Runnable() {
                // @Override
                // public void run() {
                // }
                // });
                return null;
            }
        });

        tablaUsuariosArea.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                selectedUser = newValue;
                // System.out.println(newValue);
                // llenarTablaUsuario(newValue.areaID);
                usuarioID = selectedUser.userID;
                areasUsuarioArray = selectedUser.areas;
                areasUsuarioArray.add(areaID);
                usuarioEmail = "correo@email.com";
                usuarioName = selectedUser.nombre;
                usuarioAcces = selectedUser.nivelAcceso;
            }
        });

    }

    private void iniciarEstructuraTablas() {
        tablaUsuariosArea.getItems().clear();
        tablaUsuariosArea.getColumns().clear();
        TableColumn columnUsuario = new TableColumn<>("Usuario");
        columnUsuario.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnUsuario.setPrefWidth(tablaUsuariosArea.getWidth() * 0.98);

        tablaUsuariosArea.getColumns().addAll(columnUsuario);
    }

}