/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spazomatic.works.tasks;

import com.spazomatic.works.MainApp;
import com.spazomatic.works.util.Log;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author samuelsegal
 */
public class SearchFilesTask extends Task<ObservableList<Path>> {

    private final Path path;
    private final String pattern;
    ObservableList<Path> filesFound = FXCollections.observableArrayList();
    TreeItem<Path> rootItem;
    private static final Logger LOG = Log.getLog();
    public SearchFilesTask(Path path, String pattern, TreeItem<Path> rootItem) {
        this.path = path;
        this.pattern = pattern;
        this.rootItem = rootItem;
    }

    @Override
    protected ObservableList<Path> call() throws Exception {
        LOG.log(Level.INFO, String.format("Searching Path: %s for Pattern: %s", path, pattern));
        updateProgress(0, 0);
        updateMessage("Searching ...");
        final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            int fileCount;
            int fileTrackCount;
            TreeItem<Path> newFolder;
            TreeItem<Path> fileItem;

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {

                Node greenFolder = new ImageView(
                        new Image(MainApp.class.getResourceAsStream(
                                        "graphics/greenFolder.png"))); 
                if(dir.equals(path)){
                    newFolder = rootItem;
                }else{
                    newFolder = new TreeItem<>(dir, greenFolder);
                }
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (matcher.matches(file.getFileName())) {
                    ++fileCount;
                    filesFound.add(file.toAbsolutePath());
                    updateMessage(String.format("%d files founded", fileCount));
                    updateProgress(fileCount, fileCount);
                    fileItem = new TreeItem<>(file);
                    newFolder.getChildren().add(fileItem);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (!dir.equals(path) && fileTrackCount < fileCount) {
                    fileTrackCount = fileCount;
                    rootItem.getChildren().add(newFolder);               
                }
                return super.postVisitDirectory(dir, exc);
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                LOG.log(Level.SEVERE, String.format("VisitFile Error: Message %s: Cause: %s: Suppressed %s", 
                        exc.getMessage(), exc.getCause(), Arrays.toString(exc.getSuppressed())), exc);
                return FileVisitResult.CONTINUE;
            }

        });        
        return filesFound;
    }

}
