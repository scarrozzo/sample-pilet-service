package io.piral.feedservice.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.piral.feedservice.util.JsonUtils;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    /**
     * Read pilet package exception
     */
    @ExceptionHandler({ParsePackageException.class})
    public ResponseEntity<?> piletPackageParseException(Throwable e, HttpServletRequest request, HttpServletResponse response) {
        Error error = Error.builder()
                .code(ServiceError.E0001.getCode())
                .message(e.getMessage() != null ? e.getMessage() : ServiceError.E0001.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return responseEntity(error);
    }

    /**
     * Download file exception
     */
    @ExceptionHandler({DownloadFileException.class})
    public ResponseEntity<?> downloadFileException(Throwable e, HttpServletRequest request, HttpServletResponse response) {
        Error error = Error.builder()
                .code(ServiceError.E0002.getCode())
                .message(e.getMessage() != null ? e.getMessage() : ServiceError.E0002.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        return responseEntity(error);
    }

    /**
     * Entity not found file exception
     */
    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<?> entityNotFoundException(Throwable e, HttpServletRequest request, HttpServletResponse response) {
        Error error = Error.builder()
                .code(ServiceError.E0003.getCode())
                .message(e.getMessage() != null ? e.getMessage() : ServiceError.E0003.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .build();
        return responseEntity(error);
    }


    /**
     * INTERNAL_SERVER_ERROR
     * OTHER
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> allTheRest(Throwable e, HttpServletRequest request, HttpServletResponse response) {
        Error error = Error.builder()
                .code(ServiceError.E0000.getCode())
                .message(e.getMessage() != null ? e.getMessage() : ServiceError.E0000.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        return responseEntity(error);
    }

    protected ResponseEntity<String> responseEntity(Error error) {
        String body;
        try {
            body = JsonUtils.getObjectMapper().writeValueAsString(error);
        } catch (JsonProcessingException e) {
            body = "Not serializable error body";
        }

        return new ResponseEntity<>(body, error.getStatus());
    }

    @Getter
    @Setter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NoArgsConstructor
    @AllArgsConstructor
    static class Error{
        private HttpStatus status;
        private String code;
        private Object message;
    }
}
