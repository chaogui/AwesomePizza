package com.demo.pizzeria.exception;

public class ResourceAlreadyExistsException extends Throwable {
    public ResourceAlreadyExistsException(String msg) {
        super(msg);
    }
}
