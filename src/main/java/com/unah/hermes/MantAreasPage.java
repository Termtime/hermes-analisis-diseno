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

public class MantAreasPage implements Initializable {

        @FXML
        private void btnCrearAreaClick(ActionEvent event) {
                Parent root;
                Window parentStage;
                try {
                        parentStage = ((Node) (event.getSource())).getScene().getWindow();
                } catch (Exception e) {
                        try {
                                root = FXMLLoader.load(getClass().getResource("/fxml/crearArea.fxml"));
                                Stage stage = new Stage();
                                stage.setTitle("My New Stage Title");
                                stage.setScene(new Scene(root));
                                stage.initModality(Modality.APPLICATION_MODAL);
                                stage.showAndWait();
                                // stage.show();
                        } catch (IOException a) {
                                a.printStackTrace();
                        }
                }
        }

        @Override
        public void initialize(URL arg0, ResourceBundle arg1) {
                // TODO Auto-generated method stub

        }

}