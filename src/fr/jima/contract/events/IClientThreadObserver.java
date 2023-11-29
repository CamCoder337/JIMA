package fr.jima.contract.events;

import fr.jima.service.server.ClientSocketThread;

public interface IClientThreadObserver {

    public void onMessage(ClientSocketThread socket, String log);

    /**
     * Quand un client a ferm� son socket.
     *
     * @param socket Le client d�connect�.
     */
    public void onDisconnect(ClientSocketThread socket);
}
