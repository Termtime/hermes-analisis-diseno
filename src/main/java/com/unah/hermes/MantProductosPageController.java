package com.unah.hermes;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import com.unah.hermes.utils.Navigation;

public class MantProductosPageController implements Initializable {
    @FXML private void btnAgregarProductoClick(ActionEvent event) {
        Navigation.pushRoute("Mant_Agregar_Producto", event, false, true);
    }
    @FXML private void btnModificarProductoClick(ActionEvent event) {
        Navigation.pushRoute("Mant_Modificar_Producto", event, false, true);
    }
    @FXML private void btnAgregarCategoriaClick(ActionEvent event) {
        Navigation.pushRoute("Mant_Agregar_Categoria", event, false, true);
    }
    @FXML private void btnModificarCategoriaClick(ActionEvent event) {
        Navigation.pushRoute("Mant_Modificar_Categoria", event, false, true);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // cambios
    }    
    
}
