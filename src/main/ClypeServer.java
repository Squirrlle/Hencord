package main;
import data.ClypeData;
import data.MessageClypeData;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Blueprint for the server the client wil connect to
 *
 * @author Henry Cadoff
 */
public class ClypeServer {

    public static final int DEFAULT_PORT = 7000;

    private int port;
    private Socket skt;
    private ServerSocket svr;
    private boolean closeConnection;
    private ArrayList<ServerSideClientIO> serverSideClientIOList;
    private ArrayList<ClypeData> clientList;

    /**
     * sets dataToReceiveFromClient & dataToSendToClient to null
     *
     * @param port  the port server is being accessed from
     */
    public ClypeServer(int port) throws IllegalArgumentException {

        if(port < 1024) {
            throw new IllegalArgumentException("Port must be greater than or equal to 1024");
        } else {
            this.closeConnection = false;

            this.port = port;

            this.serverSideClientIOList = new ArrayList<ServerSideClientIO>();
            this.clientList = new ArrayList<ClypeData>();
        }
    }

    /**
     * sets port to 7000 and calls the other constructor
     */
    public ClypeServer(){
        this(7000);
    }

    /**
     * @see ClypeClient
     * while closeConnection is false reads in data from ClypeClient and echos it back
     */
    public void start() {
        ServerSocket serverSocket = null;

        try {

            serverSocket = new ServerSocket(this.port);

            while(!this.closeConnection) {

                Socket client = serverSocket.accept();
                ServerSideClientIO newClient = new ServerSideClientIO(this, client);
                serverSideClientIOList.add(newClient);

                System.out.println(getClientList());

                Thread thread = new Thread(newClient);
                thread.start();
            }

        } catch(IOException ioe) {
            this.closeConnection = true;
            System.err.println("Line 128: " + ioe.getMessage());
        }

        try {
            serverSocket.close();
            this.closeConnection = true;
        } catch (IOException ioe) {
            System.err.println("Line 135: " + ioe.getMessage());
            this.closeConnection = true;
        }
    }

    /**
     *
     * @return clientList
     */
    public ArrayList<ClypeData> getClientList() {
        return this.clientList;
    }

    /**
     * brodcasts what is to be sent to the client
     * @param dataToBroadcastToClients
     */
    public synchronized void broadcast(ClypeData dataToBroadcastToClients) {
        for(int i = 0; i < serverSideClientIOList.size(); i++) {
            ServerSideClientIO client = serverSideClientIOList.get(i);
            client.setDataToSendToClient(dataToBroadcastToClients);
            client.sendData();
        }
    }

    /**
     * removes an element from the ArrayList
     * @param serverSideClientToRemove
     */
    public synchronized void remove(ServerSideClientIO serverSideClientToRemove) {
        int idx = this.serverSideClientIOList.indexOf(serverSideClientToRemove);
        this.serverSideClientIOList.remove(idx);
    }

    /**
     * get method for port
     *
     * @return port
     */
    public int getPort() {
        return port;
    }

    /**
     *
     * @return  a hashCode int
     */
    @Override
    public int hashCode() {
        int result = 31;
        result = 17 * result + port;
        return result;
    }

    /**
     * Checks if two ClypeServers are equal
     *
     * @param obj   a ClypeServer object
     * @return  if equal
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if ( !(obj instanceof  ClypeServer)) return false;
        ClypeServer other = (ClypeServer) obj;
        return this.port == other.port && this.closeConnection == other.closeConnection;
    }

    /**
     * Prints the port
     *
     * @return String describing the class and it's variables
     */
    @Override
    public String toString() {
        return "Server Information: \nPort " + getPort();
    }

    /**
     * Creates a ClypeServer with a port given by args and calls start()
     * @param args int
     */
    public static void main(String[] args) {
        if(args.length == 1) {

            try {
                int port = Integer.parseInt(args[0]);

                try {
                    ClypeServer server = new ClypeServer(port);
                    server.start();
                    System.out.println("Clype server started on port " + port);
                } catch(IllegalArgumentException iae) {
                    System.err.println(iae.getMessage());
                }

            } catch(NumberFormatException nfe) {
                System.err.println("Port must be a number");
            }

        } else if(args.length == 0) {
            ClypeServer server = new ClypeServer();
            System.out.println("Clype server started on port " + ClypeServer.DEFAULT_PORT);
            server.start();
        }


    }

}
