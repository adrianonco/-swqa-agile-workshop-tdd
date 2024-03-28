package edu.upc.talent.swqa.campus.domain.exception;

// UserNotTeacherException.java: Custom exception for handling non-teachers users
public class UserNotTeacherException extends RuntimeException {
    public UserNotTeacherException(String id) {
        super("User " + id + " is not a teacher");
    }
}
