package com.developer119.rentalpayments.service

import com.developer119.rentalpayments.model.Rental
import com.developer119.rentalpayments.repository.RentalRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class RentalService(private val rentalRepository: RentalRepository) {

    fun createRental(rental: Rental): Rental {
        return rentalRepository.save(rental)
    }

    fun getRentals(page: Int, size: Int): Page<Rental> {
        val pageable: Pageable = PageRequest.of(page, size)
        return rentalRepository.findAll(pageable)
    }

    fun getRentalByKey(key: String): Rental? {
        return rentalRepository.findByKey(key)
    }
}
