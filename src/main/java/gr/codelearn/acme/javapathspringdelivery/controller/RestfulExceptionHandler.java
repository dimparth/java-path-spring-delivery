package gr.codelearn.acme.javapathspringdelivery.controller;

import gr.codelearn.acme.javapathspringdelivery.base.BaseComponent;
import gr.codelearn.acme.javapathspringdelivery.transfer.ApiError;
import gr.codelearn.acme.javapathspringdelivery.transfer.ApiResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestControllerAdvice
public class RestfulExceptionHandler extends BaseComponent {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ApiResponse<?>> handleAllExceptions(final Exception ex, final WebRequest request) {
        logger.error("Unexpected exception occurred.", ex);
        ResponseEntity.internalServerError();
        return new ResponseEntity<>(
                ApiResponse.builder().apiError(getApiError(ex, HttpStatus.INTERNAL_SERVER_ERROR, request)).build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public final ResponseEntity<ApiResponse<?>> handleNonExistence(final NoSuchElementException ex,
                                                                   final WebRequest request) {
        logger.error("Reference to a non-existent object.", ex);
        return new ResponseEntity<>(ApiResponse.builder().apiError(
                getApiError(ex, HttpStatus.NOT_FOUND, request, "Reference to a non-existent object.")).build(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataAccessException.class)
    public final ResponseEntity<ApiResponse<?>> handleDataErrors(final DataAccessException ex,
                                                                 final WebRequest request) {
        logger.error("There was something wrong while interacting with the associated database.", ex);
        return new ResponseEntity<>(
                ApiResponse.builder().apiError(getApiError(ex, HttpStatus.NOT_ACCEPTABLE, request)).build(),
                HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<ApiResponse<?>> handleDataConstraintErrors(final DataIntegrityViolationException ex,
                                                                           final WebRequest request) {
        var customMessage = "There was a conflict while interacting with the associated database. Make sure the " +
                "data submitted does not include already existing values in fields such as ids and serial " +
                "numbers.";
        logger.error("{}", customMessage, ex);

        return new ResponseEntity<>(
                ApiResponse.builder().apiError(getApiError(ex, HttpStatus.NOT_ACCEPTABLE, request, customMessage))
                        .build(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ApiResponse<?>> handleMissingServletRequestParameter(
            final MissingServletRequestParameterException ex, final WebRequest request) {
        logger.error("There was a parameter missing from incoming request", ex);
        return new ResponseEntity<>(
                ApiResponse.builder().apiError(getApiError(ex, HttpStatus.BAD_REQUEST, request)).build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                          final WebRequest request) {
        logger.error("Method argument is invalid.", ex);
        return new ResponseEntity<>(
                ApiResponse.builder().apiError(getApiError(ex, HttpStatus.BAD_REQUEST, request)).build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex,
                                                                           final WebRequest request) {
        logger.error("Method argument, although matched, is of wrong type.", ex);
        return new ResponseEntity<>(
                ApiResponse.builder().apiError(getApiError(ex, HttpStatus.BAD_REQUEST, request)).build(),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<ApiResponse<?>> handleTimeout(final TimeoutException ex,
                                                                           final WebRequest request) {
        logger.error("took longer than 200ms to produce a response", ex);
        return new ResponseEntity<>(
                ApiResponse.builder().apiError(getApiError(ex, HttpStatus.REQUEST_TIMEOUT, request)).build(),
                HttpStatus.REQUEST_TIMEOUT);
    }
    @ExceptionHandler(ExecutionException.class)
    public ResponseEntity<ApiResponse<?>> handleExecutionException(final ExecutionException ex,
                                                        final WebRequest request) {
        logger.error("took longer than 200ms to produce a response", ex);
        return new ResponseEntity<>(
                ApiResponse.builder().apiError(getApiError(ex, HttpStatus.INTERNAL_SERVER_ERROR, request)).build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(InterruptedException.class)
    public ResponseEntity<ApiResponse<?>> handleInterruptedException(final InterruptedException ex,
                                                                   final WebRequest request) {
        logger.error("took longer than 200ms to produce a response", ex);
        return new ResponseEntity<>(
                ApiResponse.builder().apiError(getApiError(ex, HttpStatus.INTERNAL_SERVER_ERROR, request)).build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalStateException(final IllegalStateException ex,
                                                                     final WebRequest request) {
        logger.error("illegal state error", ex);
        return new ResponseEntity<>(
                ApiResponse.builder().apiError(getApiError(ex, HttpStatus.CONFLICT, request)).build(),
                HttpStatus.CONFLICT);
    }

    private ApiError getApiError(final Exception ex, final HttpStatus status, final WebRequest request) {
        String path = request.getDescription(false);
        if (path.indexOf("uri=") == 0) {
            path = StringUtils.replace(path, "uri=", "");
        }
        return ApiError.builder().status(status.value()).message(ex.getMessage()).path(path).build();
    }

    private ApiError getApiError(final Exception ex, final HttpStatus status, final WebRequest request,
                                 String customMessage) {
        String path = request.getDescription(false);
        if (path.indexOf("uri=") == 0) {
            path = StringUtils.replace(path, "uri=", "");
        }
        return ApiError.builder().status(status.value()).message(customMessage != null ? customMessage : ex.getMessage()).path(path).build();
    }
}
