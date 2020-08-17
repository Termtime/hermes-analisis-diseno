package com.unah.hermes;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;

import com.unah.hermes.provider.FirebaseConnector;
import com.unah.hermes.utils.EventListeners;
import com.unah.hermes.utils.Navigation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

public class MantProductosModalAgregarCategoria implements Initializable{
    
    @FXML private void btnCancelarClick(ActionEvent event) {
        cerrarVentana();
    }
    @FXML private void txtAgregarCategoriaInput(ActionEvent event) {
        
    }
    @FXML private void btnAgregarClick(ActionEvent event) {
        if(txtAgregarCategoria.getText().equals(""))
            {
                Navigation.pushRoute("AlertError", event, false, true);
                return;
            }else{
                Map<String, Object> data= new HashMap<>();
                data.put("nombre", txtAgregarCategoria.getText());
                
                try {
                    db.createDocument("Categorias", data);
                    Navigation.pushRoute("AlertExito", event, false, true);
                } catch (Exception e) {
                    Navigation.pushRoute("AlertError", event, false, true);
                }
                Stage stage = (Stage) btnCancelar.getScene().getWindow();

            stage.close();
            }   
    }
    @FXML AnchorPane mantProductosModalAgregarCategoria;
    @FXML AnchorPane AgregarCategoria;
    @FXML Button btnCancelar;
    @FXML TextField txtAgregarCategoria;
    FirebaseConnector db;
    @Override
    public void initialize(URL url,  ResourceBundle rb) {
        EventListeners.onWindowOpening(mantProductosModalAgregarCategoria, new Function<Window,Void>(){

            @Override
            public Void apply(Window t) {
                ((Stage)t).resizableProperty().setValue(Boolean.FALSE);
                return null;
            }
            
        });
        db=FirebaseConnector.getInstance();
    }
    private void cerrarVentana(){
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
}