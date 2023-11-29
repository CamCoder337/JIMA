package fr.jima.service.server;

import fr.jima.contract.protocol.IServerProtocol;

import java.util.Date;

public class ChatServerProtocol implements IServerProtocol {

    private ChatServer server;

    @Override
    public void setServer(ChatServer server) {
        this.server = server;
    }

    @Override
    public String processClientInput(ClientSocketThread client, String inputLine) {

        // Debug
        // System.out.println("Le client " + client + " a envoy�: " + inputLine);

        // On explose la cha�ne avec les espaces. On cherche � isoler le
        // premier mot (qui indique la commande) et le reste de la ligne.
        String[] tokens = inputLine.trim().split("\\s+", 2);

        // Message vide
        if (tokens.length < 2) {
            LogWriter.getInstance().writeError("Message invalide de " + client + " : " + inputLine);
            return null;
        }

        // On �value le type de fonction
        switch (tokens[0].toUpperCase()) {

            // Dans le cas o� le client donne pour la premi�re fois son nom, en d�but
            // de communication.
            case "HELLO" :
                // On associe le nom au socket
                client.setUserName(tokens[1]);
                // On propage l'information qu'un client est connect�
                server.broadcast(sendClientConnected(client));
                break;

            // Dans le cas o� le client envoie un message
            case "MSG" :
                // On broadcast � tous les clients
                client.notifyMessageReceived(client, tokens[1]);
                break;

            default :
                LogWriter.getInstance().writeError("Commande invalide " + tokens[0] + " envoy� par " + client);
                break;

        }

        return null;
    }

    @Override
    public String sendMessage(ClientSocketThread from, String log) {
        return String.format("MSG %s %s %s %s", from.getUserName(), from.getAddress(), new Date().getTime(), log);
    }

    @Override
    public String sendClientConnected(ClientSocketThread client) {
        return String.format("CONNECTED %s %s", client.getUserName(), client.getAddress());
    }

    @Override
    public String sendClientDisconnected(ClientSocketThread client) {
        return String.format("DISCONNECTED %s %s", client.getUserName(), client.getAddress());
    }

}
