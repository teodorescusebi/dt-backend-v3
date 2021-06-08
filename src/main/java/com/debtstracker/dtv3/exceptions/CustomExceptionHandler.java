package com.debtstracker.dtv3.exceptions;

import com.debtstracker.dtv3.dtos.DTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    public CustomExceptionHandler() {
        super();
    }

    @ExceptionHandler(value = CustomNotFoundException.class)
    public ResponseEntity<DTO.Message> handleCustomNotFound(CustomNotFoundException exception) {
        exception.printStackTrace();
        return new ResponseEntity<>(new DTO.Message(exception.getErrorMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = CustomConflictException.class)
    public ResponseEntity<DTO.Message> handleCustomConflict(CustomConflictException exception) {
        exception.printStackTrace();
        return new ResponseEntity<>(new DTO.Message(exception.getErrorMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<DTO.Message> handleAllOtherExceptions(Exception exception) {
        exception.printStackTrace();
        return new ResponseEntity<>(new DTO.Message("INTERNAL_SERVER_ERROR"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = Throwable.class)
    public ResponseEntity<DTO.Message> handleAllOtherExceptions(Throwable exception) {
        return this.handleAllOtherExceptions((Exception) exception);
    }

}
