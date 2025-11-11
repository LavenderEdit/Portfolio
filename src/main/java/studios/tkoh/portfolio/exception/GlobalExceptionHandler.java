package studios.tkoh.portfolio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import studios.tkoh.portfolio.dto.response.ApiResponse;

/**
 *
 * @author Studios TKOH!
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalStateException(IllegalStateException ex) {
        ApiResponse<Void> apiResponse = ApiResponse.error(ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT); // 409
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        ApiResponse<Void> apiResponse = ApiResponse.error("Credenciales inválidas");
        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED); // 401
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentialsException(BadCredentialsException ex) {
        ApiResponse<Void> apiResponse = ApiResponse.error("Credenciales inválidas");
        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED); // 401
    }
}
