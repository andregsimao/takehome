package com.example.takehome.exception;

public class ApplicationException extends Exception {
    private static final long serialVersionUID = 7718828512143293558L;

    private final ErrorType errorType;

    public ApplicationException(ErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }

    public ApplicationException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
