package fr.jima.service.client;

import fr.jima.controller.Controller;
import fr.jima.model.Model;
import fr.jima.contract.protocol.IClientProtocol;
import fr.jima.service.client.cypher.ClearText;
import fr.jima.service.client.cypher.ICypher;
import fr.jima.view.View;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient implements Runnable {
    private Model model;

    private Controller ctrl;

    private Socket socket;

    private PrintWriter outStream;

    private BufferedReader inStream;

    private Thread thread;

    private IClientProtocol protocol;

    private ICypher cypher;

    public ChatClient() {
        // On utilise le nom de la session
        this(System.getProperty("user.name"));
    }

    public ChatClient(String userName) {

        // Creating App Model
        this.model = new Model(userName);

        // Keeping Encoding/Decoding
        this.cypher = new ClearText();

        // Creating Default Protocol
        this.protocol = new ChatClientProtocol();
        this.protocol.setClient(this);

    }


    public void start() {
        start(null);
    }

    public void start(final Runnable callback) {

        // Creating View in EDT
        EventQueue.invokeLater(new Runnable() {

            public void run() {

                // Creating the View
                ChatClient.this.createView();

                // Running Controller
                ChatClient.this.getController().run();

                // Invoking Callback
                if (callback != null) {
                    callback.run();
                }

            }
        });

    }

    protected void createView() {

        // Creating the View
        View<ICypher> view = new View<ICypher>();

        // Creating the Controller
        ctrl = new Controller(view, model, this);

    }

    public void connect(final String hostName, final int portNumber) {

        // Running method outside on EDT
        if (EventQueue.isDispatchThread()) {
            // Creating New thread to execute method inside
            //new Thread(() -> connect(hostName, portNumber)).start();
            new Thread(new Runnable() {
                public void run() {
                    connect(hostName, portNumber);
                }
            }).start();
            // Stopping Job
            return;
        }

        // Already connected
        if (isConnected()) {
            getView().appendOutput("Already Connected!");
            return;
        }

        try {

        //Opening Socket To Server
            socket = new Socket(hostName, portNumber);

            // Opening Streams
            outStream = new PrintWriter(socket.getOutputStream(), true);
            inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Start Thread to read Socket
            thread = new Thread(this);
            thread.start();

            // Client On Connexion Msg
            getView().appendOutput(String.format("Connected on %s:%s", hostName, portNumber));

            // Sending client name to Server
            outStream.println(protocol.sayHelloToServer(model.getCurrentUser().getName()));

        }

        // Catching Error
        catch (Exception e) {

            // displaying Error
            displayMessage("Error : connexion not possible, " + e.getClass().getSimpleName() + " - " + e.getMessage());

            // If socket Opened
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e1) {

                }
            }

            // resetting Connexion Vars
            socket = null;
            outStream = null;
            inStream = null;
        }

    }

    public void displayMessage(String msg) {
        // Display on console the msg
        ctrl.getView().appendOutput(msg);
    }

    @Override
    public void run() {

        // On pr�pare la cha�ne d'entr�es
        String inputLine;

        try {
            // Reading the Socket
            while ((inputLine = inStream.readLine()) != null) {

                // Protocol Executing
                String msg = protocol.processServerInput(inputLine);

                // Display on console
                if (msg != null) {
                    displayMessage(msg);
                }

                // Thread Interruption Managing
                if (thread.isInterrupted()) return;

            }
        }
        // Catching Errors
        catch (IOException e) {
            displayMessage("Error: impossible to Read Socket");
        }

        // Connexion Ending
        displayMessage("Disconnection from Server.");

    }

    public void stop() {

        // thread Interruption
        thread.interrupt();

        // Verifying if all date are sent
        outStream.flush();

        // Closing Socket
        try {
            socket.close();
        } catch (IOException e) {

        }
        // Deleting Connexion vars
        finally {
            outStream = null;
            inStream = null;
            socket = null;
        }

    }

    public Socket getSocket() {
        return socket;
    }

    public PrintWriter getOutputStream() {
        return outStream;
    }

    public BufferedReader getInputStream() {
        return inStream;
    }

    public IClientProtocol getProtocol() {
        return protocol;
    }

    public ICypher getCypher() {
        return cypher;
    }

    public void setCypher(ICypher cypher) {
        this.cypher = cypher;
    }

    public View<ICypher> getView() {
        return this.ctrl.getView();
    }

    public Model getModel() {
        return this.model;
    }

    public Controller getController() {
        return this.ctrl;
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }
}
