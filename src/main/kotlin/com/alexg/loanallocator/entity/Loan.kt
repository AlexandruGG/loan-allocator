package com.alexg.loanallocator.entity

import com.alexg.loanallocator.enum.LoanCategory
import com.alexg.loanallocator.enum.RiskBand
import jakarta.persistence.Entity
import java.math.BigDecimal
import java.util.UUID

@Entity
class Loan(val category: LoanCategory, val riskBand: RiskBand, val amount: BigDecimal) : BaseEntity<UUID>()
