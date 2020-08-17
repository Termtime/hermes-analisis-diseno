package com.unah.hermes;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

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
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Window;

public class MantUsuariosPage implements Initializable {

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
            // Alert alert = new Alert(AlertType.ERROR,"Debe seleccionar un Usuario antes", ButtonType.OK);
            // alert.showAndWait();
            Navigation.mostrarAlertError("Falta llenar algunos campos en el formulario", event);
            
        }
    }

    @FXML
    private void btnEliminarUsuarioClick(ActionEvent event) {
        if(tablaUSelectedItem == null ){
            Navigation.mostrarAlertError("Debe seleccionar un usuario activo", event);
            return;
        }
        db.deshabilitarUsuario(tablaUSelectedItem.uid, tablaUSelectedItem.getUserID().toString());
        llenarTabla();
    }
    @FXML
    private void btnReactivarUsuarioClick(ActionEvent event) {
        if(tablaUSelectedItem == null){
            Navigation.mostrarAlertError("Debe seleccionar un usuario desactivado", event);
            // return;
        }
        db.activarUsuario(tablaUSelectedItem.uid, tablaUSelectedItem.getUserID().toString());
        llenarTabla();
    }
    
    @FXML private void txtFiltroInput(KeyEvent event){                    
        tablaUsuarios.getItems().clear();
        List<User> usuariosFiltrados = new ArrayList<User>();
        for(User usuario: usuarios){
                if(usuario.nombre.toLowerCase().contains(txtFiltro.getText().toLowerCase()) || txtFiltro.getText().equals("")){
                    usuariosFiltrados.add(usuario);
            }      
        }
        tablaUsuarios.getItems().addAll(usuariosFiltrados);
    }
    
    @FXML Rectangle marco;
    @FXML TextField txtFiltro; 
    @FXML AnchorPane MantUsuario;
    @FXML TableView<User> tablaUsuarios;
    @FXML ImageView imagenUsuario;
    @FXML Button btnEliminarUsuario;
    @FXML Button btnReactivarUsuario;
    
    User tablaUSelectedItem;
    List<Area> areas = new ArrayList<Area>();
    List<QueryDocumentSnapshot> documentos;
    List<QueryDocumentSnapshot> docsAreas;
    ObservableList<User> usuarios = FXCollections.observableArrayList();
    FirebaseConnector db=FirebaseConnector.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
                if(newValue == null) return;
                tablaUSelectedItem = newValue;
                if(!tablaUSelectedItem.desactivado){
                    btnEliminarUsuario.setDisable(false); 
                    btnReactivarUsuario.setDisable(true); 
                }else{
                    btnEliminarUsuario.setDisable(true); 
                    btnReactivarUsuario.setDisable(false); 
                }
                
                Image image = db.downloadImage(FirestorageRoutes.USUARIOS, newValue.userID);
                if(image == null){
                    image =  new Image(getClass().getResourceAsStream("/images/usuarios.png"));
                }
                imagenUsuario.setImage(image);
                
                double aspectRatio = image.getWidth() / image.getHeight();
                double realWidth = Math.min(imagenUsuario.getFitWidth(), imagenUsuario.getFitHeight() * aspectRatio);
                double realHeight = Math.min(imagenUsuario.getFitHeight(), imagenUsuario.getFitWidth() / aspectRatio);
                imagenUsuario.setTranslateX(6);
                imagenUsuario.setTranslateY(6);

                marco.setWidth(realWidth+12);
                marco.setHeight(realHeight+12);
            }
        });
        tablaUsuarios.getItems().addListener(new ListChangeListener<User>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends User> change) {

                if (change.getList().size() != 0) {
                    if (tablaUSelectedItem == null)
                        return;

                    String userID = tablaUSelectedItem.userID;

                    int index = 0;
                    for (User requisicion : change.getList()) {
                        if (requisicion.userID.equals(userID)) {
                            tablaUsuarios.getSelectionModel().select(index);
                            break;
                        }
                        index++;
                    }
                }
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
    
    private void llenarTabla(){
        documentos = db.getAllDocumentsFrom(FirestoreRoutes.USUARIOS);
        docsAreas = db.getAllDocumentsFrom(FirestoreRoutes.AREAS);
        tablaUsuarios.getItems().clear();
        usuarios.clear();
        areas.clear();
        for(DocumentSnapshot doc : docsAreas){
            Area tmp = new Area(doc.getId(), doc.getString("Area"));
            areas.add(tmp);
        }

        for (DocumentSnapshot doc : documentos) {
            User tmp;
             if(doc.exists())
              {
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
                doc.getString("nivelAcceso"), doc.getString("uid"), areasConNombre, areasConID, doc.getBoolean("deshabilitado"));
                usuarios.add(tmp);
            }
        }
            tablaUsuarios.getItems().addAll(usuarios);
    }
}

