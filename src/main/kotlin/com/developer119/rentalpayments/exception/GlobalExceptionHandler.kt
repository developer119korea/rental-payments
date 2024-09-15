package com.developer119.rentalpayments.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.dao.DataIntegrityViolationException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errors = ex.bindingResult.fieldErrors.associate {
            it.field to (it.defaultMessage ?: "${it.field}는 유효하지 않습니다")
        }
        return ResponseEntity(ErrorResponse("유효성 검사 실패", errors), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse("잘못된 요청", "요청 본문이 누락되었거나 잘못되었습니다. 올바른 JSON 형식인지 확인해주세요."), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolation(ex: DataIntegrityViolationException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse("데이터 무결성 위반", "이미 존재하는 데이터입니다."), HttpStatus.CONFLICT)
    }

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFound(ex: ResourceNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse("리소스 없음", "리소스를 찾을 수 없습니다"), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse("서버 오류", "예기치 않은 오류가 발생했습니다."), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}

data class ErrorResponse(val error: String, val message: Any)

class ResourceNotFoundException(message: String) : RuntimeException(message)
