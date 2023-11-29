package fr.jima.controller;

import fr.jima.contract.events.IModelObserver;
import fr.jima.model.Log;
import fr.jima.model.Model;
import fr.jima.model.User;
import fr.jima.service.client.ChatClient;
import fr.jima.service.client.cypher.Base64Cypher;
import fr.jima.service.client.cypher.ClearText;
import fr.jima.service.client.cypher.ICypher;
import fr.jima.view.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Controller implements IModelObserver, Runnable {

    private View<ICypher> view;

    private Model model;

    private ChatClient client;
    private User user;
    private boolean connected;

    public Controller(View<ICypher> view, Model model, ChatClient client) {
        this.view = view;
        this.model = model;
        this.client = client;
    }

    @Override
    public void run() {

        // Force le lancement dans l'EDT
        if (!EventQueue.isDispatchThread()) {
            EventQueue.invokeLater(this);
            return;
        }

        //Titling the Frame
        view.setTitle("JIMA: Java Instant Messaging App");

        // Setting default closing method.
        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Displaying User
        view.getUserNameLabel().setText(model.getCurrentUser().getName());

        // Register Cryptic Algorithm
        view.getCypherModel().addElement(new ClearText());
        view.getCypherModel().addElement(new Base64Cypher());

        // Controller is the Model Observer
        model.addListener(this);

        // Releasing on click focus
        final MouseListener handler = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                view.getInputArea().requestFocus();
            }
        };
        view.getOutputArea().addMouseListener(handler);
        view.getUsersList().addMouseListener(handler);

        // sendButton Pressed
        view.getSendButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Getting the message
                String log = view.getInputArea().getText().trim();
                // Resetting the field
                view.getInputArea().setText("");
                // Nothing to do if the line is Empty
                if (log.isEmpty()) return;
                // If client is not connected
                if (!client.isConnected()) {
                    connect(log);
                    return;
                }
                // Sending message to the Server
                else {
                    client.getOutputStream().println(client.getProtocol().sendMessageToServer(log));
                }
            }
        });

        // Pressing Enter in Input field
        view.getInputArea().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Simulating a sendButton Click
                view.getSendButton().doClick();
            }
        });

        // On Change on Cryptic Algorithm
        view.getCypherSelector().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                client.setCypher((ICypher) view.getCypherSelector().getSelectedItem());
            }
        });
        view.getCypherSelector().setSelectedItem(client.getCypher());

        // On Frame Closing, we shut the Server
        view.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                client.stop();
            }
        });

        // Set the frame visible
        view.setVisible(true);

        // Focusing by default on InputArea
        view.getInputArea().requestFocus();

    }

    private void connect(String input) {
        String[] tokens = input.split(":", 2);
        String hostName = tokens[0].trim();
        int portNumber = tokens.length > 1 ? Integer.parseInt(tokens[1]) : 500;
        view.appendOutput(String.format("Connexion � %s:%s ...", hostName, portNumber));
        client.connect(hostName, portNumber);
    }

    @Override
    public void onUserEvent(User user, boolean connected) {
        this.user = user;
        this.connected = connected;
        String fqn = user.getName() + "@" + user.getLocation();
        DefaultListModel<String> model = view.getUsersListModel();
        if (connected) {
            if (!model.contains(fqn)) view.getUsersListModel().addElement(fqn);
        }
        else {
            model.removeElement(fqn);
        }
    }

    @Override
    public void onLogReceived(Log log) {
        view.appendOutput(String.format("[%s] %s: %s", log.getDate(), log.getUser(), log.getMessage()));
    }

    public View<ICypher> getView() {
        return view;
    }

    public Model getModel() {
        return model;
    }

}
