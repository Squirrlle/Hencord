package sample;

import data.FileClypeData;
import data.MessageClypeData;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.ClypeClient;

import javax.swing.*;
import java.io.File;

public class Main extends Application {

    /**
     * The GUI
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            String userName = JOptionPane.showInputDialog("Enter the User Name:");
            String hostName = JOptionPane.showInputDialog("Enter the Host Name:");
            String portS = JOptionPane.showInputDialog("Enter the Port Number:");
            int port = Integer.parseInt(portS);
            VBox messages = new VBox(5);
            ClypeClient cc = new ClypeClient(userName, hostName, port, messages);
            Thread t = new Thread(cc);
            t.start();
            cc.start();
            BorderPane root = new BorderPane();
            Scene scene = new Scene(root, 515, 400);
            primaryStage.setTitle("Clype 2.0");
            scene.getStylesheets().add(getClass().getResource("prettyStuffs.css").toExternalForm());

            //Message section
            Label l = new Label();
            messages.setPrefHeight(300);
            messages.setPrefWidth(400);
            messages.getChildren().add(l);
            messages.getStyleClass().add("msg");

            //User Input
            HBox msgBox = new HBox(5);
            msgBox.setPrefHeight(100);
            msgBox.setPrefWidth(400);
            msgBox.getStyleClass().add("usr");
            TextField send = new TextField("Type here");
            send.setPrefSize(300, 30);
            Button yeet = new Button("Send Text");
            Button sendPic = new Button("Send Picture");
            msgBox.getChildren().addAll(send, yeet, sendPic);




            yeet.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    String mes = send.getText();
                    MessageClypeData m = new MessageClypeData(cc.getUserName(), mes, 3);
                    cc.sendData(m);
                    send.clear();
                }
            });

            sendPic.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    FileChooser fc = new FileChooser();
                    File pic = fc.showOpenDialog(primaryStage);
                    FileClypeData f = new FileClypeData(cc.getUserName(), pic.getPath(), 2);
                    f.writeFileContents();
                    cc.sendData(f);
                }
            });

            scene.setOnKeyPressed(enter -> {
                String mes = send.getText();
                MessageClypeData m = new MessageClypeData(cc.getUserName(), mes, 3);
                cc.sendData(m);
                send.clear();
            });




            root.setTop(messages);
            root.setBottom(msgBox);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (Exception e){

        }
    }


    public static void main(String[] args) {
        launch();
    }
}
