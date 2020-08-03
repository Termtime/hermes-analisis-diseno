package com.unah.hermes.utils;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Navigation {
    /**
     * Abre una nueva ventana
     * Por lo general se usa dentro de los handler de eventos de los clicks, ya que son los que tienen
     * ActionEvents
     * @param pageName Nombre (de archivo.fxml) de la pagina a abrir
     * @param event Es el evento que ocurre (click u otras cosas)
     * @param hide booleano para indicar si la pagina anterior debe ocultarse
     * @param modal boolean para indicar si la nueva pagina es modal
     */
    public static void pushRoute(String pageName, ActionEvent event, Boolean hide, Boolean modal)
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
            root = FXMLLoader.load(Navigation.class.getResource("/fxml/" + pageName + ".fxml"));
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
            // Ocultar la pagina anterior si es lo que se especifica
            if(hide) parentStage.hide();
            
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Abre una nueva ventana basandose en la scene y no en un evento
     * Utilizado para abrir una nueva ventana programaticamente, ya que no requiere de un evento de click
     * @param pageName Nombre (de archivo.fxml) de la pagina a abrir
     * @param elementID Es la escena actual de la pagina
     * @param hide booleano para indicar si la pagina anterior debe ocultarse
     * @param modal boolean para indicar si la nueva pagina es modal
     */
    public static void pushRouteFromScene(String pageName, Scene scene, Boolean hide, Boolean modal)
    {
        Parent root;
        Window parentStage = scene.getWindow();;
        try {
            root = FXMLLoader.load(Navigation.class.getResource("/fxml/" + pageName + ".fxml"));
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
            // Ocultar la pagina anterior si es lo que se especifica
            if(hide) parentStage.hide();
            
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}