package com.demo.pizzeria.exception;

public class ResourceAlreadyExistException extends Throwable {
    public ResourceAlreadyExistException(String msg) {
        super(msg);
    }
}
