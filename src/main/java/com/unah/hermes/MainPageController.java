package com.unah.hermes;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.stage.Window;
import javafx.stage.Modality;

public class MainPageController implements Initializable {
    
    @FXML public void menuBtnCerrarClick(ActionEvent event){

    }
    @FXML public void menuBtnImprimirClick(ActionEvent event){
        
    }
    @FXML public void menuBtnImprimirMensualClick(ActionEvent event){
        
    }
    @FXML public void menuBtnEntregarReqClick(ActionEvent event){
        
    }
    @FXML public void menuBtnDenegReqClick(ActionEvent event){
        
    }
    @FXML public void menuBtnOcultarReqClick(ActionEvent event){
        
    }
    @FXML public void menuBtnMantUsuariosClick(ActionEvent event){
        pushRoute("MantUsuariosPage", event, false, true);
    }
    @FXML public void menuBtnMantProductosClick(ActionEvent event){
        pushRoute("MantProductosPage", event, false, true);
    }
    @FXML public void menuBtnMantAreasClick(ActionEvent event){
        pushRoute("MantAreasPage", event, false, true);
    }

    @FXML public void btnEntregarClick(ActionEvent event){

    }
    @FXML public void btnDenegarClick(ActionEvent event){

    }
    
    public void pushRoute(String pageName, ActionEvent event, Boolean hide, Boolean modal)
    {
        Parent root;
        Window parentStage;
        try{
            parentStage = ((Node)(event.getSource())).getScene().getWindow();
        }catch(Exception e)
        {
            try{
                //intentar obtener el parent de un MenuItem
                parentStage = ((MenuItem)event.getTarget()).getParentPopup().getOwnerWindow();
            }catch(Exception ex){
                //fallar suavemente
                ex.printStackTrace();
                parentStage = null;
            }
        }
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/" + pageName + ".fxml"));
            Stage stage = new Stage();
            stage.setTitle("My New Stage Title");
            stage.setScene(new Scene(root));
            if(modal) {
                stage.initOwner(parentStage);
                stage.initModality(Modality.APPLICATION_MODAL); 
                stage.showAndWait();
            } else {
                stage.show();    
            }
            // Hide this current window (if this is what you want)
            if(hide) parentStage.hide();
            
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}