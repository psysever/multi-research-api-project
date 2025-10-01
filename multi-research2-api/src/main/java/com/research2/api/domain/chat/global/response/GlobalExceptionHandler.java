package com.research2.api.domain.chat.global.response;

import com.research2.api.domain.chat.global.error.CustomException;
import com.research2.api.domain.chat.global.error.ErrorCode;
import com.research2.api.domain.chat.global.error.ErrorCodes;
import com.research2.api.domain.chat.global.error.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;


@RestControllerAdvice

public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException e) {
        log.warn("CustomException ERROR: {}, MESSAGE: {}", e.getErrorCode(), e.getMessage(), e);
        ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }


    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode, errorCode.getMessage()));
    }

    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<Object> handleArithmeticException(ArithmeticException e) {
        ErrorCode errorCode = ErrorCodes.CommonErrorCode.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(errorCode, errorCode.getMessage());
    }


    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, String message) {
        return ResponseEntity.status(errorCode.getCode())
                .body(makeErrorResponse(errorCode, message));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode, String message) {
        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(message)
                .build();
    }


    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = makeErrorResponse(e.getBindingResult());
        return ResponseEntity.status(errorResponse.getCode())
                .body(new ErrorResponse(400, errorResponse.getMessage()));
    }


    private ErrorResponse makeErrorResponse(BindingResult bindingResult) {
        String code = "";
        String message = "";

        if (bindingResult.hasErrors()) {
            code = Objects.requireNonNull(bindingResult.getFieldError()).getCode();
            message = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
        }

        assert code != null;
        return ErrorResponse.builder()
                .code(400)
                .message(message)
                .build();

    }
}
