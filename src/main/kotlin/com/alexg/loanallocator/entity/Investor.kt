package com.alexg.loanallocator.entity

import java.math.BigDecimal
import java.util.UUID

data class Investor(val id: UUID, val name: String, var funds: BigDecimal, val preferences: Set<InvestorPreference>) {

    fun fitsLoan(loan: Loan) = funds >= loan.amount && preferences.all { it.fitsLoan(loan) }
}
