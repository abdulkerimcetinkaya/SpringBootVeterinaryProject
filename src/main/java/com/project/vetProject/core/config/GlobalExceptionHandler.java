package com.project.vetProject.core.config;

import com.project.vetProject.core.exception.DataAlreadyExistException;
import com.project.vetProject.core.result.Result;
import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.core.utilies.ResultHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    // NotFoundException'ı ele alır ve 404 yanıtı döner
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Result> handleNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(ResultHelper.NotFoundError(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    // MethodArgumentNotValidException'ı ele alır ve 400 yanıtı döner
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResultData<List<String>>> handleValidationErrors(MethodArgumentNotValidException e) {
        List<String> validationErrorList = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        return new ResponseEntity<>(ResultHelper.validateError(validationErrorList), HttpStatus.BAD_REQUEST);
    }

    // DataAlreadyExistException'ı ele alır ve 409 yanıtı döner
    @ExceptionHandler(DataAlreadyExistException.class)
    public ResponseEntity<Result> handleDataAlreadyExistException(DataAlreadyExistException e) {
        return new ResponseEntity<>(ResultHelper.error(e.getMessage()), HttpStatus.CONFLICT);
    }
}
