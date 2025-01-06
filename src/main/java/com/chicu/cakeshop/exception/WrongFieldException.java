package com.chicu.cakeshop.exception;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class WrongFieldException extends PancakeException {

    private final List<String> fieldNames;

    public WrongFieldException(List<String> fieldNames) {
        this.fieldNames = fieldNames;
    }

    public WrongFieldException() {
        this.fieldNames = Collections.emptyList();

    }

}
