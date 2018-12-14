package main;

public class ClientSideServerListener implements Runnable {

    /**
     * @param ClypeClient the client who calls this class
     **/
    private ClypeClient client;

    public ClientSideServerListener(ClypeClient client) {
        this.client = client;
    }

    /**
     * Starts the thread
     */
    @Override
    public void run() {
        while(!this.client.getCloseConnection()) {
            this.client.receiveData();
            this.client.printData();
        }
    }
}
