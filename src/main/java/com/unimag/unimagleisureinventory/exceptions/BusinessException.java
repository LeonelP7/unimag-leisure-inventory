package com.unimag.unimagleisureinventory.exceptions;


// Para reglas de negocio violadas (400)
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
