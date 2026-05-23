package com.unimag.unimagleisureinventory.exceptions;

// Para acceso no autorizado (403)
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
