package com.unah.hermes;
import javafx.beans.value.ChangeListener;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Function;
import javafx.scene.control.ListView;
import com.unah.hermes.objects.Producto;
import com.unah.hermes.objects.Requisicion;
import com.unah.hermes.provider.FirebaseConnector;
import com.unah.hermes.provider.FirestoreRoutes;
import com.unah.hermes.utils.EventListeners;
import com.google.cloud.firestore.ListenerRegistration;
import com.google.cloud.firestore.EventListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
public class EntregaReqPage implements Initializable {
    @FXML public javafx.scene.control.Button btnCancelar;
    @FXML public javafx.scene.control.Button btnTerminarEntrega;
    @FXML TableView<Requisicion> tablaVistaPrevia;
    @FXML ListView<Requisicion> listaRQP;
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

         ListenerRegistration requisicionesListener;
       FirebaseConnector db;
        Requisicion tablaVistaPreviaSelectedItem = null;
        Requisicion tablaPSelectedItem;
       

    public void initData(Object obj){
    }
    @FXML AnchorPane entregaReqPage; 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        entregaReqPage.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                entregaReqPage.requestFocus();

            }
        });
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