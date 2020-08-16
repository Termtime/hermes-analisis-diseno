package com.unah.hermes;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FirestoreException;
import com.unah.hermes.objects.Producto;
import com.unah.hermes.objects.Requisicion;
import com.unah.hermes.provider.FirebaseConnector;
import com.unah.hermes.provider.FirestoreRoutes;
import com.unah.hermes.utils.Navigation;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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

import javafx.scene.control.Alert.AlertType;

public class MantAreasModalCrearArea implements Initializable {

    @FXML
    TextField txtNombreArea;

    @FXML
    private javafx.scene.control.Button btnCancelar;

    FirebaseConnector db = FirebaseConnector.getInstance();

    @FXML
    private void btnAgregarClick(ActionEvent event) {
        String areaNueva = txtNombreArea.getText().trim();
        if (!areaNueva.isEmpty()) {
            Map<String, Object> area = new HashMap();
            area.put("Area", areaNueva);

            db.createDocument(FirestoreRoutes.AREAS, area);
            areaNueva = "";
            txtNombreArea.setText(areaNueva);
            Navigation.mostrarAlertExito("Area Agregada Exitosamente", event);
        } else {
            // Alert alert = new Alert(AlertType.ERROR, "Nombre de Area en Blanco",
            // ButtonType.OK);
            // alert.showAndWait();
            Navigation.mostrarAlertError("Nombre de Area en Blanco", event);
        }

    }

    @FXML
    private void btnCancelarClick(ActionEvent event) {
        Stage ventana = (Stage) btnCancelar.getScene().getWindow();

        ventana.close();
    }

    @FXML
    private void txtNombreAreaInput(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //
    }
}
