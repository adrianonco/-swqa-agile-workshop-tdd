package edu.upc.talent.swqa.campus.domain.exception;

// UserNotFoundException.java: Custom exception for handling non-existent users
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String id) {
        super("User " + id + " does not exist");
    }
}
