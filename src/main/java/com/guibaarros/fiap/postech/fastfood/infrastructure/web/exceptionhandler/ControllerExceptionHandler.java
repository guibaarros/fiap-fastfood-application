package com.guibaarros.fiap.postech.fastfood.infrastructure.web.exceptionhandler;

import com.guibaarros.fiap.postech.fastfood.application.exceptions.client.ClientAlreadyExistsException;
import com.guibaarros.fiap.postech.fastfood.application.exceptions.client.ClientNotFoundException;
import com.guibaarros.fiap.postech.fastfood.application.exceptions.order.InvalidOrderOperationException;
import com.guibaarros.fiap.postech.fastfood.application.exceptions.order.OrderNotFoundException;
import com.guibaarros.fiap.postech.fastfood.application.exceptions.product.InvalidProductCategoryException;
import com.guibaarros.fiap.postech.fastfood.application.exceptions.product.ProductAlreadyExistsException;
import com.guibaarros.fiap.postech.fastfood.application.exceptions.product.ProductNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = {
            ClientNotFoundException.class,
            ProductNotFoundException.class,
            OrderNotFoundException.class
    })
    public ResponseEntity<ErrorDTO> handleNotFoundException(final RuntimeException ex) {
        final ErrorDTO errorDTO = new ErrorDTO(ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(value = {
            ClientAlreadyExistsException.class,
            ProductAlreadyExistsException.class
    })
    public ResponseEntity<ErrorDTO> handleAlreadyExistsException(final RuntimeException ex) {
        final ErrorDTO errorDTO = new ErrorDTO(ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ResponseBody
    @ExceptionHandler(value = {InvalidProductCategoryException.class, InvalidOrderOperationException.class})
    public ResponseEntity<ErrorDTO> handleInvalidParameterException(final RuntimeException ex) {
        final ErrorDTO errorDTO = new ErrorDTO(ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        Map<String, List<String>> body = new HashMap<>();
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        body.put("errors", errors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

}
