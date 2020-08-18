package com.unah.hermes;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

//import com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Label;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.unah.hermes.objects.Area;
import com.unah.hermes.objects.User;
import com.unah.hermes.provider.FirebaseConnector;
import com.unah.hermes.provider.FirestorageRoutes;
import com.unah.hermes.provider.FirestoreRoutes;
import com.unah.hermes.utils.EventListeners;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.Window;

public class MantUsuariosModalModificarUsuario implements Initializable {
    
    
    @FXML private void btnAgregarImagenUsuarioClick(ActionEvent event){
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
                Image image = new Image(is);
                imagenUsuario.setImage(image);
                
                double aspectRatio = image.getWidth() / image.getHeight();
                double realWidth = Math.min(imagenUsuario.getFitWidth(), imagenUsuario.getFitHeight() * aspectRatio);
                double realHeight = Math.min(imagenUsuario.getFitHeight(), imagenUsuario.getFitWidth() / aspectRatio);
                imagenUsuario.setTranslateX(6);
                imagenUsuario.setTranslateY(6);

                marco.setWidth(realWidth+12);
                marco.setHeight(realHeight+12);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error seleccionando foto");
        }

    }
    @FXML private void btnModificarClick(ActionEvent event) {
        List<Area> areasSeleccionadas = new ArrayList();
        if(comboNivelAcceso.getSelectionModel().getSelectedItem().equals("Usuario"))
        {
            areasSeleccionadas.add(comboAreaAcceso.getSelectionModel().getSelectedItem());
        }
         if(comboNivelAcceso.getSelectionModel().getSelectedItem().equals("Jefe de Area")){
            areasSeleccionadas.addAll(listAreasSeleccionadas.getItems());
        }
        
        // validaciones de caja de texto
        if(!areasSeleccionadas.isEmpty()){
            if( txtNombre.getText().trim()!=null ){
            db.modificarUsuario(usuarioDatos.uid,txtCorreo.getText(), txtNombre.getText(),
            comboNivelAcceso.getSelectionModel().getSelectedItem().toString(), areasSeleccionadas);
            System.out.println("problemas con uid");
            System.out.println(usuarioDatos.uid);
            System.out.println("problemas con uid");

            if(!txtContrasena.getText().trim().isEmpty() && txtContrasena.getText().trim().length() >= 6){
                db.cambiarPassword(usuarioDatos.uid, txtContrasena.getText().trim());
                cerrarVentana();
                Navigation.mostrarAlertConfirmacion("Se lleno el formulario de correcta", event);
            }
              else
                Navigation.mostrarAlertError("La contraseña debe tener minimo 6 caracteres", event);
            }

            if (selectedFile != null) {
                db.uploadImage(FirestorageRoutes.USUARIOS, selectedFile, txtCorreo.getText());
            }
            
            
        }
        
        if(txtNombre.getText().trim()==null){
            //mostrar alert de que no se pudo ingresar
            Navigation.mostrarAlertError("Falta llenar algunos campos en el formulario", event);
        
        }

    }
    @FXML private void btnAgregarAreaClick(ActionEvent event) {
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
    @FXML private void btnQuitarAreaClick(ActionEvent event) {
        try{
            objetoABorrar = listAreasSeleccionadas.getSelectionModel().getSelectedItem();
            int indice = listAreasSeleccionadas.getSelectionModel().getSelectedIndex();
            listAreasSeleccionadas.getItems().remove(indice);         
            listAreas.getItems().add(objetoABorrar);    
            listAreasSeleccionadas.getSelectionModel().clearSelection();
        }catch(Exception e){

        }
    }
    @FXML private void comboNivelAccesoClick(ActionEvent event) {
        //validaciones al momento de seleccioanr el combobox
        if(comboNivelAcceso.getSelectionModel().isSelected(0)) {
           
            ocultarJefeArea();
            mostrarUsuario();
            labelAreas.setText("Area");
            labelAreasSeleccionada.setVisible(false);
     
            
        }
        if(comboNivelAcceso.getSelectionModel().isSelected(1)) {
             comboAreaAcceso.setVisible(true);
             listAreas.setVisible(true);
             listAreasSeleccionadas.setVisible(true);
             btnAgregarArea.setVisible(true);
             btnQuitarArea.setVisible(true);
        }
        if(comboNivelAcceso.getSelectionModel().isSelected(2)) {
            comboAreaAcceso.setVisible(false);
            listAreas.setVisible(false);
            listAreasSeleccionadas.setVisible(false);
            btnAgregarArea.setVisible(false);
            btnQuitarArea.setVisible(false);
        }
       
    }
    @FXML private void btnCancelarClick(ActionEvent event) {
        cerrarVentana();
    }
    
    @FXML Button btnCancelar;
    @FXML Pane panelJefeUsuario;
    @FXML AnchorPane mantUsuariosModalAgregarUsuario;
    @FXML ListView<Area>  listAreas;
    @FXML TextField txtNombre;
    @FXML ComboBox<String> comboNivelAcceso;
    @FXML ListView<Area> listAreasSeleccionadas;
    @FXML TextField txtCorreo;
    @FXML PasswordField txtContrasena;
    @FXML PasswordField txtConrfirmeContrasena;
    @FXML ComboBox<Area> comboAreaAcceso;
    @FXML Button btnAgregarArea;
    @FXML Button btnQuitarArea;
    @FXML Label labelAreasSeleccionada;
    @FXML Label labelAreas;
    @FXML ImageView imagenUsuario;
    @FXML Button btnModificar;
    @FXML Rectangle marco;
    @FXML AnchorPane mantUsuariosModalModificarUsuario;
    @FXML Label errorPass;

    Window ventaPrincipalModificar;
    FirebaseConnector db;
    User usuarioDatos;
    List<QueryDocumentSnapshot> docsAreas;
    File selectedFile;
    Area objetoABorrar;
    Area  objetoBorrado;
    
    ObservableList<Area> areas = FXCollections.observableArrayList();
    ObservableList<User> usuarios = FXCollections.observableArrayList();

    public void initData(Object data){
        usuarioDatos = (User) data; 
        
    }

    @Override
    public void initialize(URL url,  ResourceBundle rb) {
        db = FirebaseConnector.getInstance();
        txtContrasena.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.isEmpty()){
                    btnModificar.setDisable(false);
                    return;
                }
                if(newValue.length() >= 6){
                    errorPass.setVisible(false);
                    btnModificar.setDisable(false);
                 }  else{
                    btnModificar.setDisable(true);
                    errorPass.setVisible(true);
                    errorPass.setText("La contraseña debe tener 6 caracteres");
                }
            }
        });
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
                    labelAreas.setText("Area");
                    labelAreasSeleccionada.setVisible(false);
                }if(newValue.equals("Jefe de Area")){
                    labelAreas.setText("Area(s)");
                    labelAreasSeleccionada.setVisible(true);
                    mostrarJefeArea();
                    ocultarUsuario();
                } if(newValue.equals("Administrador")){
                    labelAreas.setText("");
                    labelAreasSeleccionada.setVisible(false);
                    ocultarJefeArea();
                    ocultarUsuario();
                }
				
				
			}
            
        });

        
    } 
   

    private void llenarDatos(){
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
    private void mostrarUsuario(){

        comboAreaAcceso.setVisible(true);
    }
    private void ocultarUsuario(){
        comboAreaAcceso.setVisible(false);
    }
    private void mostrarJefeArea(){
            listAreas.setVisible(true);
            listAreasSeleccionadas.setVisible(true);
            btnAgregarArea.setVisible(true);
            btnQuitarArea.setVisible(true);
            
    }
    private void ocultarJefeArea(){
            listAreas.setVisible(false);
            listAreasSeleccionadas.setVisible(false);
            btnAgregarArea.setVisible(false);
            btnQuitarArea.setVisible(false);
    }
    private void llenarCombo(){
        //procesar datos de firebase
        for (DocumentSnapshot doc : docsAreas) {
            Area tmp = new Area(doc.getId(), doc.getString("Area"));
            areas.add(tmp);
        }
          
        //llenado del combobox de nivel de acceso
        comboNivelAcceso.getItems().add("Usuario");
        comboNivelAcceso.getItems().add("Jefe de Area");
        comboNivelAcceso.getItems().add("Administrador");
        
        // comboNivelAcceso.getSelectionModel().selectFirst();
        // comboAreaAcceso.setVisible(true);
        // listAreas.setVisible(false);
        // listAreasSeleccionadas.setVisible(false);
        // btnAgregarArea.setVisible(false);
        // btnQuitarArea.setVisible(false);
        //llenado del combobox de areas
        comboAreaAcceso.getItems().addAll(areas);
        //para la ventana de modificar, para llenar la lista de la derecha y la izquierda
        for (Area area : areas) {
        //     //recibirias el usuario, tendrias un objeto -User usuario
            // usuario.areas List<String>
             String areaID = area.areaID;
           
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
    //metodo para cerrar ventana
    private void cerrarVentana(){
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    //ObservableList<User> usuarios = FXCollections.observableArrayList();
    private void llenarTabla(){
       
        //docsAreas = db.getAllDocumentsFrom(FirestoreRoutes.AREAS);
        //usuarios.clear();
        //areas.clear();
        List<User> usuariosuid = new ArrayList<User>();
        for(User usuario: usuarios){
                    usuariosuid.add(usuario);
            }  
        
            
        } 
    
}
