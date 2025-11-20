package com.oroboros.EscalaDeFolga.config.exceptionhandler;

import com.oroboros.EscalaDeFolga.domain.exception.EscalaNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.OffsetDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(EscalaNotFoundException.class)
    public ProblemDetail handleEscalaNotFound(EscalaNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle(ErrorType.RESOURCE_NOT_FOUND.getTitle());
        problem.setType(URI.create(ErrorType.RESOURCE_NOT_FOUND.getUri()));
        problem.setProperty("timestamp", OffsetDateTime.now());
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed.");
        problem.setTitle(ErrorType.INVALID_ARGUMENT.getTitle());
        problem.setType(URI.create(ErrorType.INVALID_ARGUMENT.getUri()));
        problem.setProperty("timestamp", OffsetDateTime.now());
        problem.setProperty("errors", ex.getBindingResult().getFieldErrors());
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralError(Exception ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        problem.setTitle(ErrorType.INTERNAL_SERVER_ERROR.getTitle());
        problem.setType(URI.create(ErrorType.INTERNAL_SERVER_ERROR.getUri()));
        problem.setProperty("timestamp", OffsetDateTime.now());
        return problem;
    }
}
