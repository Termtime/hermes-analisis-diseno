package com.unah.hermes;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;

import com.google.cloud.firestore.ListenerRegistration;
import com.unah.hermes.objects.Producto;
import com.unah.hermes.objects.Requisicion;
import com.unah.hermes.provider.FirebaseConnector;
import com.unah.hermes.provider.FirestoreRoutes;
import com.unah.hermes.utils.EventListeners;
import com.unah.hermes.utils.Navigation;

import org.apache.commons.lang3.ArrayUtils;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

public class MainPage implements Initializable {
    
    @FXML private void menuBtnCerrarClick(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML private void menuBtnMantUsuariosClick(ActionEvent event) {
        Navigation.pushRoute("MantUsuariosPage", event, false, true);
    }
    @FXML private void menuBtnMantProductosClick(ActionEvent event) {
        Navigation.pushRoute("MantProductosPage", event, false, true);
    }
    @FXML private void menuBtnMantAreasClick(ActionEvent event) {
        Navigation.pushRoute("MantAreasPage", event, false, true);
    }
    @FXML private void btnExpandirMenuClick(ActionEvent event) {
        if (!isNavOpen) {
            abrirNav();
        } else {
            cerrarNav();
        }
    }
    @FXML private void btnReqDenegadasGrandeClick(ActionEvent event) {
        listaRQD.getSelectionModel().clearSelection();
        listaRQE.getSelectionModel().clearSelection();
        listaRQP.getSelectionModel().clearSelection();
        ((Stage)(((Node) event.getSource()).getScene().getWindow())).setTitle("H.E.R.M.E.S. Requisiciones Denegadas");
        gridReqDenegadas.setVisible(true);
        gridReqEntregadas.setVisible(false);
        gridReqPendientes.setVisible(false);

        if (isNavOpen)
            cerrarNav();
    }
    @FXML private void btnReqEntregadasGrandeClick(ActionEvent event) {
        listaRQD.getSelectionModel().clearSelection();
        listaRQE.getSelectionModel().clearSelection();
        listaRQP.getSelectionModel().clearSelection();
        ((Stage)(((Node) event.getSource()).getScene().getWindow())).setTitle("H.E.R.M.E.S. Requisiciones Entregadas");
        gridReqDenegadas.setVisible(false);
        gridReqEntregadas.setVisible(true);
        gridReqPendientes.setVisible(false);

        if (isNavOpen)
            cerrarNav();
    }
    @FXML private void btnReqPendientesGrandeClick(ActionEvent event) {
        listaRQD.getSelectionModel().clearSelection();
        listaRQE.getSelectionModel().clearSelection();
        listaRQP.getSelectionModel().clearSelection();
        ((Stage)(((Node) event.getSource()).getScene().getWindow())).setTitle("H.E.R.M.E.S. Requisiciones Pendientes");
        gridReqDenegadas.setVisible(false);
        gridReqEntregadas.setVisible(false);
        gridReqPendientes.setVisible(true);

        if (isNavOpen)
            cerrarNav();
    }
    @FXML private void btnLogoutGrandeClick(ActionEvent event) {
        //Cerrar Sesion
        //confirmar si se desea cerrar sesión solo si no se está sosteniendo shift
        if(!isShiftDown)
            if(!Navigation.mostrarAlertConfirmacion("¿Desea cerrar sesión?", event)) return;
        if (isNavOpen)
            cerrarNav();
        Navigation.pushRoute("LoginPage", event, true, false);
    }
    @FXML private void menuBtnEntregarReqClick(ActionEvent event) {
        entregarRequisicionSeleccionada(event);
    }
    @FXML private void menuBtnDenegReqClick(ActionEvent event) {
        denegarRequisicionSeleccionada(event);
    }
    @FXML private void btnEntregarClick(ActionEvent event) {
        entregarRequisicionSeleccionada(event);
    }
    @FXML private void btnDenegarClick(ActionEvent event) {
        denegarRequisicionSeleccionada(event);
    }

    // listViews
    @FXML private ListView<Requisicion> listaRQP;
    @FXML private ListView<Requisicion> listaRQE;
    @FXML private ListView<Requisicion> listaRQD;
    ////LABELS
    // ID
    @FXML private Label lblReqIDP;
    @FXML private Label lblReqIDD;
    @FXML private Label lblReqIDE;

    // ESTADO
    @FXML private Label lblEstadoP;
    @FXML private Label lblEstadoD;
    @FXML private Label lblEstadoE;

    // FECHA
    @FXML private Label lblFechaP;
    @FXML private Label lblFechaD;
    @FXML private Label lblFechaE;

    // HORA
    @FXML private Label lblHoraP;
    @FXML private Label lblHoraD;
    @FXML private Label lblHoraE;

    // AREA
    @FXML private Label lblAreaP;
    @FXML private Label lblAreaD;
    @FXML private Label lblAreaE;

    // SOLICITANTE
    @FXML private Label lblSolicitanteP;
    @FXML private Label lblSolicitanteD;
    @FXML private Label lblSolicitanteE;

    // Tablas
    @FXML private TableView<Producto> tablaP;
    @FXML private TableView<Producto> tablaD;
    @FXML private TableView<Producto> tablaE;
    // Botones
    @FXML private Button btnExpandirMenu;
    @FXML private Button btnReqPendientes;
    @FXML private Button btnReqEntregadas;
    @FXML private Button btnReqDenegadas;
    @FXML private Button btnReqPendientesGrande;
    @FXML private Button btnReqEntregadasGrande;
    @FXML private Button btnReqDenegadasGrande;
    @FXML private Button btnLogoutGrande;
    @FXML private Button btnLogout;
    //NavBar
    @FXML private VBox vboxMenu;
    @FXML private VBox vboxMenuPequeno;
    @FXML private ImageView imagenIcono;
    //Tabs
    @FXML private AnchorPane gridReqPendientes;
    @FXML private AnchorPane gridReqEntregadas;
    @FXML private AnchorPane gridReqDenegadas;
    //AnchorPane root
    @FXML AnchorPane mainPage;
    ////TRANSICIONES
    //abrir y cerrar
    TranslateTransition openNav;
    TranslateTransition closeNav;
        //aparecer y desaparecer nav
    FadeTransition aparecerNav;
    FadeTransition desaparecerNav;
        //aparecer y desaparecer icono
    FadeTransition desaparecerIcono;
    FadeTransition aparecerIcono;

    //Variables globales
    Boolean isNavOpen = false;
    Boolean isShiftDown = false;
    ListenerRegistration requisicionesListener;
    FirebaseConnector db;
    Requisicion tablaPSelectedItem = null;
    Requisicion tablaDSelectedItem = null;
    Requisicion tablaESelectedItem = null;
    //Listas de requisiciones
    public static ObservableList<Requisicion> RequisicionesPendientes = FXCollections.observableArrayList();
    public static ObservableList<Requisicion> RequisicionesEntregadas = FXCollections.observableArrayList();
    public static ObservableList<Requisicion> RequisicionesDenegadas = FXCollections.observableArrayList();
    ObservableList<Producto> empty = FXCollections.observableArrayList();

    public void initData(Object obj) {}

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //bindear el hover de los botones pequeños y grandes del nav
        bindHover();
        bindListenersNavBar();
        //bindear las diferentes tablas y listas con listeners
        bindListeners();
        bindBotonesConfirmar();
        EventListeners.onWindowOpened(mainPage, new Function<Window, Void>() {
            @Override
            public Void apply(Window parent) {
                iniciarEstructuraTablas();
                // creacion de animaciones
                crearAnimacionesNavBar();
                //escuchar cuando se sostiene shift para hacer override a los dialogos de confirmar
                parent.getScene().addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
                    if (event.isShiftDown()) {
                        isShiftDown = true;
                    }else{
                        isShiftDown = false;
                    }
                    // event.consume();
                });
                parent.getScene().addEventFilter(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
                    if (event.isShiftDown()) {
                        isShiftDown = true;
                    }else{
                        isShiftDown = false;
                    }
                    // event.consume();
                });
                ((Stage)(parent)).setTitle("H.E.R.M.E.S. Requisiciones Pendientes");
                return null;
            }
        });

        EventListeners.onWindowClosing(mainPage, new Function<Window, Void>() {

            @Override
            public Void apply(Window t) {
                //remover el listener de requisiciones
                requisicionesListener.remove();
                return null;
            }

        });

        db = FirebaseConnector.getInstance();
        requisicionesListener = db.iniciarListenerRequisiciones();
        listaRQE.setItems(RequisicionesEntregadas);
        listaRQP.setItems(RequisicionesPendientes);
        listaRQD.setItems(RequisicionesDenegadas);
    }
    
    private void bindBotonesConfirmar() {
        btnLogoutGrande.setOnMouseClicked(event -> {
            btnLogoutGrande.fire();
        });
        btnLogout.setOnMouseClicked(event -> {
            btnLogout.fire();
        });
    }

    private void bindListenersNavBar() {
        gridReqPendientes.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                gridReqPendientes.requestFocus();
                if (isNavOpen)
                    cerrarNav();
            }
        });

        gridReqEntregadas.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                gridReqEntregadas.requestFocus();
                if (isNavOpen)
                    cerrarNav();
            }
        });

        gridReqDenegadas.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                gridReqDenegadas.requestFocus();
                if (isNavOpen)
                    cerrarNav();
            }
        });
    }
    private void crearAnimacionesNavBar() {
        vboxMenu.setViewOrder(-1.0);
        vboxMenuPequeno.setViewOrder(-2.0);

        openNav = new TranslateTransition(new Duration(450), vboxMenu);
        openNav.setToX(vboxMenuPequeno.getWidth() - 6);
        closeNav = new TranslateTransition(new Duration(450), vboxMenu);

        aparecerIcono = new FadeTransition(new Duration(250), imagenIcono);
        desaparecerIcono = new FadeTransition(new Duration(250), imagenIcono);
        aparecerIcono.setFromValue(0);
        aparecerIcono.setToValue(100);

        desaparecerIcono.setFromValue(100);
        desaparecerIcono.setToValue(0);

        aparecerNav = new FadeTransition(new Duration(450), vboxMenu);
        desaparecerNav = new FadeTransition(new Duration(250), vboxMenu);
        aparecerNav.setFromValue(0);
        aparecerNav.setToValue(100);

        desaparecerNav.setFromValue(100);
        desaparecerNav.setToValue(0);
    }
	private void recalcularColumnWidth() {
        ObservableList columnasP = tablaP.getColumns();

        ((TableColumn) (columnasP.get(0))).setPrefWidth(tablaP.getWidth() * 0.50);
        ((TableColumn) (columnasP.get(1))).setPrefWidth(tablaP.getWidth() * 0.35);
        ((TableColumn) (columnasP.get(2))).setPrefWidth(tablaP.getWidth() * 0.13);

        ObservableList columnasD = tablaD.getColumns();
        ((TableColumn) (columnasD.get(0))).setPrefWidth(tablaD.getWidth() * 0.50);
        ((TableColumn) (columnasD.get(1))).setPrefWidth(tablaD.getWidth() * 0.35);
        ((TableColumn) (columnasD.get(2))).setPrefWidth(tablaD.getWidth() * 0.1);

        ObservableList columnasE = tablaE.getColumns();
        ((TableColumn) (columnasE.get(0))).setPrefWidth(tablaE.getWidth() * 0.30);
        ((TableColumn) (columnasE.get(1))).setPrefWidth(tablaE.getWidth() * 0.20);
        ((TableColumn) (columnasE.get(2))).setPrefWidth(tablaE.getWidth() * 0.105);
        ((TableColumn) (columnasE.get(3))).setPrefWidth(tablaE.getWidth() * 0.105);

        ((TableColumn) (columnasE.get(4))).setPrefWidth(tablaE.getWidth() * 0.28);

    }
    private void iniciarEstructuraTablas() {

        // Tabla de Requisiciones pendientes
        tablaP.getItems().clear();
        tablaP.getColumns().clear();
        TableColumn columnaProductoP = new TableColumn<>("Producto");
        columnaProductoP.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaProductoP.setPrefWidth(tablaP.getWidth() * 0.50);
        columnaProductoP.setResizable(false);

        TableColumn columnaUnidadP = new TableColumn<>("Unidad");
        columnaUnidadP.setCellValueFactory(new PropertyValueFactory<>("unidad"));
        columnaUnidadP.setPrefWidth(tablaP.getWidth() * 0.35);
        columnaUnidadP.setResizable(false);

        TableColumn columnaCantidadPedidaP = new TableColumn<>("Cantidad");
        columnaCantidadPedidaP.setCellValueFactory(new PropertyValueFactory<>("cantPedida"));
        tablaP.getColumns().addAll(columnaProductoP, columnaUnidadP, columnaCantidadPedidaP);
        columnaCantidadPedidaP.setPrefWidth(tablaP.getWidth() * 0.13);
        columnaCantidadPedidaP.setResizable(false);

        // Tabla de Requisiciones entregadas
        tablaE.getItems().clear();
        tablaE.getColumns().clear();
        TableColumn columnaProductoE = new TableColumn<>("Producto");
        columnaProductoE.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaProductoE.setPrefWidth(tablaE.getWidth() * 0.30);
        columnaProductoE.setResizable(false);

        TableColumn columnaUnidadE = new TableColumn<>("Unidad");
        columnaUnidadE.setCellValueFactory(new PropertyValueFactory<>("unidad"));
        columnaUnidadE.setPrefWidth(tablaE.getWidth() * 0.20);
        // columnaUnidadE.setPrefWidth(tablaE.getWidth()*0.15);
        columnaUnidadE.setResizable(false);

        TableColumn columnaCantidadPedidaE = new TableColumn<>("C. Pedida");
        columnaCantidadPedidaE.setCellValueFactory(new PropertyValueFactory<>("cantPedida"));
        columnaCantidadPedidaE.setPrefWidth(tablaE.getWidth() * 0.105);
        columnaCantidadPedidaE.setResizable(false);

        TableColumn columnaCantidadEntregadaE = new TableColumn<>("C. Entregada");
        columnaCantidadEntregadaE.setCellValueFactory(new PropertyValueFactory<>("cantEntregada"));
        columnaCantidadEntregadaE.setPrefWidth(tablaE.getWidth() * 0.105);
        columnaCantidadEntregadaE.setResizable(false);

        TableColumn columnaComentariosE = new TableColumn<>("Comentarios");
        columnaComentariosE.setCellValueFactory(new PropertyValueFactory<>("comentario"));
        columnaComentariosE.setPrefWidth(tablaE.getWidth() * 0.28);
        columnaComentariosE.setResizable(false);

        tablaE.getColumns().addAll(columnaProductoE, columnaUnidadE, columnaCantidadPedidaE, columnaCantidadEntregadaE, columnaComentariosE);

        // Tabla de Requisiciones denegadas
        tablaD.getItems().clear();
        tablaD.getColumns().clear();
        TableColumn columnaProductoD = new TableColumn<>("Producto");
        columnaProductoD.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaProductoD.setPrefWidth(tablaD.getWidth() * 0.50);
        columnaProductoD.setResizable(false);

        TableColumn columnaUnidadD = new TableColumn<>("Unidad");
        columnaUnidadD.setCellValueFactory(new PropertyValueFactory<>("unidad"));
        columnaUnidadD.setPrefWidth(tablaD.getWidth() * 0.35);
        columnaUnidadD.setResizable(false);

        TableColumn columnaCantidadPedidaD = new TableColumn<>("Cantidad");
        columnaCantidadPedidaD.setCellValueFactory(new PropertyValueFactory<>("cantPedida"));
        columnaCantidadPedidaD.setPrefWidth(tablaD.getWidth() * 0.10);
        columnaCantidadPedidaD.setResizable(false);

        tablaD.getColumns().addAll(columnaProductoD, columnaUnidadD, columnaCantidadPedidaD);
    }
    private void popularTablaRequisicionesPDConProductos(TableView<Producto> tabla,
            ObservableList<Producto> productos) {
        try {
            tabla.getItems().clear();
            if (productos == null)
                return;
            for (Producto producto : productos) {
                tabla.getItems().add(producto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void popularTablaRequisicionesEntregadas(TableView<Producto> tabla, ObservableList<Producto> productos) {
        try {
            tabla.getItems().clear();
            if (productos == null)
                return;
            for (Producto producto : productos) {
                tabla.getItems().add(producto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void cerrarNav() {
        //mostrar el icono pequeño de la NavBar
        aparecerIcono.play();
        if (isNavOpen)
            isNavOpen = false;
        closeNav.setToX(-(vboxMenu.getWidth() + vboxMenuPequeno.getWidth()));
        //cerrar nav
        closeNav.play();
        desaparecerNav.play();
    }
    private void abrirNav() {
        desaparecerIcono.play();
        openNav.play();
        if (!isNavOpen)
            isNavOpen = true;
        aparecerNav.play();
        desaparecerIcono.play();
    }
    private void bindHover(){
        btnReqPendientesGrande.hoverProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    Event.fireEvent(btnReqPendientes, new MouseEvent(MouseEvent.MOUSE_ENTERED_TARGET, 0, 0, 0, 0,
                            MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
                } else {
                    Event.fireEvent(btnReqPendientes, new MouseEvent(MouseEvent.MOUSE_EXITED_TARGET, 0, 0, 0, 0,
                            MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
                }
            }

        });
        btnReqEntregadasGrande.hoverProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    Event.fireEvent(btnReqEntregadas, new MouseEvent(MouseEvent.MOUSE_ENTERED_TARGET, 0, 0, 0, 0,
                            MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
                } else {
                    Event.fireEvent(btnReqEntregadas, new MouseEvent(MouseEvent.MOUSE_EXITED_TARGET, 0, 0, 0, 0,
                            MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
                }
            }

        });
        btnReqDenegadasGrande.hoverProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    Event.fireEvent(btnReqDenegadas, new MouseEvent(MouseEvent.MOUSE_ENTERED_TARGET, 0, 0, 0, 0,
                            MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
                } else {
                    Event.fireEvent(btnReqDenegadas, new MouseEvent(MouseEvent.MOUSE_EXITED_TARGET, 0, 0, 0, 0,
                            MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
                }
            }

        });
        btnReqPendientes.hoverProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    Event.fireEvent(btnReqPendientesGrande, new MouseEvent(MouseEvent.MOUSE_ENTERED_TARGET, 0, 0, 0, 0,
                            MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
                } else {
                    Event.fireEvent(btnReqPendientesGrande, new MouseEvent(MouseEvent.MOUSE_EXITED_TARGET, 0, 0, 0, 0,
                            MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
                }
            }

        });
        btnReqEntregadas.hoverProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    Event.fireEvent(btnReqEntregadasGrande, new MouseEvent(MouseEvent.MOUSE_ENTERED_TARGET, 0, 0, 0, 0,
                            MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
                } else {
                    Event.fireEvent(btnReqEntregadasGrande, new MouseEvent(MouseEvent.MOUSE_EXITED_TARGET, 0, 0, 0, 0,
                            MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
                }
            }

        });
        btnReqDenegadas.hoverProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    Event.fireEvent(btnReqDenegadasGrande, new MouseEvent(MouseEvent.MOUSE_ENTERED_TARGET, 0, 0, 0, 0,
                            MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
                } else {
                    Event.fireEvent(btnReqDenegadasGrande, new MouseEvent(MouseEvent.MOUSE_EXITED_TARGET, 0, 0, 0, 0,
                            MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
                }
            }

        });
        btnLogout.hoverProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    Event.fireEvent(btnLogoutGrande, new MouseEvent(MouseEvent.MOUSE_ENTERED_TARGET, 0, 0, 0, 0,
                            MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
                } else {
                    Event.fireEvent(btnLogoutGrande, new MouseEvent(MouseEvent.MOUSE_EXITED_TARGET, 0, 0, 0, 0,
                            MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
                }
            }

        });
        btnLogoutGrande.hoverProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    Event.fireEvent(btnLogout, new MouseEvent(MouseEvent.MOUSE_ENTERED_TARGET, 0, 0, 0, 0,
                            MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
                } else {
                    Event.fireEvent(btnLogout, new MouseEvent(MouseEvent.MOUSE_EXITED_TARGET, 0, 0, 0, 0,
                            MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
                }
            }

        });
    }
    private void bindListeners(){
        //listener para escuchar cuando se seleccione una requisicion Pendiente
        listaRQP.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Requisicion>() {
            @Override
            public void changed(ObservableValue<? extends Requisicion> observable, Requisicion oldValue,
                    Requisicion newValue) {
                System.out.println(newValue);
                if (newValue != null) {
                    tablaPSelectedItem = newValue;
                    popularTablaRequisicionesPDConProductos(tablaP, newValue.productos);
                    lblReqIDP.setText(newValue.reqID);
                    lblEstadoP.setText(newValue.estado);
                    lblFechaP.setText(newValue.fechaString);
                    lblHoraP.setText(newValue.hora);
                    lblAreaP.setText(newValue.area);
                    lblSolicitanteP.setText(newValue.solicitante);
                } else {
                    lblReqIDP.setText("");
                    lblEstadoP.setText("");
                    lblFechaP.setText("");
                    lblHoraP.setText("");
                    lblAreaP.setText("");
                    lblSolicitanteP.setText("");
                    popularTablaRequisicionesPDConProductos(tablaP, null);

                }
            }
        });
        //listener para escuchar cuando el modelo de items cambie
        listaRQP.getItems().addListener(new ListChangeListener<Requisicion>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Requisicion> change) {
                if (change.getList().size() != 0) {
                    if (tablaPSelectedItem == null)
                        return;

                    String reqID = tablaPSelectedItem.reqID;
                    int index = 0;
                    for (Requisicion requisicion : change.getList()) {
                        if (requisicion.reqID.equals(reqID)) {
                            listaRQP.getSelectionModel().select(index);
                            break;
                        }
                        index++;
                    }
                }
            }
        });
        //listener para escuchar cuando se seleccione una requisicion Entregada
        listaRQE.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Requisicion>() {

            @Override
            public void changed(ObservableValue<? extends Requisicion> observable, Requisicion oldValue,
                    Requisicion newValue) {
                if (newValue != null) {
                    tablaESelectedItem = newValue;
                    popularTablaRequisicionesEntregadas(tablaE, newValue.productos);
                    lblReqIDE.setText(newValue.reqID);
                    lblEstadoE.setText(newValue.estado);
                    lblFechaE.setText(newValue.fechaString);
                    lblHoraE.setText(newValue.hora);
                    lblAreaE.setText(newValue.area);
                    lblSolicitanteE.setText(newValue.solicitante);
                } else {
                    tablaESelectedItem = newValue;
                    popularTablaRequisicionesEntregadas(tablaE, null);
                    lblReqIDE.setText("");
                    lblEstadoE.setText("");
                    lblFechaE.setText("");
                    lblHoraE.setText("");
                    lblAreaE.setText("");
                    lblSolicitanteE.setText("");
                }
            }
        });
        //listener para escuchar cuando el modelo de items cambie
        listaRQE.getItems().addListener(new ListChangeListener<Requisicion>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Requisicion> change) {
                if (change.getList().size() != 0) {
                    if (tablaPSelectedItem == null)
                        return;

                    String reqID = tablaPSelectedItem.reqID;
                    int index = 0;
                    for (Requisicion requisicion : change.getList()) {
                        if (requisicion.reqID.equals(reqID)) {
                            listaRQE.getSelectionModel().select(index);
                            break;
                        }
                        index++;
                    }
                }
            }
        });
        //listener para escuchar cuando se seleccione una requisicion Denegada
        listaRQD.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Requisicion>() {
            // prueba
            @Override
            public void changed(ObservableValue<? extends Requisicion> observable, Requisicion oldValue,
                    Requisicion newValue) {
                if (newValue != null) {
                    tablaDSelectedItem = newValue;
                    lblReqIDD.setText(newValue.reqID);
                    popularTablaRequisicionesPDConProductos(tablaD, newValue.productos);
                    lblEstadoD.setText(newValue.estado);
                    lblFechaD.setText(newValue.fechaString);
                    lblHoraD.setText(newValue.hora);
                    lblAreaD.setText(newValue.area);
                    lblSolicitanteD.setText(newValue.solicitante);
                } else {
                    popularTablaRequisicionesPDConProductos(tablaD, null);
                    tablaDSelectedItem = newValue;
                    lblReqIDD.setText("");
                    lblEstadoD.setText("");
                    lblFechaD.setText("");
                    lblHoraD.setText("");
                    lblAreaD.setText("");
                    lblSolicitanteD.setText("");
                }
            }

        });
        //listener para escuchar cuando el modelo de items cambie
        listaRQD.getItems().addListener(new ListChangeListener<Requisicion>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Requisicion> change) {

                if (change.getList().size() != 0) {
                    if (tablaPSelectedItem == null)
                        return;

                    String reqID = tablaPSelectedItem.reqID;

                    int index = 0;
                    for (Requisicion requisicion : change.getList()) {
                        if (requisicion.reqID.equals(reqID)) {

                            listaRQD.getSelectionModel().select(index);
                            break;
                        }
                        index++;
                    }
                }
            }
        });
        //bindear al ancho de la ventana para resizear
        mainPage.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                recalcularColumnWidth();
            }
        });
        //bindear al ancho de la ventana para resizear
        tablaP.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                recalcularColumnWidth();
            }
        });
        //bindear al ancho de la ventana para resizear
        tablaD.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                recalcularColumnWidth();
            }
        }); 
        
        tablaE.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                recalcularColumnWidth();
            }
        });
    }
    private void entregarRequisicionSeleccionada(ActionEvent event) {
        if (tablaPSelectedItem != null)

            Navigation.pushRouteWithParameter("EntregaReqPage", event, false, true, EntregaReqPage.class,
                    tablaPSelectedItem);
        else {
            Navigation.mostrarAlertError("Debe seleccionar una requisicion antes", event);
        }
    }
    private void denegarRequisicionSeleccionada(ActionEvent event) {
        if (tablaPSelectedItem != null) {
            //confirmar si se desea denegar solo si la tecla de shift no se está sosteniendo
            if(!isShiftDown)
                if(!Navigation.mostrarAlertConfirmacion("¿Desea denegar la requisición?", event)) return;

            String[][] unparsedData = { { "estado", "Denegada" } };
            Map<String, Object> newData = (Map) ArrayUtils.toMap(unparsedData);

            db.updateDocument(FirestoreRoutes.REQUISICIONES, tablaPSelectedItem.reqID, newData);
        } else {
            Navigation.mostrarAlertError("Debe seleccionar una requisicion antes", event);
        }
    }
}