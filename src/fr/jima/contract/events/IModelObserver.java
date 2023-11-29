package fr.jima.contract.events;

import fr.jima.model.Log;
import fr.jima.model.User;

public interface IModelObserver {

    public void onUserEvent(User user, boolean connected);

    public void onLogReceived(Log log);
}
