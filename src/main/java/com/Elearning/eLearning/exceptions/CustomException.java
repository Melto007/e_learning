package com.Elearning.eLearning.exceptions;

public class CustomException extends RuntimeException {
    private Integer status;

    public CustomException(Integer status, String message) {
        super(message);
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
