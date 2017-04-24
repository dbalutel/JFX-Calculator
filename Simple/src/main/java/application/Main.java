package application;

//TODO JAVADOC
//TODO TO FUNCTIONAL

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.awt.Toolkit;

import control.MainViewController;
import expression.Expression;

public class Main extends Application {

    final String os = System.getProperty("os.name");

    private Main main = this;

    private Stage primaryStage;
    private BorderPane root = new BorderPane();

    private Scene scene;

    ///////////////////////////////////////////////
    private ObservableList<Expression> historyLogs =  FXCollections.observableArrayList();

    public void addHistory(Expression expression) {
        historyLogs.add(expression);
    }
    ////////////////////////////////////////////////

    @Override
    public void start(Stage primaryStage) {

        try {
            if (os.startsWith("Mac")) {
                java.awt.Image image = Toolkit.getDefaultToolkit().getImage("src/main/resources/icon.png");
                com.apple.eawt.Application.getApplication().setDockIconImage(image);
            } else if (os.startsWith("Win")) {
                primaryStage.getIcons().add(new Image("https://psv4.userapi.com/c810132/u212633253/docs/3a8d2f3adffc/icon.png"));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("[no dock icon found]");
        }

        this.primaryStage = primaryStage;
        primaryStage.setResizable(false);
        primaryStage.setTitle("JFXCalculator");

        createMainView();
        createMenuBar();

       if (os.startsWith("Mac")) {
           scene = new Scene(root);
       } else if (os.startsWith("Win")) {
           scene = new Scene(root,227,423);
       }

        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(actionEvent -> Platform.exit());
    }

    private void createMainView() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/MainView.fxml"));

            AnchorPane anchorPane = loader.load();

            MainViewController controller = loader.getController();
            controller.setMainView(this);

            root.setCenter(anchorPane);

        } catch (IOException | IllegalStateException e) {
            System.err.println(e.getMessage());
            System.err.println("[no MainView.fxml file found]");
            System.exit(-1);
        }
    }

    private void createMenuBar() {
        ///////////////////////////////////
        Menu menu = new Menu("Menu");

        MenuItem history = new MenuItem("History");
        history.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new History(main,historyLogs);
            }
        });

        MenuItem close = new MenuItem("Close");
        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });

        menu.getItems().addAll(history, close);
        ////////////////////////////////////
        MenuBar menuBar = new MenuBar();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        menuBar.getMenus().add(menu);
        ////////////////////////////////////

        if (os.startsWith("Mac")) {
            menuBar.useSystemMenuBarProperty().set(true);
        }

        root.setTop(menuBar);
    }

    public static void main(String[] args) {
        Main.launch(args);
    }
}
