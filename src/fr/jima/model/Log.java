package fr.jima.model;

import java.util.Date;

public class Log {
    private Date date;
    private User user;
    private String message;

    public Log(Date date, User user, String message) {
        this.date = date;
        this.user = user;
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }
}
