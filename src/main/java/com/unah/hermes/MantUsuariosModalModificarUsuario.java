package com.unah.hermes;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.List;

import com.unah.hermes.objects.Area;
import com.unah.hermes.objects.User;
import com.unah.hermes.utils.Navigation;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.DocumentSnapshot;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import javafx.scene.layout.Pane;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MantUsuariosModalModificarUsuario implements Initializable {
    @FXML ListView<Area>  listAreas;
    @FXML
    TextField txtNombre;
    @FXML
    ComboBox<String> comboNivelAcceso = new ComboBox<>();
    @FXML
    ListView<Area> listAreasSeleccionadas;
    @FXML
    TextField txtCorreo;
    @FXML
    PasswordField txtContrasena;
    @FXML
    PasswordField txtConrfirmeContrasena;
    @FXML
    ComboBox<Area> comboAreaAcceso;
    @FXML Button btnAgregarArea;
    @FXML Button btnQuitarArea;

    
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
        //System.out.println("Inicialiar datos");
        User usuarioDatos = (User) data; 
        txtNombre.setText(usuarioDatos.nombre);
        txtCorreo.setText(usuarioDatos.userID); 
        //listAreasSeleccionadas.set

    }

    @FXML Pane panelJefeUsuario;
    @FXML AnchorPane mantUsuariosModalAgregarUsuario;

    ObservableList<Area> areas = FXCollections.observableArrayList();
    ObservableList<User> usuarios = FXCollections.observableArrayList();
    List<QueryDocumentSnapshot> docsAreas;
    List<QueryDocumentSnapshot> documentos;
    
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
    

    public void llenarCombo(){
        //procesar datos de firebase
        for (DocumentSnapshot doc : docsAreas) {
            Area tmp = new Area(doc.getId(), doc.getString("Area"));
            areas.add(tmp);
        }
          
        //llenado del combobox de nivel de acceso
        comboNivelAcceso.getItems().add("Usuario");
        comboNivelAcceso.getItems().add("Jefe de Area");
        comboNivelAcceso.getItems().add("Administrador");
        
        comboNivelAcceso.getSelectionModel().selectFirst();
        comboAreaAcceso.setVisible(true);
        listAreas.setVisible(false);
        listAreasSeleccionadas.setVisible(false);
        btnAgregarArea.setVisible(false);
        btnQuitarArea.setVisible(false);
        //llenado del combobox de areas
        comboAreaAcceso.getItems().addAll(areas);

        //para la ventana de modificar, para llenar la lista de la derecha y la izquierda
        for (Area area : areas) {
            //recibirias el usuario, tendrias un objeto -User usuario
             //usuario.areas List<String>
             String areaID = area.areaID;
             List<String> usuarioAreas = new ArrayList();
             if(usuarioAreas.contains(areaID))
             {
                 listAreasSeleccionadas.getItems().add(area);
             }else{
                 //listaDeLaIzquierda
             }
         }
        
        //llenado de la lista de la izquierda
        listAreas.getItems().addAll(areas);
    }
    
}
