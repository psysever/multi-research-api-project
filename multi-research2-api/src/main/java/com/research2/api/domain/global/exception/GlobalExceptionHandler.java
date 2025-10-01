package com.research2.api.domain.global.exception;


import com.research2.api.domain.global.exception.error.ErrorCode;
import com.research2.api.domain.global.exception.error.ErrorCodes.CommonErrorCode;
import com.research2.api.domain.global.exception.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class) // ①
    public ResponseEntity<Object> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }


    /**
     * @valid 유효성 체크 미통과 시 MethodArgumentNotValidException 발생 현재 controller parameter를 직업 validation해서
     * parameter model에 validation 추가 후 exception 에서 자동 처리 하도록 예정 sample PetInfoController
     * createPetInfo @RequestBody @valid 입력 후 테스트(기본 null 이외에 min, max등등도 처리 예정)
     */

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(makeErrorResponse(errorCode, errorCode.getMessage()));
    }

    @ExceptionHandler(ArithmeticException.class) // ③
    public ResponseEntity<Object> handleArithmeticException(ArithmeticException e) {
        ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
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
