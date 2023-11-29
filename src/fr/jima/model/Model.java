package fr.jima.model;

import fr.jima.contract.events.IModelObservable;
import fr.jima.contract.events.IModelObserver;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Model implements IModelObservable {

    /**
     * Les observateurs.
     */
    private final List<IModelObserver> listeners;

    /**
     * Les utilisateurs connect�s.
     */
    private final List<User> connectedUsers;

    /**
     * L'utilisateur en cours.
     */
    private User currentUser;

    /**
     * Constructeur.
     *
     * @param userName Le nom d'utilisateur.
     */
    public Model(String userName) {

        // On pr�pare les collections.
        listeners = new ArrayList<>();
        connectedUsers = new ArrayList<>();

        // Nom d'utilisateur par d�faut
        setCurrentUser(new User(userName, "127.0.0.1"));
    }

    @Override
    public synchronized void addListener(IModelObserver listener) {
        this.listeners.add(listener);
    }

    @Override
    public synchronized void removeListener(IModelObserver listener) {
        this.listeners.remove(listener);
    }

    @Override
    public synchronized void notifyListeners(Log log) {

        // New method using lambda
        listeners.forEach(listener -> listener.onLogReceived(log));

        // Old method using For
//        for (IModelObserver listener : listeners) {
//            listener.onLogReceived(log);
//        }
    }

    @Override
    public void notifyListeners(User user, boolean connected) {
        //listeners.forEach(listener -> listener.onUserEvent(user, connected));
        for (IModelObserver listener : listeners) {
            listener.onUserEvent(user, connected);
        }
    }

    public void addUser(User user) {
        connectedUsers.add(user);
        notifyListeners(user, true);
    }

    public void removeUser(User user) {
        if (connectedUsers.remove(user)) {
            notifyListeners(user, false);
        }
    }

    public void addLog(String text) {
        notifyListeners(new Log(new Date(), this.currentUser, text));
    }

    public void setCurrentUser(User user) {
        if (this.currentUser == user) return;
        this.currentUser = user;
        addUser(user);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public User findUser(String name, String address) {
        for (User user : connectedUsers) {
            if (user.getName().equals(name) && user.getLocation().equals(address)) {
                return user;
            }
        }
        return null;
    }

}

