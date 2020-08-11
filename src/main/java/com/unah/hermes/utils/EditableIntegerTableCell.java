package com.unah.hermes.utils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.UnaryOperator;

import com.google.api.services.storage.Storage.BucketAccessControls.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.converter.NumberStringConverter;

public class EditableIntegerTableCell<T> extends TableCell<T, Integer> {
    private TextField textField;
    private ObservableList<Collection> collectionList = FXCollections.<Collection>observableArrayList();
    @Override
    public void startEdit() {
        if (editableProperty().get()) {
            if (!isEmpty()) {
                System.out.println(getString());
                String valor = getString();
                super.startEdit();
                createTextField();

                setGraphic(textField);
                textField.requestFocus();
                textField.setText(valor);
                textField.selectAll();
            }
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItem() != null ? getItem().toString() : null);
        setGraphic(null);
    }

    
    @Override
    public void updateItem(Integer item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getItem().toString());
                    textField.selectAll();
                }
                setText(null);
                setGraphic(textField);
            } else {
                setText(getItem().toString());
                setGraphic(null);
            }
        }
    }

    private void commitHelper( boolean losingFocus ) {
            commitEdit(Integer.parseInt(textField.getText()));
    } 
    private void createTextField() {
        textField = new TextField();

        textField.setOnAction(evt -> {
            if (textField.getText() != null && !textField.getText().isEmpty()) {
                NumberStringConverter nsc = new NumberStringConverter();
                Number n = nsc.fromString(textField.getText());
                commitEdit(Integer.valueOf(n.intValue()));
            } 
        });
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

        UnaryOperator<TextFormatter.Change> digitsOnlyOperator = new UnaryOperator<TextFormatter.Change>() {
            @Override
            public Change apply(Change change) {
                if (!change.isContentChange())
                    return change;
                if (change.getControlNewText().length() == 0)
                    return change;
                try {
                    Integer.parseInt(change.getControlNewText());
                    return change;
                } catch (Exception e) {
                    return null;
                }
            }
        };
        textField.textFormatterProperty().set(new TextFormatter<>(digitsOnlyOperator));
        textField.setOnKeyPressed((keyEvent) -> {
            KeyEvent ke = keyEvent;
            System.out.println("KeyEvent");
            if (ke.getCode().equals(KeyCode.ESCAPE)) {
                cancelEdit();
            }else if (ke.getCode().equals(KeyCode.TAB)) {
                commitHelper(true);

                TableColumn<T,?> column = getTableColumn();
                int newRow = getTableRow().getIndex()+1;
                if (column != null) {
                    getTableView().requestFocus();
                    getTableView().scrollTo((getTableRow().getIndex())+1);
                    getTableView().layout();
                    System.out.print("Indice de fila: ");
                    System.out.println(getTableRow().getIndex());
                    if(ke.isShiftDown()) newRow = getTableRow().getIndex()-1;
                    getTableView().edit(newRow, column);
                }
            }
        });
        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
               //focus lost
                if(!newValue){
                    NumberStringConverter nsc = new NumberStringConverter();
                    Number n = nsc.fromString(textField.getText());
                    commitEdit(Integer.valueOf(n.intValue()));
               }

            }
            
        });
        textField.setAlignment(Pos.CENTER_LEFT);
        this.setAlignment(Pos.CENTER_LEFT);
    }
    private TableColumn<T, ?> getNextColumn(boolean forward) {
        java.util.List<TableColumn<T, ?>> columns = new ArrayList<>();
        for (TableColumn<T, ?> column : getTableView().getColumns()) {
            columns.addAll((Collection<? extends TableColumn<T, ?>>) column);
        }
        //There is no other column that supports editing.
        if (columns.size() < 2) {
            return null;
        }
        int currentIndex = columns.indexOf(getTableColumn());
        int nextIndex = currentIndex;
        if (forward) {
            nextIndex++;
            if (nextIndex > columns.size() - 1) {
                nextIndex = 0;
            }
        } else {
            nextIndex--;
            if (nextIndex < 0) {
                nextIndex = columns.size() - 1;
            }
        }
        return columns.get(nextIndex);
    }
    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }
    
    @Override
    public void commitEdit(Integer item) {
        if (isEditing()) {
            super.commitEdit(item);
        } else {
            final TableView<T> table = getTableView();
            if (table != null) {
                TablePosition<T, Integer> position = new TablePosition<T, Integer>(getTableView(),
                        getTableRow().getIndex(), getTableColumn());
                CellEditEvent<T, Integer> editEvent = new CellEditEvent<T, Integer>(table, position,
                        TableColumn.editCommitEvent(), item);
                Event.fireEvent(getTableColumn(), editEvent);
            }
            updateItem(item, false);
            if (table != null) {
                table.edit(-1, null);
            }
    
        }
    }
    
    }