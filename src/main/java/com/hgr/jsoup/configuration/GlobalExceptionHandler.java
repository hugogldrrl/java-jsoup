package com.hgr.jsoup.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manejador global de excepciones.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /** Logger de la clase */
    private static Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Manejador de excepciones MissingServletRequestParameterException.
     * @param exception Excepción provocada.
     * @return Mapa con los mensajes de excepciones producidas.
     */
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map handle(MissingServletRequestParameterException exception) {
        LOG.error(exception.getMessage(), exception);
        return error(exception.getMessage());
    }

    /**
     * Manejador de excepciones MethodArgumentNotValidException.
     * @param exception Excepción provocada.
     * @return Mapa con los mensajes de excepciones producidas.
     */
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map handle(MethodArgumentNotValidException exception) {
        LOG.error(exception.getMessage(), exception);
        return error(exception.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList()));
    }

    /**
     * Manejador de excepciones ConstraintViolationException.
     * @param exception Excepción provocada.
     * @return Mapa con los mensajes de excepciones producidas.
     */
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map handle(ConstraintViolationException exception) {
        LOG.error(exception.getMessage(), exception);
        return error(exception.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList()));
    }

    /**
     * Manejador de excepciones BindException.
     * @param exception Excepción provocada.
     * @return Mapa con los mensajes de excepciones producidas.
     */
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map handle(HttpServletRequest request, BindException exception) {
        String a = request.getMethod();
        String b = request.getRequestURL().toString();
        String c = request.getQueryString();
        LOG.error(a,b,c);
        LOG.error(exception.getMessage(), exception);
        return error(exception.getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList()));
    }

    /**
     * Manejador de excepciones Exception.
     * @param exception Excepción provocada.
     * @return Mapa con los mensajes de excepciones producidas.
     */
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map handle(Exception exception) {
        LOG.error(exception.getMessage(), exception);
        return error(exception.getMessage());
    }

    /**
     * Obtiene un mapa con el error producido.
     * @param message Error producido.
     * @return Mapa con el error producido.
     */
    private Map error(Object message) {
        return Collections.singletonMap("error", message);
    }

}