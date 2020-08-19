package com.unah.hermes;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.Window;

public class MantUsuariosModalAgregarUsuario implements Initializable {
    
    @FXML
    private void btnAgregarImagenUsuarioClick(ActionEvent event){
        try {
            //crear un filechooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Selecciona una foto");
            //colocar filtros para solo permitir imagenes
            fileChooser.getExtensionFilters().add(
                    new ExtensionFilter("Image Files", "*.png", "*.jpg"));
            ///abrir file chooser
            // selectedFile declarenlo globalmente 
                
            selectedFile = fileChooser.showOpenDialog(ventaPrincipal);
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

    @FXML
    private void btnAgregarClick(ActionEvent event) {
        //arriba de esto irian las validaciones
        List<Area> areasSeleccionadas = new ArrayList();
        if(comboNivelAcceso.getSelectionModel().getSelectedItem().equals("Usuario"))
        {
            areasSeleccionadas.add(comboAreaAcceso.getSelectionModel().getSelectedItem());
        }
        else if(comboNivelAcceso.getSelectionModel().getSelectedItem().equals("Jefe de Area")){
            areasSeleccionadas.addAll(listAreasSeleccionadas.getItems());
        }
        
        // validaciones de caja de texto
        if(txtCorreo.getText().trim()!=null && txtContrasena.getText().trim()!=null){
            if( txtNombre.getText().trim()!=null ){
            db.crearUsuario(txtCorreo.getText(), txtContrasena.getText(), txtNombre.getText(),
            comboNivelAcceso.getSelectionModel().getSelectedItem().toString(), areasSeleccionadas);
            
            if (selectedFile != null) {
                db.uploadImage(FirestorageRoutes.USUARIOS, selectedFile, txtCorreo.getText());
                
            }
            Navigation.mostrarAlertExito("Se lleno el formulario de correctamente", event);
            cerrarVentana();     
        }
            

           else{
            //mostrar alert de que no se pudo ingresar
            Navigation.mostrarAlertError("Falta llenar algunos campos en el formulario", event);
            
            
        }
        }
        
        
    }
    @FXML private void comboNivelAccesoClick(ActionEvent event) {
        if(comboNivelAcceso.getSelectionModel().isSelected(0)) {
            comboAreaAcceso.setVisible(false);
            listAreas.setVisible(false);
            listAreasSeleccionadas.setVisible(false);
            btnAgregarArea.setVisible(false);
            btnQuitarArea.setVisible(false);
            
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

    @FXML
    private void btnAgregarAreaClick(ActionEvent event) {
        //TODO IMPLEMENTAR ESTO
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

    @FXML
    private void btnQuitarAreaClick(ActionEvent event) {
        //TODO IMPLEMENTAR ESTO
        try{
            objetoABorrar = listAreasSeleccionadas.getSelectionModel().getSelectedItem();
            int indice = listAreasSeleccionadas.getSelectionModel().getSelectedIndex();
            listAreasSeleccionadas.getItems().remove(indice);         
            listAreas.getItems().add(objetoABorrar);    
            listAreasSeleccionadas.getSelectionModel().clearSelection();
        }catch(Exception e){

        }
    }

    @FXML
    private void btnCancelarClick(ActionEvent event) {
        cerrarVentana();
    }

    @FXML Button btnCancelar;
    @FXML ListView<Area>  listAreas;
    @FXML TextField txtNombre;
    @FXML ComboBox<String> comboNivelAcceso;
    @FXML ListView<Area> listAreasSeleccionadas;
    @FXML TextField txtCorreo;
    @FXML PasswordField txtContrasena;
    @FXML Button btnAgregarImagenUsuario;
    @FXML PasswordField txtConrfirmeContrasena;
    @FXML ComboBox<Area> comboAreaAcceso;
    @FXML Button btnAgregarArea;
    @FXML Button btnQuitarArea;
    @FXML Button btnAgregar;
    @FXML Label labelAreasSeleccionada;
    @FXML Label labelAreas;
    @FXML ImageView imagenUsuario;
    @FXML Rectangle marco;
    Window ventaPrincipal;
    @FXML Pane panelJefeUsuario;
    @FXML AnchorPane mantUsuariosModalAgregarUsuario;
    @FXML Label errorCorreo;
    @FXML Label errorPass;
    List<QueryDocumentSnapshot> docsAreas;
    List<QueryDocumentSnapshot> documentos;
    File selectedFile;
    FirebaseConnector db = FirebaseConnector.getInstance();
    ObservableList<Area> areas = FXCollections.observableArrayList();
    ObservableList<User> usuarios = FXCollections.observableArrayList();
    Area objetoABorrar;
    Area  objetoBorrado;

    @Override
    public void initialize(URL url,  ResourceBundle rb) {
        documentos = db.getAllDocumentsFrom(FirestoreRoutes.USUARIOS);
        docsAreas = db.getAllDocumentsFrom(FirestoreRoutes.AREAS);
        
        txtCorreo.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                errorCorreo.setVisible(false);
                errorCorreo.setText("");
                if (newValue.isEmpty()) {
                    btnAgregar.setDisable(true);
                    errorCorreo.setVisible(false);
                    errorCorreo.setText("Debe ingresar un correo válido");
                    return;
                }
                String regexString = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
                Pattern regex = Pattern.compile(regexString);
                Matcher matcher = regex.matcher(newValue);
                if (matcher.find()) {
                    if(!errorPass.isVisible()) btnAgregar.setDisable(false);
                    errorCorreo.setVisible(false);
                } else {
                    errorCorreo.setVisible(true);
                    btnAgregar.setDisable(true);
                    errorCorreo.setText("Debe ingresar un correo válido");
                }
            }
        });

        txtContrasena.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(txtContrasena.getText().length() >= 6 && txtContrasena.getText().equals(txtConrfirmeContrasena.getText())){
                    errorPass.setVisible(false);
                    if(!errorCorreo.isVisible()) btnAgregar.setDisable(false);
                }else{
                    errorPass.setVisible(true);
                    errorPass.setText("La contraseña debe tener 6 caracteres y ser iguales");
                }
            }
        });
        txtConrfirmeContrasena.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(txtContrasena.getText().length() >= 6 && txtContrasena.getText().equals(txtConrfirmeContrasena.getText())){
                    errorPass.setVisible(false);
                    if(!errorCorreo.isVisible()) btnAgregar.setDisable(false);
                }else{
                    errorPass.setVisible(true);
                    errorPass.setText("La contraseña debe tener 6 caracteres y ser iguales");
                }
            }
        });
        EventListeners.onWindowOpening(mantUsuariosModalAgregarUsuario, new Function<Window,Void>(){

            @Override
            public Void apply(Window t) {
                ventaPrincipal = t;
                ((Stage)t).resizableProperty().setValue(Boolean.FALSE);
                return null;
            }
        
        });
        mantUsuariosModalAgregarUsuario.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                mantUsuariosModalAgregarUsuario.requestFocus();
            }
        });

        EventListeners.onWindowOpened(mantUsuariosModalAgregarUsuario, new Function<Window,Void>(){
            @Override
            public Void apply(Window parent) {
                llenarCombo();       
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
                }else if(newValue.equals("Jefe de Area")){
                    labelAreas.setText("Area(s)");
                    labelAreasSeleccionada.setVisible(true);
                    mostrarJefeArea();
                    ocultarUsuario();
                }else if(newValue.equals("Administrador")){
                    labelAreas.setText("");
                    labelAreasSeleccionada.setVisible(false);
                    ocultarJefeArea();
                    ocultarUsuario();
                }
				
				
			}
            
        });        

        
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

        comboNivelAcceso.getSelectionModel().selectFirst();
        comboAreaAcceso.setVisible(true);
        listAreas.setVisible(false);
        listAreasSeleccionadas.setVisible(false);
        btnAgregarArea.setVisible(false);
        btnQuitarArea.setVisible(false);
        
        //llenado del combobox de areas
        comboAreaAcceso.getItems().addAll(areas);
        
        //llenado de la lista de la izquierda
        listAreas.getItems().addAll(areas);
    }
    
    private void cerrarVentana(){
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
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
   

}
