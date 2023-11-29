package fr.jima.service.client;

import fr.jima.contract.protocol.IClientProtocol;
import fr.jima.model.User;

public class ChatClientProtocol implements IClientProtocol {

    private ChatClient client;

    @Override
    public void setClient(ChatClient client) {
        this.client = client;
    }

    @Override
    public String sayHelloToServer(String clientName) {
        return "HELLO " + clientName.trim();
    }

    @Override
    public String sendMessageToServer(String log) {
        return "MSG " + client.getCypher().encode(log);
    }

    @Override
    public String processServerInput(String inputLine) {

        // Debug option, uncomment
        // System.out.println("Server Sent: " + inputLine);

        // Splitting msg and dispatching CMD and line
        String[] tokens = inputLine.trim().split("\\s+", 2);

        // Message vide
        if (tokens.length < 2) {
            return "Error: Invalid Server Msg";
        }

        // Evaluation function Type
        switch (tokens[0].toUpperCase()) {

            // Receiving Msg
            case "MSG":
                tokens = tokens[1].trim().split("\\s+", 4);
                String msg = client.getCypher().decode(tokens[3]);
                return String.format("%s sent: %s", tokens[0], msg);

            case "CONNECTED":
                tokens = tokens[1].trim().split("\\s+", 2);
                // Adding User
                client.getModel().addUser(new User(tokens[0], tokens[1]));
                return String.format("* %s Joined the Chat (%s)", tokens[0], tokens[1]);

            case "DISCONNECTED":
                tokens = tokens[1].trim().split("\\s+", 2);
                // Finding just disconnected User
                User user = client.getModel().findUser(tokens[0], tokens[1]);
                // Removing User
                client.getModel().removeUser(user);
                return String.format("* %s Quited the Chat (%s)", tokens[0], tokens[1]);

            // Default
            default :
                return "Invalid CMD " + tokens[0] + " sent by Server";

        }
    }

}
