package com.unah.hermes;

import java.net.URL;
import java.util.ResourceBundle;

import com.unah.hermes.utils.Navigation;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class MantUsuariosModalModificarUsuario implements Initializable {
    
    @FXML private void btnModificarClick(ActionEvent event) {
    
    }
    @FXML private void txtUsuarioInput(ActionEvent event) {
    }

    @FXML private void txtNombreInput(ActionEvent event) {
    }
    @FXML private void txtCorreoInput(ActionEvent event) {
    }
    @FXML private void comboNivelAccesoClick(ActionEvent event) {
    }
    @FXML private void txtContrasenaInput(ActionEvent event) {
    }
    @FXML private void txtConfirmeContrasenaInput(ActionEvent event) {
    }
    @FXML private void btnCancelarClick(ActionEvent event) {
    }
    
    
    @FXML private javafx.scene.control.Button btnCancelar;
  
    public void initData(Object data){
        /*  System.out.println("Inicialiar datos");
        User usuarioDatos = (User) data; cambio
        //txtFiltro.setText(usuarioDatos.nombre);     
        */
    }
    @FXML AnchorPane mantUsusariosModalModificarUsuario;
    @Override
    public void initialize(URL url,  ResourceBundle rb) {
        mantUsusariosModalModificarUsuario.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                mantUsusariosModalModificarUsuario.requestFocus();

            }
        });
    }    
}
