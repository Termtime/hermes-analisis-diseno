package com.unah.hermes;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.unah.hermes.objects.Area;
import com.unah.hermes.objects.Producto;
import com.unah.hermes.objects.User;
import com.unah.hermes.provider.FirebaseConnector;
import com.unah.hermes.provider.FirestoreRoutes;
import com.unah.hermes.utils.EventListeners;
import com.unah.hermes.utils.Navigation;

import org.apache.commons.lang3.ArrayUtils;

import java.util.function.Function;

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
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.stage.Modality;
import java.util.ArrayList;
import java.util.Collection;

public class MantUsuariosModalAgregarUsuario implements Initializable {
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
    @FXML Button btnAgregarImagenUsuario;
    @FXML
    PasswordField txtConrfirmeContrasena;
    @FXML
    ComboBox<Area> comboAreaAcceso;
    @FXML Button btnAgregarArea;
    @FXML Button btnQuitarArea;

    @FXML

    public void btnAgregarImagenUsuarioClick(ActionEvent event){

    }
    public void cerrarVentana(){
        Stage stage = (Stage) btnCancelar.getScene().getWindow();

        stage.close();
    }

    public void btnAgregarClick(ActionEvent event) {
        //arriba de esto irian las validaciones
        List<Area> areasSeleccionadas = new ArrayList();
        if(comboNivelAcceso.getSelectionModel().getSelectedItem().equals("Usuario"))
        {
            areasSeleccionadas.add(comboAreaAcceso.getSelectionModel().getSelectedItem());
        }else if(comboNivelAcceso.getSelectionModel().getSelectedItem().equals("Jefe de Area")){
            areasSeleccionadas.addAll(listAreasSeleccionadas.getItems());
        }
        // areas.addAll((Collection<? extends Area>)
        // comboNivelAcceso.getSelectionModel().getSelectedItem());

        if(db.crearUsuario(txtCorreo.getText(), txtContrasena.getText(), txtNombre.getText(),
            comboNivelAcceso.getSelectionModel().getSelectedItem().toString(), areasSeleccionadas))
        {
            System.out.println(listAreasSeleccionadas);

            //antes de esta instruccion mostrar dialogo de alerta de que se completo exitosamente
            cerrarVentana();
        }else{
            //mostrar alert de que no se pudo ingresar
        }

        
    }

    @FXML
    public void txtUsuarioInput(KeyEvent event) {
    }

    @FXML
    public void txtNombreInput(KeyEvent event) {
    }

    @FXML
    public void txtContrasenaInput(KeyEvent event) {
    }

    @FXML
    public void txtConfirmeContrasenaInput(KeyEvent event) {
    }

    @FXML
    public void comboAreaAccesoClick(ActionEvent event) {
    }

    @FXML
    public void btnAgregarAreaClick(ActionEvent event) {

    }

    @FXML
    public void btnQuitarAreaClick(ActionEvent event) {
    }

    @FXML
    public void comboNivelAccesoClick(ActionEvent event){       

        
        //  comboNivelAcceso.getSelectionModel().selectFirst();
        //  comboAreaAcceso.setVisible(true);
        //  listAreas.setVisible(true);
        //  listAreasSeleccionadas.setVisible(true);
        //  btnAgregarArea.setVisible(true);
        //  btnQuitarArea.setVisible(true);

        
    }

    @FXML
    public void txtCorreoInput(KeyEvent event) {
    }

    @FXML
    public javafx.scene.control.Button btnCancelar;

    @FXML
    public void btnCancelarClick(ActionEvent event) {

        Stage stage = (Stage) btnCancelar.getScene().getWindow();

        stage.close();

    }

    FirebaseConnector db = FirebaseConnector.getInstance();

    // List<QueryDocumentSnapshot> docsAreas =
    // db.getAllDocumentsFrom(FirestoreRoutes.AREAS);
    @FXML Pane panelJefeUsuario;
    @FXML AnchorPane mantUsuariosModalAgregarUsuario;

    ObservableList<Area> areas = FXCollections.observableArrayList();
    ObservableList<User> usuarios = FXCollections.observableArrayList();
    List<QueryDocumentSnapshot> docsAreas;
    List<QueryDocumentSnapshot> documentos;

    @Override
    public void initialize(URL url,  ResourceBundle rb) {
        documentos = db.getAllDocumentsFrom(FirestoreRoutes.USUARIOS);
        docsAreas = db.getAllDocumentsFrom(FirestoreRoutes.AREAS);
        llenarCombo();
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

        EventListeners.onWindowOpened(mantUsuariosModalAgregarUsuario, new Function<Window,Void>(){
            @Override
            public Void apply(Window parent) {
                       
                return null;                 
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
        // for (Area area : areas) {
        //     //recibirias el usuario, tendrias un objeto -User usuario
        //     //usuario.areas List<String>
        //     String areaID = area.areaID;
        //     List<String> usuarioAreas = new ArrayList();
        //     if(usuarioAreas.contains(areaID))
        //     {
        //         listAreasSeleccionadas.getItems().add(area);
        //     }else{
        //         //listaDeLaIzquierda
        //     }
        // }
        
        //llenado de la lista de la izquierda
        listAreas.getItems().addAll(areas);
    }


/*
    private void llenarComboBox(){   
        
       List<User> nivelAcceso = new ArrayList<User>();
        for(User usuario: usuarios){
            for (DocumentSnapshot doc : documentos) {
                Producto tmp;
                if(doc.exists()){
                    tmp = new User(doc.getId(), doc.getString("Nombre"), doc.getString("nivelAcceso"), doc.getString("Categoria"));
                    int cont=0;
                    for(nivelAcceso acceso: comboNivelAcceso)
                    {
                        if(tmp.categoria.equals(comboNivelAcceso.get(i).categoria))
                        {
                            cont++;
                        }
                    }
                    if(cont==0)
                        comboCategoria.getItems().add(tmp.getCategoria());
                }
            } 
                   
    }      
}
comboNivelAcceso.getItems().addListener((ListChangeListener<? super String>) nivelAcceso);
    


    private void llenarComboBox(){
        System.out.println("llenarcombo");
        
        List<User> comboNivelAcceso = new ArrayList<User>();
        for(DocumentSnapshot doc : docsAreas){
            Area tmp = new Area(doc.getId(), doc.getString("Area"));
            areas.add(tmp);
        }
        for (DocumentSnapshot doc : documentos) {
            User tmp;
            if(doc.exists()){
                List<String> arregloIDAreas = (List<String>) doc.get("areas");
                List<String> areasConNombre = new ArrayList();
                for(Area area : areas){
                    for(String areaID : arregloIDAreas){
                        if(areaID.equals(area.areaID.trim()))
                        {
                            areasConNombre.add(area.nombre);
                            break;
                        }   
                    }
                }
                tmp = new User(doc.getId(), doc.getString("Nombre"),doc.getString("nivelAcceso"),areasConNombre);
                usuarios.add(tmp);
                int cont=0;
                for(int i=0;i<comboNivelAcceso.size();i++)
                {
                    if(tmp.nivelAcceso.equals(comboNivelAcceso.get(i).nivelAcceso))
                    {
                        cont++;
                    }
                }
                if(cont==0)
                 System.out.println(usuarios);
                 System.out.println("!!!");

                
            }
        } 
    }*/
    
}
