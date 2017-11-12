package com.github.luohaha.luoORM.exception;

public class ClassNotExistAnnotation extends Exception {
    public ClassNotExistAnnotation() {
        super("class's annotation isn't exist");
    }
}
