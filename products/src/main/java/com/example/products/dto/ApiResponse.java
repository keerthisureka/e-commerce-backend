package com.example.products.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> implements Serializable {
    private HttpStatus status;
    private String message;
    private T data;

    public ApiResponse(HttpStatus status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
