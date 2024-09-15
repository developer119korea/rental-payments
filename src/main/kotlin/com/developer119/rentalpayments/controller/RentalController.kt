package com.developer119.rentalpayments.controller

import com.developer119.rentalpayments.model.Rental
import com.developer119.rentalpayments.service.RentalService
import com.developer119.rentalpayments.exception.ResourceNotFoundException
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.data.domain.Page

@RestController
@RequestMapping("/rentals")
class RentalController(private val rentalService: RentalService) {

    @PostMapping
    fun createRental(@Valid @RequestBody rental: Rental): ResponseEntity<Rental> {
        val createdRental = rentalService.createRental(rental)
        return ResponseEntity(createdRental, HttpStatus.CREATED)
    }

    @GetMapping("/{key}")
    fun getRental(@PathVariable key: String): ResponseEntity<Rental> {
        val rental = rentalService.getRentalByKey(key)
        return rental?.let { ResponseEntity(it, HttpStatus.OK) }
            ?: throw ResourceNotFoundException("렌탈 정보를 조회할 수 없습니다 key: $key")
    }

    @GetMapping
    fun getRentals(@RequestParam(defaultValue = "0") page: Int,
                   @RequestParam(defaultValue = "10") size: Int): ResponseEntity<Page<Rental>> {
        val result = rentalService.getRentals(page, size)
        return ResponseEntity(result, HttpStatus.OK)
    }
}
