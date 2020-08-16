package com.unah.hermes;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.ListenerRegistration;
import com.unah.hermes.objects.Producto;
import com.unah.hermes.objects.Requisicion;
import com.unah.hermes.objects.User;
import com.unah.hermes.provider.FirebaseConnector;
import com.unah.hermes.provider.FirestoreRoutes;
import com.unah.hermes.utils.EventListeners;
import com.unah.hermes.utils.Navigation;

import org.apache.commons.lang3.ArrayUtils;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.stage.Modality;

public class MainPage implements Initializable {
    // listViews
    @FXML
    ListView<Requisicion> listaRQP;
    @FXML
    ListView<Requisicion> listaRQE;
    @FXML
    ListView<Requisicion> listaRQD;
    // labels
    // ID
    @FXML
    Label lblReqIDP;
    @FXML
    Label lblReqIDD;
    @FXML
    Label lblReqIDE;

    // ESTADO
    @FXML
    Label lblEstadoP;
    @FXML
    Label lblEstadoD;
    @FXML
    Label lblEstadoE;

    // FECHA
    @FXML
    Label lblFechaP;
    @FXML
    Label lblFechaD;
    @FXML
    Label lblFechaE;

    // HORA
    @FXML
    Label lblHoraP;
    @FXML
    Label lblHoraD;
    @FXML
    Label lblHoraE;

    // AREA
    @FXML
    Label lblAreaP;
    @FXML
    Label lblAreaD;
    @FXML
    Label lblAreaE;

    // SOLICITANTE
    @FXML
    Label lblSolicitanteP;
    @FXML
    Label lblSolicitanteD;
    @FXML
    Label lblSolicitanteE;

    // Tablas
    @FXML
    TableView<Producto> tablaP;
    @FXML
    TableView<Producto> tablaD;
    @FXML
    TableView<Producto> tablaE;

    @FXML
    private Button btnExpandirMenu;

    @FXML
    private Button btnReqPendientes;

    @FXML
    private Button btnReqEntregadas;

    @FXML
    private Button btnReqDenegadas;

    @FXML
    private VBox vboxMenu;

    @FXML
    private VBox vboxMenuPequeno;

    @FXML
    private Button btnReqPendientesGrande;

    @FXML
    private Button btnReqEntregadasGrande;

    @FXML
    private Button btnReqDenegadasGrande;

    @FXML
    private AnchorPane gridReqPendientes;

    @FXML
    private AnchorPane gridReqEntregadas;

    @FXML
    private AnchorPane gridReqDenegadas;

    TranslateTransition openNav;
    TranslateTransition closeNav;

    FadeTransition aparecerNav;
    FadeTransition desaparecerNav;

    FadeTransition desaparecerIcono;
    FadeTransition aparecerIcono;

    @FXML
    ImageView imagenIcono;

    @FXML
    public void menuBtnCerrarClick(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    ///////////////////////////////////////////////////////////////////////////////////
    public void cerrarNav() {
        aparecerIcono.play();
        closeNav.setToX(-(vboxMenu.getWidth() + vboxMenuPequeno.getWidth()));
        closeNav.play();
        if(isNavOpen) isNavOpen = false;
        // Task<Void> sleeper = new Task<Void>() {
        //     @Override
        //     protected Void call() throws Exception {
        //         try {
        //             Thread.sleep(500);
        //         } catch (InterruptedException e) {
        //         }
        //         return null;
        //     }
        // };
        // sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
        //     @Override
        //     public void handle(WorkerStateEvent event) {
        //         vboxMenu.setViewOrder(0);
        //     }
        // });
        // new Thread(sleeper).start();

        desaparecerNav.play();
    }

    public void abrirNav() {
        desaparecerIcono.play();
        openNav.play();
        if(!isNavOpen) isNavOpen = true;
        aparecerNav.play();
        desaparecerIcono.play();
    }

    @FXML
    void btnReqDenegadasGrandeClick(ActionEvent event) {
        gridReqDenegadas.setVisible(true);
        gridReqEntregadas.setVisible(false);
        gridReqPendientes.setVisible(false);

        if (isNavOpen)
            cerrarNav();
    }

    @FXML
    void btnReqEntregadasGrandeClick(ActionEvent event) {
        gridReqDenegadas.setVisible(false);
        gridReqEntregadas.setVisible(true);
        gridReqPendientes.setVisible(false);

        if (isNavOpen)
            cerrarNav();
    }

    @FXML
    void btnReqPendientesGrandeClick(ActionEvent event) {
        gridReqDenegadas.setVisible(false);
        gridReqEntregadas.setVisible(false);
        gridReqPendientes.setVisible(true);

        if (isNavOpen)
            cerrarNav();
    }
    ///////////////////////////////////////////////////////////////////////////////////

    @FXML
    public void menuBtnImprimirClick(ActionEvent event) {

    }

    @FXML
    public void menuBtnImprimirMensualClick(ActionEvent event) {

    }

    @FXML
    public void menuBtnEntregarReqClick(ActionEvent event) {
        if (tablaPSelectedItem != null)
            Navigation.pushRouteWithParameter("EntregaReqPage", event, false, true, EntregaReqPage.class,
                    tablaPSelectedItem);
        else {
            Alert alert = new Alert(AlertType.ERROR, "Debe seleccionar una requisicion pendiente antes", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    public void menuBtnDenegReqClick(ActionEvent event) {
        if (tablaPSelectedItem != null) {
            String[][] unparsedData = { { "estado", "Denegada" } };
            Map<String, Object> newData = (Map) ArrayUtils.toMap(unparsedData);

            db.updateDocument(FirestoreRoutes.REQUISICIONES, tablaPSelectedItem.reqID, newData);
        } else {
            Alert alert = new Alert(AlertType.ERROR, "Debe seleccionar una requisicion antes", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    public void menuBtnOcultarReqClick(ActionEvent event) {

    }

    @FXML
    void btnExpandirMenuClick(ActionEvent event) {
        if (!isNavOpen) {
            // System.out.println(vboxMenu.getTranslateX());
            // System.out.println(Math.ceil((vboxMenu.getWidth() +
            // vboxMenuPequeno.getWidth() - 6) - 272));
            abrirNav();
            System.out.println("que3 pedo te vas a mover prru? if");
        } else {
            cerrarNav();

            System.out.println("que3 pedo te vas a mover prru? else");
        }
    }

    @FXML
    public void menuBtnMantUsuariosClick(ActionEvent event) {
        Navigation.pushRoute("MantUsuariosPage", event, false, true);
    }

    @FXML
    public void menuBtnMantProductosClick(ActionEvent event) {
        Navigation.pushRoute("MantProductosPage", event, false, true);
    }

    @FXML
    public void menuBtnMantAreasClick(ActionEvent event) {
        Navigation.pushRoute("MantAreasPage", event, false, true);
    }

    @FXML
    public void btnEntregarClick(ActionEvent event) {
        if (tablaPSelectedItem != null)
            Navigation.pushRouteWithParameter("EntregaReqPage", event, false, true, EntregaReqPage.class,
                    tablaPSelectedItem);
        else {
            Alert alert = new Alert(AlertType.ERROR, "Debe seleccionar una requisicion pendiente antes", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    public void btnDenegarClick(ActionEvent event) {
        if (tablaPSelectedItem != null) {
            String[][] unparsedData = { { "estado", "Denegada" } };
            Map<String, Object> newData = (Map) ArrayUtils.toMap(unparsedData);

            db.updateDocument(FirestoreRoutes.REQUISICIONES, tablaPSelectedItem.reqID, newData);
        } else {
            Alert alert = new Alert(AlertType.ERROR, "Debe seleccionar una requisicion antes", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    private void ocultarRequisicionesSeleccionadas(ActionEvent event) {

    }

    @FXML
    private void imprimirReqSeleccionada(ActionEvent event) {

    }

    @FXML
    private AnchorPane mainPage;
    Boolean isNavOpen = false;
    ListenerRegistration requisicionesListener;
    FirebaseConnector db;
    Requisicion tablaPSelectedItem = null;
    Requisicion tablaDSelectedItem = null;
    Requisicion tablaESelectedItem = null;
    public static ObservableList<Requisicion> RequisicionesPendientes = FXCollections.observableArrayList();
    public static ObservableList<Requisicion> RequisicionesEntregadas = FXCollections.observableArrayList();
    public static ObservableList<Requisicion> RequisicionesDenegadas = FXCollections.observableArrayList();
    ObservableList<Producto> empty = FXCollections.observableArrayList();

    public void initData(Object obj) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // crear los listeners para los datos de firebase
        // TODO requisiciones denegadas, requisiciones entregadas

        gridReqPendientes.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                gridReqPendientes.requestFocus();
                if(isNavOpen)cerrarNav();
            }
        });

        gridReqEntregadas.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                gridReqEntregadas.requestFocus();
                if(isNavOpen)cerrarNav();
            }
        });

        gridReqDenegadas.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                gridReqDenegadas.requestFocus();
                if(isNavOpen)cerrarNav();
            }
        });

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

        EventListeners.onWindowOpened(mainPage, new Function<Window, Void>() {
            @Override
            public Void apply(Window parent) {
                iniciarEstructuraTablas();

                // CREACION DE ANIMACIONES
                // ///////////////////////////////////////////////////////////
                vboxMenu.setViewOrder(-1.0);
                vboxMenuPequeno.setViewOrder(-2.0);
                
                openNav = new TranslateTransition(new Duration(450), vboxMenu);
                openNav.setToX(0 + vboxMenuPequeno.getWidth() - 6);
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
                // ///////////////////////////////////////////////////////////

                return null;
            }

        });

        EventListeners.onWindowClosing(mainPage, new Function<Window, Void>() {

            @Override
            public Void apply(Window t) {
                requisicionesListener.remove();
                return null;
            }

        });

        mainPage.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                recalcularColumnWidth();
            }
        });

        tablaP.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                recalcularColumnWidth();
            }
        });

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

        db = FirebaseConnector.getInstance();
        requisicionesListener = db.iniciarListenerRequisiciones();
        listaRQE.setItems(RequisicionesEntregadas);
        listaRQP.setItems(RequisicionesPendientes);
        listaRQD.setItems(RequisicionesDenegadas);

        // agregar el listener de cambio para cuando se cambie la seleccion
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
        // ((TableColumn)( columnasE.get(4) )).setPrefWidth(tablaE.getWidth()*0.105);
        // ((TableColumn)( columnasE.get(5) )).setPrefWidth(tablaE.getWidth()*0.23);

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

        // TableColumn columnaCantidadPendienteE = new TableColumn<>("C. Pendiente");
        // columnaCantidadPendienteE.setCellValueFactory(new
        // PropertyValueFactory<>("cantPendiente"));
        // columnaCantidadPendienteE.setPrefWidth(tablaE.getWidth()*0.105);
        // columnaCantidadPendienteE.setResizable(false);

        TableColumn columnaComentariosE = new TableColumn<>("Comentarios");
        columnaComentariosE.setCellValueFactory(new PropertyValueFactory<>("comentario"));
        columnaComentariosE.setPrefWidth(tablaE.getWidth() * 0.28);
        // columnaComentariosE.setPrefWidth(tablaE.getWidth()*0.22);
        columnaComentariosE.setResizable(false);

        tablaE.getColumns().addAll(columnaProductoE, columnaUnidadE, columnaCantidadPedidaE, columnaCantidadEntregadaE,
                /* columnaCantidadPendienteE, */ columnaComentariosE);

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

}