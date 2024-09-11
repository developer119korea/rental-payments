package com.developer119.rentalpayments

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration

@SpringBootApplication(scanBasePackages = ["com.developer119.rentalpayments"], exclude = [SecurityAutoConfiguration::class])
@EnableJpaRepositories(basePackages = ["com.developer119.rentalpayments.repository"])
@EntityScan(basePackages = ["com.developer119.rentalpayments.model"])
class RentalPaymentsApplication

fun main(args: Array<String>) {
    runApplication<RentalPaymentsApplication>(*args)
}