package com.alexg.loanallocator.dto

import com.alexg.loanallocator.entity.Loan
import com.alexg.loanallocator.enum.LoanCategory
import com.alexg.loanallocator.enum.LoanRiskBand
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.util.UUID

data class LoanDto(
    val id: UUID? = null,
    val category: LoanCategory,
    val riskBand: LoanRiskBand,

    @field:Positive
    val amount: BigDecimal,
) {
    fun toEntity(): Loan = Loan(category = category, riskBand = riskBand, amount = amount)
}
