/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spazomatic.works;

import com.spazomatic.works.controls.TextFieldTreeCellImpl;
import com.spazomatic.works.services.SearchDirectoryService;
import com.spazomatic.works.util.Log;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 *
 * @author samuelsegal
 */
public class MainApp
        extends Application {

    private VBox vbox;
    private BorderPane border;
    private final Image greenFolderImage;
    private final Node greenFolder;
    private final GridPane centerGrid = new GridPane();
    private static final Logger LOG = Log.getLog();

    public MainApp() {
        this.greenFolderImage = new Image(
                MainApp.class.getResourceAsStream(
                        "graphics/greenFolder.png"));
        this.greenFolder = new ImageView(greenFolderImage);

    }
    @Override
    public void start(Stage primaryStage) {

        Properties properties = new Properties(System.getProperties());
        StringBuilder logSysInfo = new StringBuilder(properties.size()+2);
        logSysInfo.append("\n----------SYS INFO MATION----------\n");
        properties.stringPropertyNames().stream().forEach((property) -> {
            logSysInfo.append(String.format("%s: %s\n", property, properties.getProperty(property)));
        });
        logSysInfo.append("----------SYS INFO MATION----------");
        LOG.log(Level.INFO, logSysInfo.toString());
        border = new BorderPane();
        HBox hbox = addHBox(primaryStage);
        border.setTop(hbox);
        border.setLeft(addVBox());
        addStackPane(hbox);
        border.setRight(addFlowPane());
        border.setCenter(addAnchorPane(addGridPane()));
        Scene scene = new Scene(border);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.setTitle("Work");
        primaryStage.show();
    }

    private HBox addHBox(Stage primaryStage) {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15.0, 12.0, 15.0, 12.0));
        hbox.setSpacing(10.0);
        hbox.setStyle("-fx-background-color: #336699;");
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        MenuItem searchFile = new MenuItem("Search Files");
        searchFile.setOnAction(t -> {
            presentSearchFileCtrl(primaryStage);
        }
        );
        MenuItem clear = new MenuItem("Clear");
        clear.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        clear.setOnAction(t -> {
            vbox.setVisible(false);
        }
        );
        menuFile.getItems().addAll(new MenuItem[]{searchFile, clear});
        Menu menuEdit = new Menu("Edit");
        Menu menuView = new Menu("View");
        menuBar.getMenus().addAll(new Menu[]{menuFile, menuEdit, menuView});
        hbox.getChildren().addAll(new Node[]{menuBar});
        return hbox;
    }

    private VBox addVBox() {
        vbox = new VBox();
        vbox.setPadding(new Insets(10.0));
        vbox.setSpacing(8.0);
        Text title = new Text("Data");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14.0));
        vbox.getChildren().add(title);
        Hyperlink[] options = new Hyperlink[]{new Hyperlink("Search Files"), new Hyperlink("View Network"), new Hyperlink("Draw Cartoons"), new Hyperlink("Record Musac")};
        for (int i = 0; i < 4; ++i) {
            VBox.setMargin(options[i], new Insets(0.0, 0.0, 0.0, 8.0));
            vbox.getChildren().add(options[i]);
        }
        return vbox;
    }

    private void addStackPane(HBox hb) {
        StackPane stack = new StackPane();
        Rectangle helpIcon = new Rectangle(30.0, 25.0);
        helpIcon.setFill(new LinearGradient(0.0, 0.0, 0.0, 1.0, true, CycleMethod.NO_CYCLE, new Stop[]{new Stop(0.0, Color.web("#4977A3")), new Stop(0.5, Color.web("#B0C6DA")), new Stop(1.0, Color.web("#9CB6CF"))}));
        helpIcon.setStroke(Color.web("#D0E6FA"));
        helpIcon.setArcHeight(3.5);
        helpIcon.setArcWidth(3.5);
        Text helpText = new Text("?");
        helpText.setFont(Font.font("Verdana", FontWeight.BOLD, 18.0));
        helpText.setFill(Color.WHITE);
        helpText.setStroke(Color.web("#7080A0"));
        stack.getChildren().addAll(new Node[]{helpIcon, helpText});
        stack.setAlignment(Pos.CENTER_RIGHT);
        StackPane.setMargin(helpText, (Insets) new Insets(0.0, 10.0, 0.0, 0.0));
        hb.getChildren().add(stack);
        HBox.setHgrow(stack, Priority.ALWAYS);
    }

    private GridPane addGridPane() {

        centerGrid.setHgap(10.0);
        centerGrid.setVgap(10.0);
        centerGrid.setPadding(new Insets(0.0, 10.0, 0.0, 10.0));
        ImageView bruce = new ImageView(new Image(getClass().getResourceAsStream("graphics/bruce.png")));
        centerGrid.add(bruce, 0, 0);
        return centerGrid;
    }

    private FlowPane addFlowPane() {
        FlowPane flow = new FlowPane();
        flow.setPadding(new Insets(5.0, 0.0, 5.0, 0.0));
        flow.setVgap(4.0);
        flow.setHgap(4.0);
        flow.setPrefWrapLength(170.0);
        flow.setStyle("-fx-background-color: DAE6F3;");
        ImageView[] pages = new ImageView[8];
        for (int i = 0; i < 8; ++i) {
            pages[i] = new ImageView(new Image(getClass().getResourceAsStream("graphics/chart_" + (i + 1) + ".png")));
            flow.getChildren().add(pages[i]);
        }
        return flow;
    }

    private TilePane addTilePane() {
        TilePane tile = new TilePane();
        tile.setPadding(new Insets(5.0, 0.0, 5.0, 0.0));
        tile.setVgap(4.0);
        tile.setHgap(4.0);
        tile.setPrefColumns(2);
        tile.setStyle("-fx-background-color: DAE6F3;");
        ImageView[] pages = new ImageView[8];
        for (int i = 0; i < 8; ++i) {
            pages[i] = new ImageView(new Image(getClass().getResourceAsStream("graphics/chart_" + (i + 1) + ".png")));
            tile.getChildren().add(pages[i]);
        }
        return tile;
    }

    private AnchorPane addAnchorPane(GridPane grid) {
        AnchorPane anchorpane = new AnchorPane();
        Button buttonSave = new Button("Save");
        Button buttonCancel = new Button("Cancel");
        HBox hb = new HBox();
        hb.setPadding(new Insets(0.0, 10.0, 10.0, 10.0));
        hb.setSpacing(10.0);
        hb.getChildren().addAll(new Node[]{buttonSave, buttonCancel});
        anchorpane.getChildren().addAll(new Node[]{grid, hb});
        AnchorPane.setBottomAnchor(hb, (Double) 8.0);
        AnchorPane.setRightAnchor(hb, (Double) 5.0);
        AnchorPane.setTopAnchor(grid, (Double) 10.0);
        return anchorpane;
    }

    private void presentSearchFileCtrl(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(10.0);
        grid.setVgap(10.0);
        grid.setPadding(new Insets(15.0, 15.0, 15.0, 15.0));
        Text scenetitle = new Text("Search Files");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12.0));
        grid.add(scenetitle, 0, 0, 2, 1);
        Label regex = new Label("Glob:");
        grid.add(regex, 0, 1, 2, 1);
        final TextField regexTextField = new TextField();
        grid.add(regexTextField, 0, 2, 2, 1);
        Label startingDir = new Label("Root Dir:");
        grid.add(startingDir, 0, 3, 1, 1);
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("select Directory");
        final TextField rootDirText = new TextField();
        grid.add(rootDirText, 0, 4, 2, 1);
        Button chooserBtn = new Button("Select Root Directory", new ImageView(greenFolderImage));
        chooser.setTitle("Select Root Directory");
        chooserBtn.setOnAction(event -> {
            File selDir = chooser.showDialog(primaryStage);
            if (selDir != null) {
                rootDirText.setText(selDir.getAbsolutePath());
            }
        }
        );
        grid.add(chooserBtn, 1, 3);
        final Button btn = new Button("Search...");
        HBox searchBtn = new HBox(10.0);
        searchBtn.setAlignment(Pos.BOTTOM_RIGHT);
        searchBtn.getChildren().add(btn);
        grid.add(searchBtn, 0, 5);
        final Label statusLabel = new Label("Status");
        final ListView<Path> fileListView = new ListView<>();
        fileListView.setPrefSize(240.0, 262.0);
        final TreeView<Path> treeView = new TreeView<>();
        treeView.setPrefSize(240.0, 262.0);
        treeView.setCellFactory((TreeView<Path> param) -> new TextFieldTreeCellImpl(centerGrid));
        grid.add(statusLabel, 0, 6);
        grid.add(fileListView, 0, 7, 2, 1);
        grid.add(treeView, 0, 8, 2, 1);
        btn.setOnAction((ActionEvent e) -> {
            Path rootPath = Paths.get(rootDirText.getText(), new String[0]);
            //checkSetFilePermissions(rootPath);
            String pattern = regexTextField.getText();
            TreeItem<Path> rootItem = new TreeItem<>(rootPath, greenFolder);
            SearchDirectoryService searchDirectoryService = new SearchDirectoryService(rootPath, pattern, rootItem);
            searchDirectoryService.setOnSucceeded((WorkerStateEvent event) -> {
                btn.setText("Search Again");
                treeView.setRoot(rootItem);
            });
            try{
                searchDirectoryService.start();
            }catch(Exception ex){
                LOG.log(Level.SEVERE, "ERROR starrting searchDirectoryService: ", ex);
            }
            statusLabel.textProperty().bind(searchDirectoryService.messageProperty());
            btn.disableProperty().bind(searchDirectoryService.runningProperty());
            fileListView.itemsProperty().bind(searchDirectoryService.valueProperty());
        });
        vbox.getChildren().remove(0, vbox.getChildren().size());
        vbox.getChildren().add(grid);
        vbox.setVisible(true);
        border.setLeft(vbox);
    }

    public static void main(String[] args) {
        MainApp.launch(args);
    }


}
