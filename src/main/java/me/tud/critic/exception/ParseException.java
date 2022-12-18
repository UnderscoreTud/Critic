package me.tud.critic.exception;

public class ParseException extends RuntimeException {

    public ParseException(String message) {
        super("Parser Error: " + message);
    }

}
