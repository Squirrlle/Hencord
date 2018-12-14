package main;
import data.ClypeData;
import data.FileClypeData;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.TextField;
import data.MessageClypeData;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 *	Represents the client user
 *
 * @author Henry Cadoff
 */
public class ClypeClient implements Runnable{

    private String userName, hostName;
    private int port;
    private boolean closeConnection;
    private ObjectInputStream inFromServer;
    private ObjectOutputStream outToServer;
    private ClypeData dataToSendToServer;
    private ClypeData dataToReceiveFromServer;
    private Scanner inFromStd;
    private VBox gui;

    /**
     * Constructor userName, hostName and port
     *
     * @param userName  name of the user
     * @param hostName  name of the Host
     * @param port      port number
     */
    public ClypeClient(String userName, String hostName, int port, VBox v) {

        if(userName == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }

        if(hostName == null) {
            throw new IllegalArgumentException("Hostname cannot be null");
        }

        if(port < 1024) {
            throw new IllegalArgumentException("Port cannot be less than 1024");
        }

        this.closeConnection = false;
        this.dataToReceiveFromServer = null;
        this.dataToSendToServer = null;
        this.inFromServer = null;
        this.outToServer = null;

        this.userName = userName;
        this.hostName = hostName;
        this.port = port;
        this.gui = v;
    }

    /**
     * Sets port to 7000 then sets the userName and hostName
     *
     * @param userName  name of the user
     * @param hostName  name of the Host
     */
    public ClypeClient(String userName, String hostName, VBox v){
        this(userName, hostName, 7000, v);
    }

    /**
     * sets hostName to local host and poet to 7000
     *
     * @param userName  name of the user
     */
    public ClypeClient(String userName, VBox v){
        this(userName, "localhost", 7000, v);
    }

    /**
     * Defualt constructor, sets userName to anonymous user, hostName to local host and port to 7000
     */
    public ClypeClient(VBox v){
        this("anonymous user", "localhost", 7000, v);
    }

    /**
     * calls readClientData and printData until closeConnection is set to
     * @see ClypeServer
     */
    public void start() {
        Socket socket;

        System.out.println("Succesfully connected to server.");
        System.out.println("OPTIONS: DONE (to quit) or SENDFILE (to send file)");
        System.out.println("Type a message or type one of the options above to send information.");

        try {
            socket = new Socket(this.hostName, this.port);

            this.outToServer = new ObjectOutputStream(socket.getOutputStream());
            this.inFromServer = new ObjectInputStream(socket.getInputStream());

        } catch (IOException ioe) {
            this.closeConnection = true;
            System.err.println(ioe);

        }

        ClientSideServerListener listener = new ClientSideServerListener(this);
        Thread clientThread = new Thread(listener);
        clientThread.start();
    }

    /**
     * Takes in a ClypeData and sends it to the server
     * @param m a ClypeData Object
     */
    public void sendData(ClypeData m){
        try {
            Socket socket = new Socket(this.hostName, this.port);
            this.outToServer = new ObjectOutputStream(socket.getOutputStream());
            outToServer.writeObject(m);
        }
        catch (IOException e){
            System.err.println(e);
        }
        catch (NullPointerException e){
            System.err.println(e);
        }
    }


    /**
     * reads in dadt from the server and puts it into dataToRecieveFromServer
     * it also outputs the text and image objects
     */
    public void receiveData(){

        System.out.println("In recieve data line 140");

        try {
            dataToReceiveFromServer = (ClypeData) inFromServer.readObject();
            if(dataToReceiveFromServer.getType() == 3) {
                Platform.runLater(() -> {
                    TextField t = new TextField(dataToReceiveFromServer.toString());
                    t.setEditable(false);
                    gui.getChildren().add(t);
                });
            }
            else if(dataToReceiveFromServer.getType() == 2){
                Platform.runLater(() -> {
                    try {
                        BufferedImage img = ImageIO.read(new ByteArrayInputStream((byte[]) dataToReceiveFromServer.getData()));
                        Image i = SwingFXUtils.toFXImage(img, null);
                        ImageView iv = new ImageView(i);
                        iv.setFitHeight(50);
                        iv.setFitWidth(100);
                        gui.getChildren().add(iv);
                    }
                    catch (IOException e){
                        System.err.println(e);
                    }

                });
            }
        }
        catch (ClassNotFoundException e){
            System.err.println("Class Not Found");
        }
        catch (IOException ie){
            System.err.println("IOException");
        }

    }
    /**
     * prints dataToRecieceFromServer
     */
    public void printData(){
        System.out.println(dataToReceiveFromServer);
    }

    /**
     *
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     *
     * @return hostName
     */
    public String getHostName() {
        return hostName;
    }

    public boolean getCloseConnection(){
        return closeConnection;
    }

    /**
     *
     * @return port
     */
    public int getPort() {
        return port;
    }

    @Override
    public void run() {

    }

    /**
     *
     * @return hashCode
     */
    @Override
    public int hashCode() {
        int result = 31;
        result = 17 * result + port;
        result = 17 * result + userName.hashCode() + hostName.hashCode()
                + dataToReceiveFromServer.hashCode() + dataToSendToServer.hashCode();
        return result;
    }

    /**
     * Checks if the port, userName, hostName, sataToSendToServer & dataToRecieceFromServer are equal
     *
     * @param obj   a ClipeClient to compare to
     * @return if two ClypeClients are equal
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if ( !(obj instanceof  ClypeClient)) return false;
        ClypeClient other = (ClypeClient) obj;
        return this.port == other.port && this.userName.equals(other.userName)
                && this.hostName.equals(other.hostName) && this.dataToSendToServer.equals(other.dataToSendToServer)
                && this.dataToReceiveFromServer.equals(other.dataToReceiveFromServer);
    }

    /**
     * Prints userName, hostName & port
     *
     * @return Description of the Class and it's variables
     */
    @Override
    public String toString() {
        return "Client info: \nUser Name: " + getUserName() + "\nHost Name "
                + getHostName() + "\nPort: " +getPort();
    }

    /**
     * Creates a ClypeClient cc with parameters given in args and calls start();
     * @param args userName@hostName:port
     */
    public static void main(String args[]){

    }

}