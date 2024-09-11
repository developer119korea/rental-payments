package com.developer119.rentalpayments.repository

import com.developer119.rentalpayments.model.Rental
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface RentalRepository : JpaRepository<Rental, UUID> {
    fun findByKey(key: String): Rental?
}
