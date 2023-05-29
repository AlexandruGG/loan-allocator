package com.alexg.loanallocator.entity

import com.alexg.loanallocator.enum.LoanCategory
import com.alexg.loanallocator.enum.LoanRiskBand
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.util.UUID

@Entity
class Loan(
    @field:Enumerated(EnumType.STRING)
    val category: LoanCategory,

    @field:Enumerated(EnumType.STRING)
    val riskBand: LoanRiskBand,

    @field:Positive
    val amount: BigDecimal,
) : BaseEntity<UUID>() {
    override fun toString(): String {
        return "Loan(id=$id, category=$category, riskBand=$riskBand, amount=$amount)"
    }
}
