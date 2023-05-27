package com.alexg.loanallocator.entity

import com.alexg.loanallocator.enum.LoanCategory
import com.alexg.loanallocator.enum.LoanRiskBand
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.UUID

data class Investor(
    val id: UUID,
    val name: String,
    var funds: BigDecimal,
    val preferences: Set<Preference> = emptySet(),
    val allocationPreference: Map<LoanCategory, BigDecimal>? = null,
    var currentAllocation: MutableSet<Loan> = mutableSetOf(),
) {
    fun matches(loan: Loan) = funds >= loan.amount &&
        preferences.all { it.matches(loan) } &&
        allocationPreference?.all { entry ->
            val currAmountForAllocation = currentAllocation.filter { it.category == entry.key }.sumOf { it.amount }
            val totalFunds = funds + currentAllocation.sumOf { it.amount }

            if (loan.category in allocationPreference) {
                (currAmountForAllocation + loan.amount).divide(totalFunds, 2, RoundingMode.CEILING) <= entry.value
            } else {
                currAmountForAllocation.divide(totalFunds, 2, RoundingMode.CEILING) <= entry.value
            }
        } ?: true
}

sealed class Preference {
    abstract fun matches(loan: Loan): Boolean
}

data class LoanCategories(val categories: Set<LoanCategory>) : Preference() {
    override fun matches(loan: Loan) = loan.category in categories
}

data class LoanRiskBands(val riskBands: Set<LoanRiskBand>) : Preference() {
    override fun matches(loan: Loan) = loan.riskBand in riskBands
}
