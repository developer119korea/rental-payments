package com.developer119.rentalpayments.controller

import com.developer119.rentalpayments.model.Rental
import com.developer119.rentalpayments.service.RentalService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.sql.SQLException

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RentalControllerTest @Autowired constructor(
    private val mockMvc: MockMvc
) {
    @MockBean
    private lateinit var rentalService: RentalService

    @Test
    @DisplayName("유효한 렌탈 정보로 요청 시 CREATED 상태를 반환해야 함")
    fun `should return CREATED when rental is valid`() {
        // given
        val rental = Rental("testKey", "ACTIVE", "BILL123", "Product", 12, 1000, 1, 6)
        given(rentalService.createRental(any())).willReturn(rental)

        // when & then
        mockMvc.perform(post("/rentals")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"key":"testKey","status":"ACTIVE","billingCode":"BILL123","productName":"Product","installmentPlan":12,"installmentAmount":1000,"paymentDay":1,"paymentPeriod":6}
            """.trimIndent()))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.key").value("testKey"))
            .andExpect(jsonPath("$.status").value("ACTIVE"))
    }

    @Test
    @DisplayName("유효하지 않은 렌탈 정보로 요청 시 BAD REQUEST 상태를 반환해야 함")
    fun `should return BAD REQUEST when rental is invalid`() {
        // when & then
        mockMvc.perform(post("/rentals")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"key":"", "status":"", "billingCode":"", "productName":"", "installmentPlan":0, "installmentAmount":0, "paymentDay":0, "paymentPeriod":0}
            """))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.key").value("키는 필수 입력값입니다"))
    }

    @Test
    @DisplayName("이미 존재하는 렌탈 키로 요청 시 CONFLICT 상태를 반환해야 함")
    fun `should return CONFLICT when rental key already exists`() {
        // given
        given(rentalService.createRental(any()))
            .willThrow(DataIntegrityViolationException("Unique index or primary key violation", SQLException("Duplicate entry")))

        // when & then
        mockMvc.perform(post("/rentals")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"key":"testKey","status":"ACTIVE","billingCode":"BILL123","productName":"Product","installmentPlan":12,"installmentAmount":1000,"paymentDay":1,"paymentPeriod":6}
            """))
            .andExpect(status().isConflict)
            .andReturn()
    }

    @Test
    @DisplayName("알 수 없는 오류 발생 시 INTERNAL SERVER ERROR 상태를 반환해야 함")
    fun `should return INTERNAL SERVER ERROR on unknown error`() {
        // given
        given(rentalService.createRental(any())).willThrow(RuntimeException("Unexpected error"))

        // when & then
        mockMvc.perform(post("/rentals")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
            {"key":"testKey","status":"ACTIVE","billingCode":"BILL123","productName":"Product","installmentPlan":12,"installmentAmount":1000,"paymentDay":1,"paymentPeriod":6}
        """))
            .andExpect(status().isInternalServerError)
            .andExpect(jsonPath("$.error").value("데이터 저장 중 오류가 발생했습니다."))
    }

    @Test
    @DisplayName("렌탈 목록 조회 시 OK 상태와 렌탈 목록을 반환해야 함")
    fun `getRentals should return OK and list of rentals`() {
        val rentals = listOf(Rental(
            key = "testKey",
            status = "ACTIVE",
            billingCode = "BILL123",
            productName = "Product",
            installmentPlan = 12,
            installmentAmount = 1000,
            paymentDay = 1,
            paymentPeriod = 6,
        ))

        given(rentalService.getRentals(0, 10)).willReturn(rentals)

        mockMvc.perform(get("/rentals?page=1&size=10"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].key").value("testKey"))
            .andExpect(jsonPath("$[0].status").value("ACTIVE"))
    }

    @Test
    @DisplayName("존재하는 렌탈 조회 시 OK 상태를 반환해야 함")
    fun `getRental should return OK when rental exists`() {
        val rental = Rental(
            key = "testKey",
            status = "ACTIVE",
            billingCode = "BILL123",
            productName = "Product",
            installmentPlan = 12,
            installmentAmount = 1000,
            paymentDay = 1,
            paymentPeriod = 6,
        )

        given(rentalService.getRentalByKey("testKey")).willReturn(rental)

        mockMvc.perform(get("/rentals/testKey"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.key").value("testKey"))
            .andExpect(jsonPath("$.status").value("ACTIVE"))
    }

    @Test
    @DisplayName("존재하지 않는 렌탈 조회 시 NOT_FOUND 상태를 반환해야 함")
    fun `getRental should return NOT_FOUND when rental does not exist`() {
        given(rentalService.getRentalByKey("nonExistentKey")).willReturn(null)

        mockMvc.perform(get("/rentals/nonExistentKey"))
            .andExpect(status().isNotFound)
    }
}
