package prog.academy.infomoney.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ApplicationException.class})
    public ResponseEntity<Object> handleProfileExistException(ApplicationException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
