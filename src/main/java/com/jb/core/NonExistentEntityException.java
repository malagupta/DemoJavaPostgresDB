package com.jb.core;

public class NonExistentEntityException extends Throwable {

    private static final long serialVersionUID = 8633588908169766368L;

    public NonExistentEntityException() {
        super("Entity does not exist");
    }
}