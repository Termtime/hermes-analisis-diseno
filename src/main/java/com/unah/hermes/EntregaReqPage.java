package com.unah.hermes;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

import com.unah.hermes.objects.Producto;
import com.unah.hermes.objects.Requisicion;
import com.unah.hermes.objects.RequisicionEntregadaRow;
import com.unah.hermes.provider.FirebaseConnector;
import com.unah.hermes.utils.EventListeners;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

public class EntregaReqPage implements Initializable {
    @FXML public javafx.scene.control.Button btnCancelar;
    @FXML public javafx.scene.control.Button btnTerminarEntrega;
    @FXML TableView<RequisicionEntregadaRow> tablaVistaPrevia;
    @FXML Label labelID;
    @FXML Label labelHora;
    @FXML Label labelFecha;
    @FXML Label labelArea;
    @FXML Label labelSolicitante;


    @FXML public void btnTerminarEntregaClick(ActionEvent event){}


    @FXML public void btnCancelarClick(ActionEvent event) {

        Stage stage = (Stage) btnCancelar.getScene().getWindow();

        stage.close();
         }

    @FXML AnchorPane EntregaReqPage;

       FirebaseConnector db;
        Requisicion tablaVistaPreviaSelectedItem = null;
        Requisicion tablaPSelectedItem;
        ObservableList<Requisicion> RequisicionesPendientes =FXCollections.observableArrayList(); 
        ObservableList<Producto> empty =FXCollections.observableArrayList();

    public void initData(Object obj){
    }
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       //Este Event es que me da error, i need help :c 
        EventListeners.onWindowOpened(EntregaReqPage, new Function<Window,Void>(){
            @Override
            public Void apply(Window parent) {
               iniciarEstructuraTabla();
              

                return null;
                
            }
        });
       
        
    }

    private void recalcularColumnWidth(){
    
        ObservableList columnas = tablaVistaPrevia.getColumns();
        ((TableColumn)( columnas.get(0) )).setPrefWidth(tablaVistaPrevia.getWidth()*0.35);
        ((TableColumn)( columnas.get(1) )).setPrefWidth(tablaVistaPrevia.getWidth()*0.15);
        ((TableColumn)( columnas.get(2) )).setPrefWidth(tablaVistaPrevia.getWidth()*0.105);
        ((TableColumn)( columnas.get(3) )).setPrefWidth(tablaVistaPrevia.getWidth()*0.105);
        ((TableColumn)( columnas.get(4) )).setPrefWidth(tablaVistaPrevia.getWidth()*0.105);
        ((TableColumn)( columnas.get(5) )).setPrefWidth(tablaVistaPrevia.getWidth()*0.185);

}

private void iniciarEstructuraTabla(){
    tablaVistaPrevia.getItems().clear();
    tablaVistaPrevia.getColumns().clear();
    TableColumn columnaProducto = new TableColumn<>("Producto");
    columnaProducto.setCellValueFactory(new PropertyValueFactory<>("producto"));
    columnaProducto.setPrefWidth(tablaVistaPrevia.getWidth()*0.35);

    TableColumn columnaUnidad = new TableColumn<>("Unidad");
    columnaUnidad.setCellValueFactory(new PropertyValueFactory<>("unidad"));
    columnaUnidad.setPrefWidth(tablaVistaPrevia.getWidth()*0.15);

    TableColumn columnaCantidadPedida = new TableColumn<>("Cantidad Pedida");
    columnaCantidadPedida.setCellValueFactory(new PropertyValueFactory<>("cantidadPedida"));
    columnaCantidadPedida.setPrefWidth(tablaVistaPrevia.getWidth()*0.105);

    TableColumn columnaCantidadEntregada = new TableColumn<>("Cantidad Entregada");
    columnaCantidadEntregada.setCellValueFactory(new PropertyValueFactory<>("cantidadEntregada"));
    columnaCantidadEntregada.setPrefWidth(tablaVistaPrevia.getWidth()*0.105);

    TableColumn columnaCantidadPendiente = new TableColumn<>("Cantidad Pendiente");
    columnaCantidadPendiente.setCellValueFactory(new PropertyValueFactory<>("cantidadPendiente"));
    columnaCantidadPendiente.setPrefWidth(tablaVistaPrevia.getWidth()*0.105);

    TableColumn columnaComentarios = new TableColumn<>("Comentarios");
    columnaComentarios.setCellValueFactory(new PropertyValueFactory<>("comentarios"));
    columnaComentarios.setPrefWidth(tablaVistaPrevia.getWidth()*0.18);
    tablaVistaPrevia.getColumns().addAll(columnaProducto, columnaUnidad, columnaCantidadPedida, columnaCantidadEntregada, columnaCantidadPendiente, columnaComentarios);
  
}

    
}    