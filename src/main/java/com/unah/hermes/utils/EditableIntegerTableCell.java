package com.unah.hermes.utils;

import java.text.NumberFormat;
import java.util.function.UnaryOperator;

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
import javafx.util.converter.NumberStringConverter;

public class EditableIntegerTableCell<T> extends TableCell<T, Integer> {
    private TextField textField;

    @Override
    public void startEdit() {
        if (editableProperty().get()) {
            if (!isEmpty()) {
                System.out.println(getString());
                super.startEdit();
                createTextField();
                
                setGraphic(textField);
                textField.requestFocus();
                textField.setText(getString());
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

    private void createTextField() {
        textField = new TextField();
    
        textField.setOnAction(evt -> {
            if(textField.getText() != null && !textField.getText().isEmpty()){
                NumberStringConverter nsc = new NumberStringConverter();
                Number n = nsc.fromString(textField.getText());
                commitEdit(Integer.valueOf(n.intValue()));
            }
        });
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
    
        UnaryOperator digitsOnlyOperator = new UnaryOperator<TextFormatter.Change>() {
			@Override
			public Change apply(Change change) {
                if(!change.isContentChange()) return change;
                if(change.getControlNewText().length() == 0) return change;
                try{
                    Integer.parseInt(change.getControlNewText());
                    return change;
                }catch(Exception e){
                    return null;
                }
			}
        };
        textField.textFormatterProperty().set(new TextFormatter<>(digitsOnlyOperator));
        textField.setOnKeyTyped((ke) -> {
            if (ke.getCode().equals(KeyCode.ESCAPE)) {
                cancelEdit();
            }
        });
    
        textField.setAlignment(Pos.CENTER_LEFT);
        this.setAlignment(Pos.CENTER_LEFT);
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