package com.unah.hermes;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Function;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.ListenerRegistration;
import com.google.cloud.firestore.ListenerRegistration;
import com.unah.hermes.provider.FirebaseConnector;
import com.unah.hermes.provider.FirestorageRoutes;
import com.unah.hermes.provider.FirestoreRoutes;
import com.unah.hermes.utils.EventListeners;
import com.unah.hermes.utils.Navigation;

import io.opencensus.metrics.export.Summary.Snapshot;

import com.google.cloud.firestore.ListenerRegistration;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FirestoreException;
import com.unah.hermes.objects.User;
import com.unah.hermes.utils.Navigation;
import com.unah.hermes.utils.EventListeners;
import com.google.cloud.firestore.EventListener;
import com.unah.hermes.objects.Area;
import com.unah.hermes.objects.Requisicion;
import com.unah.hermes.provider.FirebaseConnector;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
//import jdk.nashorn.internal.runtime.ListAdapter;
import javafx.stage.Modality;


import javafx.fxml.Initializable;

public class MantUsuariosPage implements Initializable {

    @FXML
    TableView<User> tablaUsuarios;
    @FXML ImageView imagenUsuario;

    @FXML
    private void btnAgregarUsuarioClick(ActionEvent event) {
        Navigation.pushRoute("MantUsuariosModalAgregarUsuario", event, false, true);
        llenarTabla();
    }

    @FXML
    private void btnModificarUsuarioClick(ActionEvent event) {
        if(tablaUSelectedItem != null)
            Navigation.pushRouteWithParameter("MantUsuariosModalModificarUsuario", event, false, true, MantUsuariosModalModificarUsuario.class, tablaUSelectedItem );
        else{
            Alert alert = new Alert(AlertType.ERROR,"Debe seleccionar un Usuario antes", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    private void btnEliminarUsuarioClick(ActionEvent event) {
        db.eliminarUsuario(tablaUSelectedItem.uid, tablaUSelectedItem.getUserID().toString());
        llenarTabla();
    }

    @FXML
    private void btnPermisosClick(ActionEvent event) {

    }
    
    @FXML TextField txtFiltro; 
   
    @FXML private void txtFiltroInput(KeyEvent event){                    
        tablaUsuarios.getItems().clear();
        List<User> usuariosFiltrados = new ArrayList<User>();
        for(User usuario: usuarios){
            for(Area area : areas){
                if(usuario.nombre.toLowerCase().contains(txtFiltro.getText().toLowerCase()) || txtFiltro.getText().equals("")){
                    usuariosFiltrados.add(usuario);
                    break;
                }
            }      
        }
        tablaUsuarios.getItems().addAll(usuariosFiltrados);
    }
    
    @FXML
    AnchorPane MantUsuario;

    FirebaseConnector db=FirebaseConnector.getInstance(); ;   
    
    User tablaUSelectedItem;
    List<Area> areas = new ArrayList<Area>();
    ObservableList<User> usuarios = FXCollections.observableArrayList();
    List<QueryDocumentSnapshot> documentos;
    List<QueryDocumentSnapshot> docsAreas;
    
    public void llenarTabla(){
        documentos = db.getAllDocumentsFrom(FirestoreRoutes.USUARIOS);
        docsAreas = db.getAllDocumentsFrom(FirestoreRoutes.AREAS);
        tablaUsuarios.getItems().clear();
        usuarios.clear();
        for(DocumentSnapshot doc : docsAreas){
            Area tmp = new Area(doc.getId(), doc.getString("Area"));
            areas.add(tmp);
        }

        for (DocumentSnapshot doc : documentos) {
            User tmp;
             if(doc.exists())
              {
            //     if(doc.getBoolean("inhabilitado") == null) continue;
                
                List<String> arregloIDAreas = (List<String>) doc.get("areas");
                List<String> areasConNombre = new ArrayList();
                //agregar areas con ID
                List<String> areasConID = new ArrayList();  

                    for(Area area : areas){
                    for(String areaID : arregloIDAreas){
                        if(areaID.equals(area.areaID.trim()))
                        {
                            areasConNombre.add(area.nombre);
                            areasConID.add(area.areaID);
                            break;
                        }   
                    }
                }   
                tmp = new User(doc.getId(), doc.getString("Nombre"),
                doc.getString("nivelAcceso"), doc.getString("uid"), areasConNombre, areasConID);
                usuarios.add(tmp);
            }
        }
            tablaUsuarios.getItems().addAll(usuarios);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // ////MANUAL DE COMO BAJAR Y SUBIR FOTOS
        // //HANDLER DEL BOTON ESCOGER IMAGEN
        // //crear un filechooser
        // FileChooser fileChooser = new FileChooser();
        // fileChooser.setTitle("Selecciona una foto");
        // //colocar filtros para solo permitir imagenes
        // fileChooser.getExtensionFilters().addAll(
        //         new ExtensionFilter("Image Files", "*.png", "*.jpg"),
        // ///abrir file chooser
        // // selectedFile declarenlo globalmente 
        // //     File selectedFile
        // selectedFile = fileChooser.showOpenDialog(mainStage);
        // if (selectedFile != null) {
        //     // si el archivo no es nulo, entonces crear un input stream y
        //     // popular el imageView con la imagen seleccionada
        //     InputStream is = new FileInputStream(selectedFile);
        //     nombreDeTuImageView.setImage(new Image(is));
        // }


        // //A LA HORA de AGREGAR y MODIFICAR    
        // //cuando ya le den agregar o modificar
        //     //ustedes tendrian un ID de lo que ya crearon (el correo para el usuario,
        //     // el id del producto que regresa el metodo createDocument() )

        //     //USUARIO EJEMPLO
        //     if (selectedFile != null) {
        //         uploadImage(FirestorageRoutes.USUARIOS,selectedFile, txtCorreo.getText());
        //     }
        //     //PRODUCTO EJEMPLO
        //     String idDelProductoRecienCreado = db.createDocument("Productos", data);
        //     if (selectedFile != null) {

        //         uploadImage(FirestorageRoutes.PRODUCTOS, selectedFile, idDelProductoRecienCreado);
        //     }
 
        // //PARA BAJAR LA IMAGEN
        // //aqui tendrian el usuario o producto que seleccionaron que en el handler de seleccion se llama newValue
        // //esto va dentro del TU_TABLA.getSelectionModel().selectedItemProperty().addListener{blablabla}
        //     if(newValue == null) return;
        //     Image imagenDeUsuario/Producto = downloadImage(FirestorageRoutes.USUARIOS, newValue.ubicacionFoto);
        //     nombreDeTuImageView.setImage(imagenDeUsuario/Producto);
        MantUsuario.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
                MantUsuario.requestFocus();
            }
        });
        EventListeners.onWindowOpened(MantUsuario, new Function<Window,Void>(){
            @Override
            public Void apply(Window parent) {
                iniciarEstructuraTablas();     
                llenarTabla();

                return null;
            }

            


        });

        MantUsuario.widthProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                recalcularColumnWidth();
               
            }
        });
        tablaUsuarios.widthProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                recalcularColumnWidth();
            }
        });

         tablaUsuarios.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>(){
             @Override
            public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                tablaUSelectedItem = newValue;
                
            //PARA BAJAR LA IMAGEN
            //aqui tendrian el usuario o producto que seleccionaron que en el handler de seleccion se llama newValue
            //esto va dentro del TU_TABLA.getSelectionModel().selectedItemProperty().addListener{blablabla}
                if(newValue == null) return;
                Image imagenDeUsuario = db.downloadImage(FirestorageRoutes.USUARIOS, newValue.userID);
                imagenUsuario.setImage(imagenDeUsuario);
            }
        });
         MantUsuario.widthProperty().addListener(new ChangeListener<Number>(){
             @Override
             public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                 recalcularColumnWidth();
             }
         });

  
    }
    
    
       
    
  
    private void recalcularColumnWidth(){
                       
        ObservableList columnasUsuario = tablaUsuarios.getColumns();

        ((TableColumn)( columnasUsuario.get(0) )).setPrefWidth(tablaUsuarios.getWidth()*0.20);
        ((TableColumn)( columnasUsuario.get(1) )).setPrefWidth(tablaUsuarios.getWidth()*0.38);
        ((TableColumn)( columnasUsuario.get(2) )).setPrefWidth(tablaUsuarios.getWidth()*0.15);
        ((TableColumn)( columnasUsuario.get(3) )).setPrefWidth(tablaUsuarios.getWidth()*0.25);
        
        
    }    

    

    private void iniciarEstructuraTablas(){
       // Colocar nombre de columnas y obtener campos

        //Tabla de usuarios
        tablaUsuarios.getItems().clear();
        tablaUsuarios.getColumns().clear();
        TableColumn columnaCodigo = new TableColumn<>("Correo");
        columnaCodigo.setCellValueFactory(new PropertyValueFactory<>("userID"));
        columnaCodigo.setPrefWidth(tablaUsuarios.getWidth()*0.25);

        TableColumn columnaUsuario = new TableColumn<>("Nombre");
        columnaUsuario.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaUsuario.setPrefWidth(tablaUsuarios.getWidth()*0.25);

        TableColumn columnaNivelAcceso = new TableColumn<>("Nivel de acceso");
        columnaNivelAcceso.setCellValueFactory(new PropertyValueFactory<>("nivelAcceso"));
        columnaNivelAcceso.setPrefWidth(tablaUsuarios.getWidth()*0.20);

        TableColumn columnaArea = new TableColumn<>("Areas");
        columnaArea.setCellValueFactory(new PropertyValueFactory<>("stringDeArea"));
        columnaArea.setPrefWidth(tablaUsuarios.getWidth()*0.28);

        
        tablaUsuarios.getColumns().addAll(columnaCodigo, columnaUsuario, columnaNivelAcceso, columnaArea);

       
    }

    // private void popularTablaMantUsuarios(ListView lista, ObservableList<User> Usuarios) {
    //     try{
    //         ListAdaptertItems().clear();
    //         for (User usuario : Usuarios) {
    //             User row = new User(usuario.userID, usuario.nombre, usuario.nivelAcceso,usuario.areas);
    //             lista.getItems().add(row);
    //         }
    //     }catch(Exception e){
    //         e.printStackTrace();
    //     }
    // }


  }

