package com.developer119.rentalpayments.model

import jakarta.persistence.*
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "rentals")
class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "uuid")
    var id: UUID = UUID.randomUUID() // UUID는 자동 생성됨

    @NotBlank(message = "키는 필수 입력값입니다")
    @Column(name = "key", length = 255, nullable = false)
    var key: String

    @NotBlank(message = "상태는 필수 입력값입니다")
    @Column(name = "status", length = 255, nullable = false)
    var status: String

    @NotBlank(message = "빌링 코드는 필수 입력값입니다")
    @Column(name = "billing_code", length = 255, nullable = false)
    var billingCode: String

    @NotBlank(message = "제품명은 필수 입력값입니다")
    @Column(name = "product_name", length = 255, nullable = false)
    var productName: String

    @NotNull(message = "분납 기간은 필수 입력값입니다")
    @Min(value = 1, message = "할부 계획은 1 이상이어야 합니다")
    @Column(name = "installment_plan", nullable = false)
    var installmentPlan: Int

    @NotNull(message = "분납 금액은 필수 입력값입니다")
    @Min(value = 0, message = "할부 금액은 0 이상이어야 합니다")
    @Column(name = "installment_amount", nullable = false)
    var installmentAmount: Int

    @NotNull(message = "결제일은 필수 입력값입니다")
    @Min(value = 1, message = "결제일은 1 이상이어야 합니다")
    @Max(value = 28, message = "결제일은 28 이하여야 합니다")
    @Column(name = "payment_day", nullable = false)
    var paymentDay: Int

    @NotNull(message = "결제 주기는 필수 입력값입니다")
    @Min(value = 1, message = "결제 기간은 1 이상이어야 합니다")
    @Column(name = "payment_period", nullable = false)
    var paymentPeriod: Int

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()

    constructor(
        key: String,
        status: String,
        billingCode: String,
        productName: String,
        installmentPlan: Int,
        installmentAmount: Int,
        paymentDay: Int,
        paymentPeriod: Int
    ) {
        this.key = key
        this.status = status
        this.billingCode = billingCode
        this.productName = productName
        this.installmentPlan = installmentPlan
        this.installmentAmount = installmentAmount
        this.paymentDay = paymentDay
        this.paymentPeriod = paymentPeriod
    }

    // 엔티티가 저장되기 전에 호출되는 메서드 (생성 시점)
    @PrePersist
    fun onPrePersist() {
        createdAt = LocalDateTime.now() // 생성일자 설정
        updatedAt = LocalDateTime.now() // 처음 생성될 때 업데이트 일자도 설정
    }

    // 엔티티가 업데이트되기 전에 호출되는 메서드
    @PreUpdate
    fun onPreUpdate() {
        updatedAt = LocalDateTime.now() // 업데이트 일자만 설정
    }

    override fun toString(): String {
        return "Rental(id=$id, key=$key, status=$status, billingCode=$billingCode, productName=$productName, installmentPlan=$installmentPlan, installmentAmount=$installmentAmount, paymentDay=$paymentDay, paymentPeriod=$paymentPeriod, createdAt=$createdAt, updatedAt=$updatedAt)"
    }
}
