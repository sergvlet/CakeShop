package com.chicu.cakeshop.exception;

public class PancakeException extends Exception {

    String errorMessage;

    public PancakeException() {
        super();
    }

    public PancakeException(String errorMessage) {
        super();
        this.errorMessage = errorMessage;
    }


}
