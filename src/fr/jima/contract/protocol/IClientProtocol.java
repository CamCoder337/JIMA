package fr.jima.contract.protocol;


import fr.jima.service.client.ChatClient;

public interface IClientProtocol {

    public void setClient(ChatClient client);

    public String sayHelloToServer(String clientName);

    public String sendMessageToServer(String log);

    public String processServerInput(String inputLine);
}
