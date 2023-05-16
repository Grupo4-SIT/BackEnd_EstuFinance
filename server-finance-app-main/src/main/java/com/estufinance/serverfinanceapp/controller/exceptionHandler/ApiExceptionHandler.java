package com.estufinance.serverfinanceapp.controller.exceptionHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.estufinance.serverfinanceapp.exception.NotDeletableException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler {

    public static final Logger logger = LogManager.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    public void constraintViolationHandler(ConstraintViolationException exception){
        logger.error("Exception message" + exception.getMessage());
        logger.error("Cause: " + exception.getCause());
        exception.fillInStackTrace();

    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    public void bindExceptionHandler(BindException exception){
        logger.error("Exception message" + exception.getMessage());
        logger.error("Cause: " + exception.getCause());
        exception.fillInStackTrace();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    public void entityNotFoundExceptionHandler(EntityNotFoundException exception){
        logger.error("Exception message" + exception.getMessage());
        logger.error("Cause: " + exception.getCause());
        exception.fillInStackTrace();
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(value=HttpStatus.CONFLICT)
    public void entityAlreadyOnStateExceptionHandler(IllegalStateException exception){
        logger.error("Exception message" + exception.getMessage());
        logger.error("Cause: " + exception.getCause());
        exception.fillInStackTrace();
    }

    @ExceptionHandler(IllegalCallerException.class)
    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    public void illegalCallerExceptionHandler(BindException exception){
        logger.error("Exception message" + exception.getMessage());
        logger.error("Cause: " + exception.getCause());
        exception.fillInStackTrace();
    }

    @ExceptionHandler(NotDeletableException.class)
    @ResponseStatus(value=HttpStatus.NOT_ACCEPTABLE)
    public void ahorroNotDeletableExceptionHandler(Exception exception){
        logger.error("Exception message" + exception.getMessage());
        logger.error("Cause: " + exception.getCause());
        exception.fillInStackTrace();
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    public void principalExceptionHandler(Exception exception){
        logger.error("Exception message" + exception.getMessage());
        logger.error("Cause: " + exception.getCause());
        exception.fillInStackTrace();
    }
    
}
