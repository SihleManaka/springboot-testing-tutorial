package com.example.springboot.testing.tutorial.exception;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String msg){
        super(msg);
    }

    public  ResourceNotFoundException(String msg, Throwable cause){
        super(msg,cause);
    }
}
