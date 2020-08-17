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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

public class MantAreasModalAgregarUsuarioArea implements Initializable {

    @FXML private void btnAgregarClick(ActionEvent event) {
        if (!usuarioID.isEmpty() && !areasUsuarioArray.isEmpty()) {
            Map<String, Object> datos = new HashMap();

            datos.put("areas", areasUsuarioArray);
            db.updateDocument(FirestoreRoutes.USUARIOS, usuarioID, datos);
            Navigation.mostrarAlertExito("Usuario Agregado exitosamente al área", event);
            llenarTabla();

            Stage ventana = (Stage) btnCancelar.getScene().getWindow();

            ventana.close();
        } else {
            Navigation.mostrarAlertError("No ha seleccionado un usuario", event);
        }
    }

    @FXML private void btnCancelarClick(ActionEvent event) {
        Stage ventana = (Stage) btnCancelar.getScene().getWindow();

        ventana.close();

    }

    @FXML private void txtBuscarInput(KeyEvent event) {
        tablaUsuariosArea.getItems().clear();
        List<User> usuariosFiltro = new ArrayList<User>();

        for (User usuario : usuarios) {
            if (usuario.nombre.toLowerCase().contains(txtBuscar.getText().toLowerCase())
                    || txtBuscar.getText().equals("")) {
                usuariosFiltro.add(usuario);
            }
        }
        tablaUsuariosArea.getItems().addAll(usuariosFiltro);
    }

    @FXML TableView<User> tablaUsuariosArea;
    @FXML AnchorPane MantAreasModalAgregarUsuarioArea;
    @FXML TextField txtBuscar;
    @FXML Button btnCancelar;
    User usuariosNoArea;
    Area areaSelected;
    User selectedUser;
    String areaID;
    String usuarioEmail;
    String usuarioName;
    String usuarioAcces;
    List<String> areasUsuarioArray;
    List<Area> areas;
    String usuarioID = "";
    ObservableList<User> usuarios = FXCollections.observableArrayList();
    FirebaseConnector db;
    public void initData(Object area) {
        areaSelected = (Area) area;
        areaID = areaSelected.areaID;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
        EventListeners.onWindowOpened(MantAreasModalAgregarUsuarioArea, new Function<Window, Void>() {
            @Override
            public Void apply(Window parent) {
                iniciarEstructuraTablas();
                llenarTabla();

                return null;
            }
        });

        tablaUsuariosArea.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                selectedUser = newValue;

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

    private void llenarTabla() {
        db = FirebaseConnector.getInstance();

        // Usuarios inicio (Prueba)
        List<QueryDocumentSnapshot> usuariosFirebase = db.getAllDocumentsFrom(FirestoreRoutes.USUARIOS);
        List<QueryDocumentSnapshot> docsAreas = db.getAllDocumentsFrom(FirestoreRoutes.AREAS);
        usuarios.clear();
        for (DocumentSnapshot doc : usuariosFirebase) {
            User tmp;

            if (doc.exists()) {
                List<String> arregloIDAreas = (List<String>) doc.get("areas");
                if (!arregloIDAreas.contains(areaSelected.areaID)) {
                    tmp = new User(doc.getId(), doc.getString("Nombre"), doc.getString("nivelAcceso"), arregloIDAreas);
                    System.out.println(tmp.nombre);
                    usuarios.add(tmp);
                }

            }
        }
        tablaUsuariosArea.getItems().clear();
        tablaUsuariosArea.getItems().addAll(usuarios);
    }

}