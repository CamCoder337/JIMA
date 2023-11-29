package fr.jima.contract.events;

import fr.jima.service.server.ClientSocketThread;

public interface IClientThreadObservable {
    public void setObserver(IClientThreadObserver observer);

    /**
     * Quand un client a envoy� un message.
     */
    public void notifyMessageReceived(ClientSocketThread client, String log);

    /**
     * Quand un client vient de se d�connecter.
     */
    public void notifyClientDisconnected(ClientSocketThread client);
}
