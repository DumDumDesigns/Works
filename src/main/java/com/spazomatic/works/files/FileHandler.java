/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spazomatic.works.files;

import com.spazomatic.works.util.Log;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.apache.tika.Tika;

/**
 *
 * @author samuelsegal
 */
public class FileHandler {

    private static final Logger LOG = Log.getLog();
    private File file;
    private Tika tika;
    
    public FileHandler(Path path) {
        file = path.toFile();
        tika = new Tika();
    }



    public void viewFile(Node node) {
        String fileType;
        try {
            fileType = tika.detect(file);
            LOG.log(Level.INFO, String.format("File: %s: Type: %s",file, fileType));
            if (fileType == null) {
                //TODO: Handle unknown file type
                return;
            }            
        } catch (IOException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        String [] baseSubTypes = fileType.split("/");
        String baseType = baseSubTypes[0];
        GridPane centerGrid = (GridPane)node;
        switch (baseType.toLowerCase()) {
            case "text": {                  
                break;
            }
            case "image": {                
                centerGrid.getChildren().clear();
                centerGrid.setHgap(10.0);
                centerGrid.setVgap(10.0);
                centerGrid.setPadding(new Insets(0.0, 10.0, 0.0, 10.0));
                ImageView iv = new ImageView("file://" + file.toPath().toAbsolutePath().toString());
                centerGrid.add(iv, 0, 0);
                break;
            }
        }
    }
    
}
