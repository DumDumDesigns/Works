/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spazomatic.works.controls;

import com.spazomatic.works.files.FileHandler;
import com.spazomatic.works.util.Log;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author samuelsegal
 */
public class TextFieldTreeCellImpl extends TreeCell<Path> {

    private TextField textField;
    private final ContextMenu contextMenu = new ContextMenu();
    private static final Logger LOG = Log.getLog();

    public TextFieldTreeCellImpl(Node node) {
        MenuItem openMenuItem = new MenuItem("Open");
        contextMenu.getItems().add(openMenuItem);
        
        openMenuItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Path pathItem = getItem();
                FileHandler fileHandler = new FileHandler(pathItem);                
                fileHandler.viewFile(node);
                Log.getLog().log(Level.INFO, String.format("Handling file: %s", pathItem));
            }


        });
    }

    @Override
    public void startEdit() {
        super.startEdit();

        if (textField == null) {
            createTextField();
        }
        setText(null);
        setGraphic(textField);
        textField.selectAll();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText(getItem().getFileName().toString());
        setGraphic(getTreeItem().getGraphic());
    }

    @Override
    public void updateItem(Path item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());
                }
                setText(null);
                setGraphic(textField);
                setContextMenu(null);
            } else {
                setText(getString());
                setGraphic(getTreeItem().getGraphic());
                if (getTreeItem().isLeaf() && getTreeItem().getParent() != null) {
                    LOG.log(Level.INFO, "TreeItem: {0}", getTreeItem());
                    setContextMenu(contextMenu);
                }
            }
        }
    }

    private void createTextField() {
        textField = new TextField(getString());
        textField.setOnKeyReleased(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ENTER) {
                    commitEdit(getItem().getFileName());
                } else if (t.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                }
            }
        });

    }

    private String getString() {
        return getItem() == null ? "" : getItem().getFileName().toString();
    }

}
