package com.oroboros.EscalaDeFolga.config.exceptionhandler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {

    RESOURCE_NOT_FOUND("/resource-not-found", "Resource not found"),
    BUSINESS_ERROR("/business-error", "Business rule violation"),
    INTERNAL_SERVER_ERROR("/internal-server-error", "Internal server error"),
    INVALID_ARGUMENT("/invalid-argument", "Invalid request argument");

    private final String uri;
    private final String title;

    ErrorType(String uri, String title) {
        this.uri = "http//localhost:8080/api/escala" + uri;
        this.title = title;
    }
}
