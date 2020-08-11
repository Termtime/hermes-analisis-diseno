package com.unah.hermes;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class EntregaReqPage implements Initializable {
    
    public void initData(Object data){
    }
    @FXML AnchorPane entregaReqPage;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        entregaReqPage.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                entregaReqPage.requestFocus();

            }
        });
    }    
    
}