package fr.jima.contract.events;

import fr.jima.model.Log;
import fr.jima.model.User;

public interface IModelObservable {

    public void addListener(IModelObserver listener);

    /**
     * Retirer un observateur.
     */
    public void removeListener(IModelObserver listener);

    /**
     * Lancer un �v�nement quand un message est re�u.
     */
    public void notifyListeners(Log log);

    /**
     * Lancer un �v�nement quand un utilisateur vient d'�tre connect� ou d�connect�.
     */
    public void notifyListeners(User user, boolean connected);
}
