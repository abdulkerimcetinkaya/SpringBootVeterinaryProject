package com.project.vetProject.core.exception;

// Aranan veri bulunamadığında fırlatılan özel istisna
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
