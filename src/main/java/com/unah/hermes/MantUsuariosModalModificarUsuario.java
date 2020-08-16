package com.unah.hermes;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.ArrayList;
import java.util.List;

import com.unah.hermes.objects.Area;
import com.unah.hermes.objects.User;
import com.unah.hermes.provider.FirebaseConnector;
import com.unah.hermes.provider.FirestoreRoutes;
import com.unah.hermes.utils.EventListeners;
import com.unah.hermes.utils.Navigation;
import com.google.cloud.firestore.QueryDocumentSnapshot;
//import com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Label;
import com.google.cloud.firestore.DocumentSnapshot;

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

import javafx.scene.layout.Pane;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

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
    @FXML Label labelAreasSeleccionada;
    @FXML Label labelAreas;

    
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

    public void btnAgregarImagenUsuarioClick(ActionEvent event){

    }
    @FXML public void btnModificarClick(ActionEvent event) {
    }
    @FXML public void btnAgregarAreaClick(ActionEvent event) {
        // if(listAreasSeleccionadas.getSelectionModel().getSelectedItem().equals(areas)){
                       
        // }
    }
    
    @FXML public void btnQuitarAreaClick(ActionEvent event) {
        
    }
    @FXML public void comboNivelAccesoClick(ActionEvent event) {

        if(comboNivelAcceso.getSelectionModel().isSelected(1)) {
             comboAreaAcceso.setVisible(true);
             listAreas.setVisible(true);
             listAreasSeleccionadas.setVisible(true);
             btnAgregarArea.setVisible(true);
             btnQuitarArea.setVisible(true);
   
            }
            if(comboNivelAcceso.getSelectionModel().isSelected(0)) {
                comboAreaAcceso.setVisible(false);
                listAreas.setVisible(false);
                listAreasSeleccionadas.setVisible(false);
                btnAgregarArea.setVisible(false);
                btnQuitarArea.setVisible(false);
      
               }

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
        usuarioDatos = (User) data; 
        // txtNombre.setText(usuarioDatos.nombre);
        // txtCorreo.setText(usuarioDatos.userID);

        // System.out.println(listAreas);
        // System.out.println("listAreasSeleccionadas");

        //llenarCombo();
        //listAreas.setItems(areas);
        //listAreasSeleccionadas.setItems(areas);
        //comboNivelAcceso.setSelectionModel(usuarioDatos.nivelAcceso);
        

    }

    @FXML Pane panelJefeUsuario;
    @FXML AnchorPane mantUsuariosModalAgregarUsuario;

    ObservableList<Area> areas = FXCollections.observableArrayList();
    ObservableList<User> usuarios = FXCollections.observableArrayList();
    FirebaseConnector db;
    User usuarioDatos;
    List<QueryDocumentSnapshot> docsAreas;
    
    @FXML AnchorPane mantUsuariosModalModificarUsuario;
    @Override
    public void initialize(URL url,  ResourceBundle rb) {
        db = FirebaseConnector.getInstance();
        EventListeners.onWindowOpening(mantUsuariosModalModificarUsuario, new Function<Window,Void>(){

            @Override
            public Void apply(Window t) {
                ((Stage)t).resizableProperty().setValue(Boolean.FALSE);
                return null;
            }
            
        });
        EventListeners.onWindowOpened(mantUsuariosModalModificarUsuario, new Function<Window,Void>(){

            @Override
            public Void apply(Window t) {
                docsAreas = db.getAllDocumentsFrom(FirestoreRoutes.AREAS);
                llenarCombo();
                llenarDatos();
                return null;
            }
            
        });
        mantUsuariosModalModificarUsuario.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                mantUsuariosModalModificarUsuario.requestFocus();

            }
        });
        comboNivelAcceso.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("Usuario")){
                    ocultarJefeArea();
                    mostrarUsuario();
                }else if(newValue.equals("Jefe de Area")){
                    labelAreas.setVisible(true);
                    labelAreasSeleccionada.setVisible(true);
                    mostrarJefeArea();
                    ocultarUsuario();
                }else if(newValue.equals("Administrador")){
                    labelAreas.setVisible(false);
                    labelAreasSeleccionada.setVisible(false);
                    ocultarJefeArea();
                    ocultarUsuario();
                }
				
				
			}
            
        });
    } 
    
    public void llenarDatos(){
        comboNivelAcceso.getSelectionModel().select(usuarioDatos.nivelAcceso);
        txtCorreo.setText(usuarioDatos.userID);
        txtCorreo.setDisable(true);
        txtNombre.setText(usuarioDatos.nombre);
        if(usuarioDatos.areas.size() == 1) {
            for(Area area : areas){
                if(area.areaID.equals(usuarioDatos.areas.get(0))) {
                    comboAreaAcceso.getSelectionModel().select(area);
                    ocultarJefeArea();
                    mostrarUsuario();
                    break;
                }
            }
        }else if(usuarioDatos.areas.size() > 1 ){
            mostrarJefeArea();
            ocultarUsuario();
        }else if(usuarioDatos.areas.isEmpty()){
            ocultarJefeArea();
            ocultarUsuario();
        }
    }
    public void mostrarUsuario(){

        comboAreaAcceso.setVisible(true);
    }
    public void ocultarUsuario(){
        comboAreaAcceso.setVisible(false);
    }
    public void mostrarJefeArea(){
            listAreas.setVisible(true);
            listAreasSeleccionadas.setVisible(true);
            btnAgregarArea.setVisible(true);
            btnQuitarArea.setVisible(true);
            
    }
    public void ocultarJefeArea(){
            listAreas.setVisible(false);
            listAreasSeleccionadas.setVisible(false);
            btnAgregarArea.setVisible(false);
            btnQuitarArea.setVisible(false);
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
            // usuario.areas List<String>
             String areaID = area.areaID;
            //   List<String> usuarioAreas = new ArrayList();
            System.out.print(usuarioDatos.areas);
            if(usuarioDatos.areas.contains(areaID))
            {
                System.out.println("agregando a la derecha");
                listAreasSeleccionadas.getItems().add(area);
            }else{
                System.out.println("agregando a la izquierda"); 
                //listaDeLaIzquierda
                listAreas.getItems().add(area);
            }
          }
    }
    
}
