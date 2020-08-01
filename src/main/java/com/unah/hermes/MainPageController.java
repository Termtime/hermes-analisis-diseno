package com.unah.hermes;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FirestoreException;
import com.unah.hermes.objects.Producto;
import com.unah.hermes.objects.Requisicion;
import com.unah.hermes.objects.RequisicionEntregadaRow;
import com.unah.hermes.objects.RequisicionRow;
import com.unah.hermes.provider.FirebaseConnector;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.stage.Window;
import javafx.stage.Modality;

public class MainPageController implements Initializable {
    
    @FXML public void menuBtnCerrarClick(ActionEvent event){

    }
    @FXML public void menuBtnImprimirClick(ActionEvent event){
        
    }
    @FXML public void menuBtnImprimirMensualClick(ActionEvent event){
        
    }
    @FXML public void menuBtnEntregarReqClick(ActionEvent event){
        
    }
    @FXML public void menuBtnDenegReqClick(ActionEvent event){
        
    }
    @FXML public void menuBtnOcultarReqClick(ActionEvent event){
        
    }
    @FXML public void menuBtnMantUsuariosClick(ActionEvent event){
        pushRoute("MantUsuariosPage", event, false, true);
    }
    @FXML public void menuBtnMantProductosClick(ActionEvent event){
        pushRoute("MantProductosPage", event, false, true);
    }
    @FXML public void menuBtnMantAreasClick(ActionEvent event){
        pushRoute("MantAreasPage", event, false, true);
    }

    @FXML public void btnEntregarClick(ActionEvent event){

    }
    @FXML public void btnDenegarClick(ActionEvent event){

    }
    FirebaseConnector db;

    
    public void pushRoute(String pageName, ActionEvent event, Boolean hide, Boolean modal)
    {
        Parent root;
        Window parentStage;
        try{
            parentStage = ((Node)(event.getSource())).getScene().getWindow();
        }catch(Exception e)
        {
            try{
                //intentar obtener el parent de un MenuItem
                parentStage = ((MenuItem)event.getTarget()).getParentPopup().getOwnerWindow();
            }catch(Exception ex){
                //fallar suavemente
                ex.printStackTrace();
                parentStage = null;
            }
        }
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/" + pageName + ".fxml"));
            Stage stage = new Stage();
            stage.setTitle("My New Stage Title");
            stage.setScene(new Scene(root));
            if(modal) {
                stage.initOwner(parentStage);
                stage.initModality(Modality.APPLICATION_MODAL); 
                stage.showAndWait();
            } else {
                stage.show();    
            }
            // Hide this current window (if this is what you want)
            if(hide) parentStage.hide();
            
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    //listViews
    @FXML ListView<Requisicion> listaRQP;
    @FXML ListView<Requisicion> listaRQE;
    @FXML ListView<Requisicion> listaRQD;
    //labels
        //ID
        @FXML Label lblReqIDP;
        @FXML Label lblReqIDD;
        @FXML Label lblReqIDE;
        
        //ESTADO
        @FXML Label lblEstadoP;
        @FXML Label lblEstadoD;
        @FXML Label lblEstadoE;
        
        //FECHA
        @FXML Label lblFechaP;
        @FXML Label lblFechaD;
        @FXML Label lblFechaE;
        
        //HORA
        @FXML Label lblHoraP;
        @FXML Label lblHoraD;
        @FXML Label lblHoraE;
        
        //AREA
        @FXML Label lblAreaP;
        @FXML Label lblAreaD;
        @FXML Label lblAreaE;
        
        //SOLICITANTE
        @FXML Label lblSolicitanteP;
        @FXML Label lblSolicitanteD;
        @FXML Label lblSolicitanteE;

    //Tablas
    @FXML TableView<RequisicionRow> tablaP;
    @FXML TableView<RequisicionRow> tablaD;
    @FXML TableView<RequisicionEntregadaRow> tablaE;
    Requisicion tablaPSelectedItem;
    Requisicion tablaDSelectedItem;
    Requisicion tablaESelectedItem;
    public static ObservableList<Requisicion> RequisicionesPendientes = FXCollections.observableArrayList();
    public static ObservableList<Requisicion> RequisicionesEntregadas = FXCollections.observableArrayList();
    public static ObservableList<Requisicion> RequisicionesDenegadas = FXCollections.observableArrayList();
    ObservableList<Producto> empty = FXCollections.observableArrayList();

    @FXML
    private void ocultarRequisicionesSeleccionadas(ActionEvent event) {
        
    }
    @FXML
    private void imprimirReqSeleccionada(ActionEvent event) {
        
    }

    @FXML
    private AnchorPane mainPage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //crear los listeners para los datos de firebase
        //TODO requisiciones denegadas, requisiciones entregadas
        db = FirebaseConnector.getInstance();
        db.iniciarListenerRequisiciones();
        listaRQE.setItems(RequisicionesEntregadas);
        listaRQP.setItems(RequisicionesPendientes);
        listaRQD.setItems(RequisicionesDenegadas);
        
        
        iniciarEstructuraTablas();
        //agregar el listener de cambio para cuando se cambie la seleccion
        listaRQP.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Requisicion>(){
            @Override
            public void changed(ObservableValue<? extends Requisicion> observable, Requisicion oldValue, Requisicion newValue) {
                tablaPSelectedItem = newValue;
                System.out.println(newValue);
                popularTablaRequisicionesPDConProductos(tablaP, newValue.productos);
                lblReqIDP.setText(newValue.reqID);
                lblEstadoP.setText(newValue.estado);
                lblFechaP.setText(newValue.fechaString);
                lblHoraP.setText(newValue.hora);
                lblAreaP.setText(newValue.area);
                lblSolicitanteP.setText(newValue.solicitante);
            }
        });

        listaRQE.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Requisicion>(){
            
            @Override
            public void changed(ObservableValue<? extends Requisicion> observable, Requisicion oldValue, Requisicion newValue) {
                tablaESelectedItem = newValue;
                System.out.println(newValue);
                popularTablaRequisicionesEntregadas(tablaE, newValue.productos);
                lblReqIDE.setText(newValue.reqID);
                lblEstadoE.setText(newValue.estado);
                lblFechaE.setText(newValue.fechaString);
                lblHoraE.setText(newValue.hora);
                lblAreaE.setText(newValue.area);
                lblSolicitanteE.setText(newValue.solicitante);
            }
        });

        listaRQD.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Requisicion>(){

            @Override
            public void changed(ObservableValue<? extends Requisicion> observable, Requisicion oldValue, Requisicion newValue) {
                System.out.println(newValue);
                tablaDSelectedItem = newValue;
                lblReqIDD.setText(newValue.reqID);
                popularTablaRequisicionesPDConProductos(tablaD, newValue.productos);
                lblEstadoD.setText(newValue.estado);
                lblFechaD.setText(newValue.fechaString);
                lblHoraD.setText(newValue.hora);
                lblAreaD.setText(newValue.area);
                lblSolicitanteD.setText(newValue.solicitante);
            }

        });    
    }

    private void iniciarEstructuraTablas(){

        //Tabla de Requisiciones pendientes
        tablaP.getItems().clear();
        tablaP.getColumns().clear();
        TableColumn columnaProductoP = new TableColumn<>("Producto");
        columnaProductoP.setCellValueFactory(new PropertyValueFactory<>("producto"));

        TableColumn columnaUnidadP = new TableColumn<>("Unidad");
        columnaUnidadP.setCellValueFactory(new PropertyValueFactory<>("unidad"));

        TableColumn columnaCantidadPedidaP = new TableColumn<>("Cantidad");
        columnaCantidadPedidaP.setCellValueFactory(new PropertyValueFactory<>("cantidadPedida"));
        tablaP.getColumns().addAll(columnaProductoP, columnaUnidadP, columnaCantidadPedidaP);

        //Tabla de Requisiciones entregadas
        tablaE.getItems().clear();
        tablaE.getColumns().clear();
        TableColumn columnaProductoE = new TableColumn<>("Producto");
        columnaProductoE.setCellValueFactory(new PropertyValueFactory<>("producto"));
        // columnaProductoE.setPrefWidth(tablaE.getWidth()*0.35);

        TableColumn columnaUnidadE = new TableColumn<>("Unidad");
        columnaUnidadE.setCellValueFactory(new PropertyValueFactory<>("unidad"));
        // columnaUnidadE.setPrefWidth(tablaE.getWidth()*0.15);

        TableColumn columnaCantidadPedidaE = new TableColumn<>("Cant. Pedida");
        columnaCantidadPedidaE.setCellValueFactory(new PropertyValueFactory<>("cantidadPedida"));
        // columnaCantidadPedidaE.setPrefWidth(tablaE.getWidth()*0.105);

        TableColumn columnaCantidadEntregadaE = new TableColumn<>("Cant. Entregada");
        columnaCantidadEntregadaE.setCellValueFactory(new PropertyValueFactory<>("cantidadEntregada"));
        // columnaCantidadEntregadaE.setPrefWidth(tablaE.getWidth()*0.105);

        TableColumn columnaCantidadPendienteE = new TableColumn<>("Cant. Pendiente");
        columnaCantidadPendienteE.setCellValueFactory(new PropertyValueFactory<>("cantidadPendiente"));
        // columnaCantidadPendienteE.setPrefWidth(tablaE.getWidth()*0.105);

        TableColumn columnaComentariosE = new TableColumn<>("Comentarios");
        columnaComentariosE.setCellValueFactory(new PropertyValueFactory<>("comentario"));
        // columnaComentariosE.setPrefWidth(tablaE.getWidth()*0.18);

        tablaE.getColumns().addAll(columnaProductoE, columnaUnidadE, columnaCantidadPedidaE, columnaCantidadEntregadaE, columnaCantidadPendienteE, columnaComentariosE);

        //Tabla de Requisiciones denegadas
        tablaD.getItems().clear();
        tablaD.getColumns().clear();
        TableColumn columnaProductoD = new TableColumn<>("Producto");
        columnaProductoD.setCellValueFactory(new PropertyValueFactory<>("producto"));

        TableColumn columnaUnidadD = new TableColumn<>("Unidad");
        columnaUnidadD.setCellValueFactory(new PropertyValueFactory<>("unidad"));

        TableColumn columnaCantidadPedidaD = new TableColumn<>("Cantidad");
        columnaCantidadPedidaD.setCellValueFactory(new PropertyValueFactory<>("cantidadPedida"));
        tablaD.getColumns().addAll(columnaProductoD, columnaUnidadD, columnaCantidadPedidaD);
    }

    private void popularTablaRequisicionesPDConProductos(TableView<RequisicionRow> tabla, ObservableList<Producto> productos) {
        try{
            tabla.getItems().clear();
            for (Producto producto : productos) {
                RequisicionRow row = new RequisicionRow(producto.nombre, producto.unidad, producto.cantPedida);
                tabla.getItems().add(row);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void popularTablaRequisicionesEntregadas(TableView<RequisicionEntregadaRow> tabla, ObservableList<Producto> productos) {
        try{
            // tabla.getItems().clear();
            for (Producto producto : productos) {
                RequisicionEntregadaRow row = new RequisicionEntregadaRow(producto.nombre, producto.unidad, producto.cantPedida, producto.cantEntregada, producto.cantPendiente, producto.comentario);
                tabla.getItems().add(row);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
            
    }

    
}