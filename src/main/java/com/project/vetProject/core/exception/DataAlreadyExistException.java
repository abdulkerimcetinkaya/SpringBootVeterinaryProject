package com.project.vetProject.core.exception;

// Aynı veri zaten mevcut olduğunda fırlatılan özel istisna
public class DataAlreadyExistException extends RuntimeException {
    public DataAlreadyExistException(String message) {
        super(message);
    }
}
