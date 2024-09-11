package com.developer119.rentalpayments.controller

import com.developer119.rentalpayments.model.Rental
import com.developer119.rentalpayments.service.RentalService
import jakarta.validation.Valid
import org.hibernate.exception.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/rentals")
class RentalController(private val rentalService: RentalService) {

    @PostMapping
    fun createRental(@Valid @RequestBody rental: Rental, bindingResult: BindingResult): ResponseEntity<Any> {
        if (bindingResult.hasErrors()) {
            val errors = bindingResult.fieldErrors.associate {
                it.field to (it.defaultMessage ?: "${it.field}는 유효하지 않습니다")
            }
            return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
        }

        try {
            val createdRental = rentalService.createRental(rental)
            return ResponseEntity(createdRental, HttpStatus.CREATED)
        } catch (e: DataIntegrityViolationException) {
            if (e.cause is ConstraintViolationException) {
                val constraintName = (e.cause as ConstraintViolationException).constraintName
                if (constraintName == "unique_key") {
                    return ResponseEntity(mapOf("error" to "이미 존재하는 키입니다."), HttpStatus.CONFLICT)
                }
            }
            return ResponseEntity(mapOf("error" to "데이터 저장 중 오류가 발생했습니다."), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping
    fun getRentals(@RequestParam(defaultValue = "1") page: Int,
                      @RequestParam(defaultValue = "10") size: Int): ResponseEntity<List<Rental>> {
        val adjustedPage = page - 1  // 내부적으로 0부터 시작하는 페이지 번호로 조정
        val rentals = rentalService.getRentals(adjustedPage, size)
        return ResponseEntity(rentals, HttpStatus.OK)
    }

    @GetMapping("/{key}")
    fun getRental(@PathVariable key: String): ResponseEntity<Rental> {
        val rental = rentalService.getRentalByKey(key)
        return if (rental != null) {
            ResponseEntity(rental, HttpStatus.OK)
        } else {
            return ResponseEntity<Rental>(HttpStatus.NOT_FOUND) // 빈 객체로 반환
        }
    }
}
