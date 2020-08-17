package com.unah.hermes;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


import com.unah.hermes.objects.Area;
import com.unah.hermes.objects.User;
import com.unah.hermes.provider.FirebaseConnector;
import com.unah.hermes.provider.FirestorageRoutes;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.FileChooser.ExtensionFilter;

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
    @FXML ImageView imagenUsuario;
    Window ventaPrincipalModificar;
    @FXML Button btnModificar;
    

    
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
    @FXML
    public void btnAgregarImagenUsuarioClick(ActionEvent event){
        System.out.println("se esta ejecutando");
        try {
        ////MANUAL DE COMO BAJAR Y SUBIR FOTOS
          //HANDLER DEL BOTON ESCOGER IMAGEN
          //crear un filechooser
          FileChooser fileChooser = new FileChooser();
          fileChooser.setTitle("Selecciona una nueva foto");
          //colocar filtros para solo permitir imagenes
          fileChooser.getExtensionFilters().add(
                  new ExtensionFilter("Image Files", "*.png", "*.jpg"));
          ///abrir file chooser
          // selectedFile declarenlo globalmente 
               
          selectedFile = fileChooser.showOpenDialog(ventaPrincipalModificar);
          if (selectedFile != null) {
              // si el archivo no es nulo, entonces crear un input stream y
              // popular el imageView con la imagen seleccionada
              InputStream is = new FileInputStream(selectedFile);
              imagenUsuario.setImage(new Image(is));
          }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error seleccionando foto");
        }

    }
    @FXML public void btnModificarClick(ActionEvent event) {
        List<Area> areasSeleccionadas = new ArrayList();
        if(comboNivelAcceso.getSelectionModel().getSelectedItem().equals("Usuario"))
        {
            areasSeleccionadas.add(comboAreaAcceso.getSelectionModel().getSelectedItem());
        }else if(comboNivelAcceso.getSelectionModel().getSelectedItem().equals("Jefe de Area")){
            areasSeleccionadas.addAll(listAreasSeleccionadas.getItems());
        }
        
        // validaciones de caja de texto
        if(!areasSeleccionadas.isEmpty()){
            if( txtNombre.getText().trim()!=null )

        //A LA HORA de AGREGAR y MODIFICAR    
        //cuando ya le den agregar o modificar
            //ustedes tendrian un ID de lo que ya crearon (el correo para el usuario,
            // el id del producto que regresa el metodo createDocument())

            db.modificarUsuario(usuarioDatos.uid,txtCorreo.getText(), txtNombre.getText(),
            comboNivelAcceso.getSelectionModel().getSelectedItem().toString(), areasSeleccionadas);

            if(!txtContrasena.getText().trim().isEmpty() && txtContrasena.getText().trim().length() >= 6)
                db.cambiarPassword(usuarioDatos.uid, txtContrasena.getText().trim());
            else
                Navigation.mostrarAlertError("La contraseña debe tener minimo 6 caracteres", event);
            //USUARIO EJEMPLO
            if (selectedFile != null) {
                db.uploadImage(FirestorageRoutes.USUARIOS, selectedFile, txtCorreo.getText());
            }
            Stage stage = (Stage) btnModificar.getScene().getWindow();
        stage.close();
        }
        else{
            //mostrar alert de que no se pudo ingresar
            Navigation.mostrarAlertError("Falta llenar algunos campos en el formulario", event);
        
        }

    }
    @FXML public void btnAgregarAreaClick(ActionEvent event) {
        //enviar areas seleccionadas a areas
        try{
            objetoBorrado = listAreas.getSelectionModel().getSelectedItem();
            int indice = listAreas.getSelectionModel().getSelectedIndex();
            listAreas.getItems().remove(indice);         
            listAreasSeleccionadas.getItems().add(objetoBorrado);     
            listAreas.getSelectionModel().clearSelection();
        }catch(Exception e){
            
        }
    
        
    }    
    @FXML public void btnQuitarAreaClick(ActionEvent event) {
        try{

            objetoABorrar = listAreasSeleccionadas.getSelectionModel().getSelectedItem();
            int indice = listAreasSeleccionadas.getSelectionModel().getSelectedIndex();
            listAreasSeleccionadas.getItems().remove(indice);         
            listAreas.getItems().add(objetoABorrar);    
            listAreasSeleccionadas.getSelectionModel().clearSelection();
        }catch(Exception e){

        }
        //enviar areas a areas seleccionadas 
        
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
    File selectedFile;
    Area objetoABorrar;
    Area  objetoBorrado;
    

   
    @FXML AnchorPane mantUsuariosModalModificarUsuario;
    @Override
    public void initialize(URL url,  ResourceBundle rb) {
        db = FirebaseConnector.getInstance();

        EventListeners.onWindowOpening(mantUsuariosModalModificarUsuario, new Function<Window,Void>(){

            @Override
            public Void apply(Window t) {
                ventaPrincipalModificar = t;
                ((Stage)t).resizableProperty().setValue(Boolean.FALSE);
                return null;
            }
        });
        
       
        mantUsuariosModalModificarUsuario.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                mantUsuariosModalModificarUsuario.requestFocus();

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
        //     //recibirias el usuario, tendrias un objeto -User usuario
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
