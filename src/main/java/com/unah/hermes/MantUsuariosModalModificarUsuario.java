package com.unah.hermes;

import java.net.URL;
import java.util.ResourceBundle;

import com.unah.hermes.utils.Navigation;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MantUsuariosModalModificarUsuario implements Initializable {
    
    @FXML public void btnAgregarClick(ActionEvent event) {
        
        Navigation.pushRoute("MantUsuariosPage", event, false, true);

    }
    @FXML public void txtUsuarioInput(KeyEvent event) {
    }

    @FXML public void txtNombreInput(KeyEvent event) {
    }
    @FXML public void txtContrasenaInput(KeyEvent event) {
    }
    @FXML public void txtConfirmeContrasenaInput(KeyEvent event) {
    }

    @FXML public void comboAreaAccesoClick(ActionEvent event) {
    }
    @FXML public void btnAgregarAreaClick(ActionEvent event) {
    }
    @FXML public void btnModificarClick(ActionEvent event) {
    }
    @FXML public void btnQuitarAreaClick(ActionEvent event) {
    }
    @FXML public void comboNivelAccesoClick(ActionEvent event) {
    }
    
    @FXML public void txtCorreoInput(KeyEvent event) {
    }
   

    @FXML public void btnCancelarClick(ActionEvent event) {
        
       Stage stage = (Stage) btnCancelar.getScene().getWindow();
       
       stage.close();

    }
    
    
    @FXML private javafx.scene.control.Button btnCancelar;
  
    public void initData(Object data){
        System.out.println("Inicialiar datos");
        User usuarioDatos = (User) data; cambio
        txtFiltro.setText(usuarioDatos.nombre);     
        */
    }
    @FXML AnchorPane mantUsuariosModalModificarUsuario;
    @Override
    public void initialize(URL url,  ResourceBundle rb) {
        mantUsuariosModalModificarUsuario.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                mantUsuariosModalModificarUsuario.requestFocus();

            }
        });
    }    
}
