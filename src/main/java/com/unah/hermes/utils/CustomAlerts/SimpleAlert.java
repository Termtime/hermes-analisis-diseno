package com.unah.hermes.utils.CustomAlerts;
 
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;

import com.google.cloud.firestore.EventListener;
import com.unah.hermes.utils.EventListeners;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class SimpleAlert implements Initializable {
    

    @FXML
    private Label labelMsg;
    
    @FXML
    private AnchorPane alertBody;

    @FXML
    private void btnOkClick(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
    Window owner;
    String mensaje;

    public void initData(Object data, Window dueno){
         mensaje = data.toString();
         owner = dueno;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        

        EventListeners.onWindowOpening(alertBody, new Function<Window,Void>(){

            @Override
            public Void apply(Window t) {
                
                ((Stage)t).initOwner(owner);
                ((Stage)t).initModality(Modality.APPLICATION_MODAL);
                ((Stage)t).setResizable(false);
                // ((Stage)t).resizableProperty().setValue(Boolean.FALSE);
                // ((Stage)t).initStyle(StageStyle.UNDECORATED);
                return null;
            }
            
        });
        EventListeners.onWindowOpened(alertBody, new Function<Window,Void>(){

            @Override
            public Void apply(Window t) {
                labelMsg.setText(mensaje);
                return null;
            }
            
        });
    }    
    
}