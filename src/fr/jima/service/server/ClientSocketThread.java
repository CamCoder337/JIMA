package fr.jima.service.server;

import fr.jima.contract.events.IClientThreadObservable;
import fr.jima.contract.events.IClientThreadObserver;
import fr.jima.contract.protocol.IServerProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;



public class ClientSocketThread extends Thread implements IClientThreadObservable {

    private Socket clientSocket;

    private PrintWriter outStream;

    private BufferedReader inStream;

    private IServerProtocol protocol;

    private String userName = "Anonymous";

    private IClientThreadObserver observer;

    public ClientSocketThread(Socket clientSocket, IClientThreadObserver observer, IServerProtocol protocol) throws IOException {

        // Giving unique name to Thread
        super("ClientSocket-" + clientSocket.getPort());

        // We save client socket, observer and protocol
        this.clientSocket = clientSocket;
        this.observer = observer;
        this.protocol = protocol;

        // Opening In/Out Streams
        this.outStream = new PrintWriter(clientSocket.getOutputStream(), true);
        this.inStream  = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

    }

    @Override
    public String toString() {
        return getUserName() + "@" + getAddress();
    }

    public void setUserName(String userName) {
        // Having client name
        if ("Anonymous".equals(this.userName)) {
            LogWriter.getInstance().writeLog("New connexion of " + getAddress() + " (" + userName + ")");
        }
        this.userName = userName;

    }

    public String getUserName() {
        return userName;
    }

    public String getAddress() {
        if (clientSocket == null || clientSocket.isClosed()) return "null";
        return clientSocket.getInetAddress().getHostAddress();
    }

    @Override
    public void run() {

        // Preparing In/Out Streams
        String inputLine, outputLine;

        try {
            // Reading the socket Line per Line
            while ((inputLine = inStream.readLine()) != null) {

                // Protocol executing Process
                outputLine = protocol.processClientInput(this, inputLine);

                // On response return
                if (outputLine != null) {
                    write(outputLine);
                }

            }
        }
        // Catching Reading/Writing Error
        catch (IOException e) {
            LogWriter.getInstance().writeError(e.getClass().getSimpleName() + " While reading/Writing socket " + toString()
                    + " : " + e.getMessage());
        }

        // No data anymore to read, Client properly disconnected.
        // Socket closed, interruption
        if (clientSocket != null) {
            notifyClientDisconnected(this);
        }

    }

    public void write(String data) {
        outStream.println(data);
        outStream.flush();
    }

    @Override
    public void notifyMessageReceived(ClientSocketThread client, String log) {
        observer.onMessage(client, log);
    }

    @Override
    public void notifyClientDisconnected(ClientSocketThread client) {
        // We notify the Observer
        observer.onDisconnect(client);
        // We release ressources
        close();
        // Thread killing him self
    }

    private void close() {
        try {
            clientSocket.close();
            inStream.close();
            outStream.close();
        }
        catch (Exception ex) { }
        finally {
            clientSocket = null;
            inStream = null;
            outStream = null;
        }
    }

    @Override
    public void setObserver(IClientThreadObserver observer) {
        this.observer = observer;
    }

    @Override
    public void interrupt() {
        // Stopping the Socket Reading Thread
        super.interrupt();
        // Closing socket and Streams
        close();
    }

}
