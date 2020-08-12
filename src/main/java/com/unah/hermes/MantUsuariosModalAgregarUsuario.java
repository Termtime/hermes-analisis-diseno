package com.unah.hermes;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FirestoreException;
import com.unah.hermes.objects.Area;
import com.unah.hermes.objects.Producto;
import com.unah.hermes.objects.Requisicion;
import com.unah.hermes.provider.FirebaseConnector;
import com.unah.hermes.utils.Navigation;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.stage.Modality;
import java.util.ArrayList;


public class MantUsuariosModalAgregarUsuario implements Initializable {
    @FXML TextField txtNombre;
    @FXML ComboBox<String> comboNivelAcceso= new ComboBox<>();
    @FXML ListView<Area> listAreasSeleccionadas = new ListView<>();
    @FXML TextField txtCorreo;
    @FXML PasswordField txtContrasena;
    @FXML PasswordField txtConrfirmeContrasena;


 

                
    @FXML public void btnAgregarClick(ActionEvent event) {                
          
         //db.crearUsuario(txtCorreo.getText(),txtContrasena.getText(),txtNombre.getText(),comboNivelAcceso.getSelectionModel().getSelectedItem().toString(), listAreasSeleccionadas);
         // db.crearUsuario(txtCorreo.getText(),txtContrasena.getText(),txtNombre.getText(),comboNivelAcceso.getSelectionModel().getSelectedItem().toString(),listAreasSeleccionadas.getItems() );          
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
    @FXML public void btnQuitarAreaClick(ActionEvent event) {
    }
    @FXML public void comboNivelAccesoClick(ActionEvent event) {
    }
    
    @FXML public void txtCorreoInput(KeyEvent event) {
    }
   
    @FXML public javafx.scene.control.Button btnCancelar;

    @FXML public void btnCancelarClick(ActionEvent event) {
        
       Stage stage = (Stage) btnCancelar.getScene().getWindow();
       
       stage.close();

    }

    FirebaseConnector db = FirebaseConnector.getInstance();
    //List<QueryDocumentSnapshot> docsAreas = db.getAllDocumentsFrom(FirestoreRoutes.AREAS);  


    @FXML AnchorPane mantUsuariosModalAgregarUsuario;
    @Override
    public void initialize(URL url,  ResourceBundle rb) {
       // Area area= new Area("nombre");
       // Area area1= new Area("nombre1");
       // listAreasSeleccionadas.getItems().add(area1.nombre);
        
       // listAreasSeleccionadas.getItems().add(area.nombre);
        mantUsuariosModalAgregarUsuario.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                mantUsuariosModalAgregarUsuario.requestFocus();


            }
        });
    }    
    
}
