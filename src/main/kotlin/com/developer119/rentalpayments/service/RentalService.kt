package com.developer119.rentalpayments.service

import com.developer119.rentalpayments.model.Rental
import com.developer119.rentalpayments.repository.RentalRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

@Service
class RentalService(private val rentalRepository: RentalRepository) {

    fun createRental(rental: Rental): Rental {
        rental.id = UUID.randomUUID() // UUID 생성
        rental.createdAt = LocalDateTime.now()
        rental.updatedAt = LocalDateTime.now()
        return rentalRepository.save(rental)
    }

    fun getRentals(page: Int, size: Int): List<Rental> {
        val pageable: Pageable = PageRequest.of(page, size)
        return rentalRepository.findAll(pageable).content
    }

    fun getRentalByKey(key: String): Rental? {
        return rentalRepository.findByKey(key)
    }
}
