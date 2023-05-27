package com.alexg.loanallocator.entity

import com.alexg.loanallocator.enum.LoanCategory
import com.alexg.loanallocator.enum.LoanRiskBand
import jakarta.persistence.Entity
import java.math.BigDecimal
import java.util.UUID

@Entity
class Loan(val category: LoanCategory, val riskBand: LoanRiskBand, val amount: BigDecimal) : BaseEntity<UUID>() {
    override fun toString(): String {
        return "Loan(id=$id, category=$category, riskBand=$riskBand, amount=$amount)"
    }
}
