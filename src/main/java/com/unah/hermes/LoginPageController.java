package com.unah.hermes;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.unah.hermes.provider.FirebaseConnector;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class LoginPageController implements Initializable {
    
    private FirebaseConnector db;

    @FXML
    private TextField correoTxt;
    @FXML
    private PasswordField passTxt;
    
    private String correo, pass;
    @FXML
    private void loginBtnClick(ActionEvent event)
    {
        correo = correoTxt.getText();
        pass = passTxt.getText();

        if(db.loginWithEmailPassword(correo, pass))
        {
            Parent root;
            try {
                root = FXMLLoader.load(getClass().getResource("/fxml/MainPage.fxml"));
                Stage stage = new Stage();
                stage.setTitle("My New Stage Title");
                stage.setScene(new Scene(root));
                stage.show();
                // Hide this current window (if this is what you want)
                ((Node)(event.getSource())).getScene().getWindow().hide();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = FirebaseConnector.getInstance();
    }    
    
}