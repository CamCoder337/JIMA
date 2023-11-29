package fr.jima.service.server;

import fr.jima.contract.events.IClientThreadObserver;
import fr.jima.contract.protocol.IServerProtocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("LanguageDetectionInspection")
public class ChatServer extends Thread implements IClientThreadObserver {

    private int portNumber;
    private ServerSocket serverSocket;

    private ExecutorService threadPool;

    private List<ClientSocketThread> clientList;

    private IServerProtocol protocol;


    public ChatServer(int portNumber) {
        this(portNumber, new ChatServerProtocol());
    }

    public ChatServer(int portNumber, IServerProtocol protocol) {

        // Naming Thread
        super("ChatServer");

        // Saving the port
        this.portNumber = portNumber;

        // Collecting protocol and giving him current server
        this.protocol = protocol;
        this.protocol.setServer(this);

        // Creating Client Thread Pool
        this.threadPool = Executors.newFixedThreadPool(50);

        // Creating List for Connected Client
        this.clientList = new ArrayList<>();

    }

    @Override
    public synchronized void start() {

        // Creating Server Socket
        try {
            serverSocket = new ServerSocket(portNumber);
            serverSocket.setSoTimeout(1000);
        }
        catch (IOException e) {

            throw new RuntimeException(e);
        }

        // calling Start on Parent Thread.
        super.start();

        // On log
        LogWriter.getInstance().writeLog("Server Starting on Port  " + portNumber + "...");

    }

    /**
     * Execution du thread.
     */
    @Override
    public void run() {

        // On not Interruption Demand
        while (!Thread.interrupted()) {

            // Listening client
            try {

                // Having a client
                Socket clientSocket = serverSocket.accept();

                // Creating a thread
                ClientSocketThread clientThread = new ClientSocketThread(clientSocket, this, protocol);

                // Keeping ref
                synchronized (clientList) {
                    clientList.add(clientThread);
                }

                // Executing thread in pool
                threadPool.execute(clientThread);

                // If client is not alone, sending connected Client List
                if (clientList.size() > 1) {
                    sendConnectedClients(clientThread);
                }

            }
            catch (SocketTimeoutException e) {

            }
            // Log Important Errors
            catch (IOException e) {
                LogWriter.getInstance().writeError("accept() throws " + e.getClass().getSimpleName() + " : " + e.getMessage());
            }

        }

    }

    public void broadcast(String data) {
        synchronized (clientList) {
            for (ClientSocketThread client : clientList) {
                client.write(data);
            }
        }
    }

    public void sendConnectedClients(ClientSocketThread newClient) {
        synchronized (clientList) {
            for (ClientSocketThread client : clientList) {
                if (client != newClient)
                    newClient.write(protocol.sendClientConnected(client));
            }
        }
    }

    @Override
    public void onMessage(ClientSocketThread socket, String log) {
        // We log
        LogWriter.getInstance().writeLog("Msg from " + socket.getUserName() + " (" + socket.getAddress() + ")");
        // broadcasting on Client
        broadcast(protocol.sendMessage(socket, log));
    }

    @Override
    public void onDisconnect(ClientSocketThread socket) {

        // We log
        LogWriter.getInstance().writeLog("Disconnection of " + socket.getUserName() + " (" + socket.getAddress() + ")");

        // Removing the disconnected Client
        synchronized (clientList) {
            clientList.remove(socket);
        }

        // Broadcasting on Client
        broadcast(protocol.sendClientDisconnected(socket));

    }

    @Override
    public void interrupt() {

        // Stopping Socket Monitoring Thread
        super.interrupt();

        // Stopping all client connexion and clearing client list
        synchronized (clientList) {
            //clientList.forEach(client -> client.interrupt());
            for (ClientSocketThread c : clientList) c.interrupt();
            clientList.clear();
        }

        // Stopping Pool
        threadPool.shutdownNow();

        // closing the sErver
        try {
            serverSocket.close();
        }
        catch (IOException e) {
        }
        finally {
            // releasing the Socket
            serverSocket = null;
        }

    }

}
