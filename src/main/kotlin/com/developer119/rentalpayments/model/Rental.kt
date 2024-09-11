package com.developer119.rentalpayments.model

import java.time.LocalDateTime
import java.util.*
import jakarta.persistence.*
import jakarta.validation.constraints.*

@Entity
@Table(name = "rentals")
class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "uuid")
    var id: UUID? = UUID.randomUUID()

    @NotBlank(message = "키는 필수 입력값입니다")
    @Column(name = "key", length = 255, nullable = false)
    var key: String? = null

    @NotBlank(message = "상태는 필수 입력값입니다")
    @Column(name = "status", length = 255, nullable = false)
    var status: String? = null

    @NotBlank(message = "빌링 코드는 필수 입력값입니다")
    @Column(name = "billing_code", length = 255, nullable = false)
    var billingCode: String? = null

    @NotBlank(message = "제품명은 필수 입력값입니다")
    @Column(name = "product_name", length = 255, nullable = false)
    var productName: String? = null

    @NotNull(message = "분납 기간은 필수 입력값입니다")
    @Min(value = 1, message = "할부 계획은 1 이상이어야 합니다")
    @Column(name = "installment_plan", nullable = false)
    var installmentPlan: Int? = null

    @NotNull(message = "분납 금액은 필수 입력값입니다")
    @Min(value = 0, message = "할부 금액은 0 이상이어야 합니다")
    @Column(name = "installment_amount", nullable = false)
    var installmentAmount: Int? = null

    @NotNull(message = "결제일은 필수 입력값입니다")
    @Min(value = 1, message = "결제일은 1 이상이어야 합니다")
    @Max(value = 28, message = "결제일은 28 이하여야 합니다")
    @Column(name = "payment_day", nullable = false)
    var paymentDay: Int? = null

    @NotNull(message = "결제 주기는 필수 입력값입니다")
    @Min(value = 1, message = "결제 기간은 1 이상이어야 합니다")
    @Column(name = "payment_period", nullable = false)
    var paymentPeriod: Int? = null

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime? = null

    constructor()

    constructor(
        id: UUID?, key: String?, status: String?, billingCode: String?, productName: String?,
        installmentPlan: Int?, installmentAmount: Int?, paymentDay: Int?,
        paymentPeriod: Int?, createdAt: LocalDateTime?, updatedAt: LocalDateTime?
    ) {
        this.id = id
        this.key = key
        this.status = status
        this.billingCode = billingCode
        this.productName = productName
        this.installmentPlan = installmentPlan
        this.installmentAmount = installmentAmount
        this.paymentDay = paymentDay
        this.paymentPeriod = paymentPeriod
        this.createdAt = createdAt
        this.updatedAt = updatedAt
    }
}