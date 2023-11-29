package fr.jima.contract.protocol;

import fr.jima.service.server.ChatServer;
import fr.jima.service.server.ClientSocketThread;

public interface IServerProtocol {

    public void setServer(ChatServer server);

    public String processClientInput(ClientSocketThread client, String inputLine);

    public String sendMessage(ClientSocketThread socket, String log);

    public String sendClientConnected(ClientSocketThread clientThread);

    public String sendClientDisconnected(ClientSocketThread socket);
}
