package main;

import data.ClypeData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerSideClientIO implements Runnable{
    /**
     * @param closedConnection if the connection is open or not
     * @param dataToReceiveFromClient data rom the client
     * @param dataToSendToClient what's being sent
     * @param inFromClient receive information from the client
     * @param outFromClient what to send to the client
     * @param server the server
     * @param clientSocket the socket used by the client
     */
    private boolean closedConnection;
    private ClypeData dataToReceiveFromClient;
    private ClypeData dataToSendToClient;
    private ObjectInputStream inFromClient;
    private ObjectOutputStream outToClient;
    private ClypeServer server;
    private Socket clientSocket;

    ServerSideClientIO(ClypeServer server, Socket clientSocket){
        this.server = server;
        this.clientSocket = clientSocket;
        closedConnection = false;
        dataToReceiveFromClient = null;
        dataToSendToClient = null;
    }

    @Override
    public void run() {
        try {
            outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
            inFromClient = new ObjectInputStream (clientSocket.getInputStream());
            while (!closedConnection) {
                receiveData();
                dataToSendToClient = dataToReceiveFromClient;
                System.out.println(dataToSendToClient);
                server.broadcast(dataToSendToClient);
            }
        }
        catch (IOException e){
            System.err.print(e);
        }
    }

    /**
     * takes in data
     */
    public void receiveData(){
        try {
            dataToReceiveFromClient = (ClypeData) inFromClient.readObject();
            if(dataToReceiveFromClient == null){
                closedConnection = true;
                server.remove(this);
            }
        }
        catch (IOException e){
            System.err.println(e);
        }
        catch (ClassNotFoundException ce){
            System.err.println(ce);
        }
    }

    /**
     * sends out data
     */
    public void sendData(){
        try {
            outToClient.writeObject(dataToSendToClient);
        }
        catch (IOException e){
            System.err.println(e);
        }
    }

    /**
     *
     * @param dataToSendToClient
     */
    public void setDataToSendToClient(ClypeData dataToSendToClient){
        this.dataToSendToClient = dataToSendToClient;
    }
}
