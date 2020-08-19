package com.unah.hermes;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.unah.hermes.provider.FirebaseConnector;
import com.unah.hermes.provider.FirestoreRoutes;
import com.unah.hermes.utils.Navigation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MantAreasModalCrearArea implements Initializable {

    @FXML
    private void btnAgregarClick(ActionEvent event) {
        String areaNueva = txtNombreArea.getText().trim();
        if (!areaNueva.isEmpty()) {
            Map<String, Object> area = new HashMap();
            area.put("Area", areaNueva);

            db.createDocument(FirestoreRoutes.AREAS, area);
            areaNueva = "";
            txtNombreArea.setText(areaNueva);
            Navigation.mostrarAlertExito("Área agregada exitosamente.", event);

            Stage ventana = (Stage) btnCancelar.getScene().getWindow();

            ventana.close();
        } else {
            Navigation.mostrarAlertError("Nombre de área en blanco.", event);
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

    @FXML
    TextField txtNombreArea;
    @FXML
    Button btnCancelar;
    FirebaseConnector db = FirebaseConnector.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
