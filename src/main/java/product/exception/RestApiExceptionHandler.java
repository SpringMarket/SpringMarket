package product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import product.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;


@RestControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(value = { RequestException.class })
    public ResponseEntity<Response> handleApiRequestException(RequestException e) {
        return new ResponseEntity<>(Response.failure(e.getHttpStatus(), e.getMessage()),
                e.getHttpStatus());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidationExceptions(MethodArgumentNotValidException e) {

        String msg =  e.getBindingResult().getFieldError().getDefaultMessage();

        return new ResponseEntity<>(Response.failure(HttpStatus.BAD_REQUEST, msg),
                HttpStatus.BAD_REQUEST);
    }
}
