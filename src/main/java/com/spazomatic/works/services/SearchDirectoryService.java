/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spazomatic.works.services;

import com.spazomatic.works.tasks.SearchFilesTask;
import java.nio.file.Path;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.TreeItem;

/**
 *
 * @author samuelsegal
 */
public class SearchDirectoryService extends Service<ObservableList<Path>>{

    private final SearchFilesTask searchFilesTask;
    public SearchDirectoryService(Path rootDirPath, String searchPattern, TreeItem<Path> rootItem) {
        this.searchFilesTask = new SearchFilesTask(rootDirPath, searchPattern, rootItem);
    }

    @Override
    protected Task<ObservableList<Path>> createTask() {
        return searchFilesTask;
    }

}