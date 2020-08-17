package com.unah.hermes;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.unah.hermes.objects.Area;
import com.unah.hermes.objects.User;
import com.unah.hermes.provider.FirebaseConnector;
import com.unah.hermes.provider.FirestoreRoutes;
import com.unah.hermes.utils.EventListeners;
import com.unah.hermes.utils.Navigation;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;

public class MantAreasPage implements Initializable {

    @FXML private void btnCrearAreaClick(ActionEvent event) {
        Navigation.pushRoute("MantAreasModalCrearArea", event, false, true);
        llenarTablaAreas();
    }

    @FXML private void btnEliminarAreaClick(ActionEvent event) {
        if (!areaSelectedID.isEmpty()) {
            for (User usuario : usuarios) {
                if (usuario.areas.contains(areaSelectedID)) {
                    usuario.areas.remove(areaSelectedID);
                    Map<String, Object> datos = new HashMap();
                    List<String> vacio = new ArrayList();
                    vacio.add("");
                    if (usuario.areas.isEmpty())
                        datos.put("areas", vacio);
                    else
                        datos.put("areas", usuario.areas);

                    db.updateDocument(FirestoreRoutes.USUARIOS, usuario.userID, datos);
                }
            }
            db.deleteDocument(FirestoreRoutes.AREAS, areaSelectedID);
            Navigation.mostrarAlertExito("Area eliminada exitosamente", event);
            llenarTablaAreas();
            tablaUsuario.getItems().clear();
        } else {
            Navigation.mostrarAlertError("Debe seleccionar un Area antes", event);
        }
    }

    @FXML private void btnAgregarUsuarioAreaClick(ActionEvent event) {
        if (TablaAreaSelectedRow != null) {
            Navigation.pushRouteWithParameter("MantAreasAgregarUsuarioArea", event, false, true,
                    MantAreasModalAgregarUsuarioArea.class, TablaAreaSelectedRow);
            llenarTablaUsuario(areaSelectedID);
        } else {
            Navigation.mostrarAlertError("Debe seleccionar un Area antes", event);
        }
    }

    @FXML private void btnEliminarUsuarioAreaClick(ActionEvent event) {

        if (!usuarioID.isEmpty() && !areasUsuarioArray.isEmpty()) {
            Map<String, Object> datos = new HashMap();
            areasUsuarioArray.remove(index);
            datos.put("areas", areasUsuarioArray);
            db.updateDocument(FirestoreRoutes.USUARIOS, usuarioID, datos);
            Navigation.mostrarAlertExito("Usuario eliminado exitosamente del área", event);
            llenarTablaUsuario(areaSelectedID);
        } else {
            Navigation.mostrarAlertError("No ha seleccionado un usuario", event);
        }
    }

    @FXML TableView<Area> tablaArea;
    @FXML TableView<User> tablaUsuario;
    @FXML AnchorPane MantenimientoAreas;
    Area areaSelected;
    String areaSelectedID;
    User selectedUser;
    String areaID;
    String usuarioEmail;
    String usuarioName;
    String usuarioAcces;
    List<String> areasUsuarioArray;
    List<Area> areas;
    int index;
    FirebaseConnector db;
    Area TablaAreaSelectedRow;
    String usuarioID = "";
    ObservableList<Area> Areas = FXCollections.observableArrayList();
    ObservableList<User> usuarios = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MantenimientoAreas.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                MantenimientoAreas.requestFocus();
            }
        });
        EventListeners.onWindowOpened(MantenimientoAreas, new Function<Window, Void>() {
            @Override
            public Void apply(Window parent) {
                iniciarEstructuraTablas();
                db = FirebaseConnector.getInstance();
                // Areas
                llenarTablaAreas();

                // Usuarios inicio

                List<QueryDocumentSnapshot> usuariosFirebase = db.getAllDocumentsFrom(FirestoreRoutes.USUARIOS);
                List<QueryDocumentSnapshot> docsAreas = db.getAllDocumentsFrom(FirestoreRoutes.AREAS);
                for (DocumentSnapshot doc : usuariosFirebase) {
                    User tmp;

                    if (doc.exists()) {
                        List<String> arregloIDAreas = (List<String>) doc.get("areas");

                        tmp = new User(doc.getId(), doc.getString("Nombre"), doc.getString("nivelAcceso"),
                                arregloIDAreas);

                        usuarios.add(tmp);

                    }
                }

                return null;
            }
        });

        MantenimientoAreas.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

            }
        });

        tablaArea.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Area>() {
            @Override
            public void changed(ObservableValue<? extends Area> observable, Area oldValue, Area newValue) {
                areaSelectedID = "";
                TablaAreaSelectedRow = newValue;
                llenarTablaUsuario(newValue.areaID);
                areaSelectedID = TablaAreaSelectedRow.areaID;
            }
        });

        tablaUsuario.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                selectedUser = newValue;
                usuarioID = selectedUser.userID;
                areasUsuarioArray = selectedUser.areas;
                index = areasUsuarioArray.indexOf(areaSelectedID);

                // areasUsuarioArray.add(areaID);
                usuarioEmail = "correo@email.com";
                usuarioName = selectedUser.nombre;
                usuarioAcces = selectedUser.nivelAcceso;
            }
        });
    }

    private void iniciarEstructuraTablas() {
        tablaArea.getItems().clear();
        tablaArea.getColumns().clear();
        TableColumn columnArea = new TableColumn<>("Area");
        columnArea.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnArea.setPrefWidth(tablaUsuario.getWidth() * 0.95);

        tablaArea.getColumns().addAll(columnArea);

        // Tabla Usuarios

        tablaUsuario.getItems().clear();
        tablaUsuario.getColumns().clear();
        TableColumn columnUsuario = new TableColumn<>("Usuario");
        columnUsuario.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnUsuario.setPrefWidth(tablaUsuario.getWidth() * 0.95);

        tablaUsuario.getColumns().addAll(columnUsuario);
    }

    private void llenarTablaUsuario(String areaID) {
        usuarios.clear();
        List<QueryDocumentSnapshot> usuariosFirebase = db.getAllDocumentsFrom(FirestoreRoutes.USUARIOS);
        List<QueryDocumentSnapshot> docsAreas = db.getAllDocumentsFrom(FirestoreRoutes.AREAS);
        for (DocumentSnapshot doc : usuariosFirebase) {
            User tmp;

            if (doc.exists()) {
                List<String> arregloIDAreas = (List<String>) doc.get("areas");

                tmp = new User(doc.getId(), doc.getString("Nombre"), doc.getString("nivelAcceso"), arregloIDAreas);

                usuarios.add(tmp);

            }
        }
        tablaUsuario.getItems().clear();
        for (User usuario : usuarios) {
            for (int i = 0; i < usuario.areas.size(); i++) {
                if (usuario.areas.get(i).equals(areaID)) {
                    tablaUsuario.getItems().add(usuario);
                    break;
                }

            }
        }
    }

    private void llenarTablaAreas() {
        db = FirebaseConnector.getInstance();
        Areas.clear();
        // Areas
        List<QueryDocumentSnapshot> documentos = db.getAllDocumentsFrom(FirestoreRoutes.AREAS);

        for (DocumentSnapshot doc : documentos) {

            Area tmp;

            if (doc.exists()) {
                tmp = new Area(doc.getId(), doc.getString("Area"));

                Areas.add(tmp);

            }
        }
        tablaArea.getItems().clear();
        tablaArea.getItems().addAll(Areas);
    }
}
