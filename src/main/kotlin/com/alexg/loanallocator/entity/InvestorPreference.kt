package com.alexg.loanallocator.entity

import com.alexg.loanallocator.enum.LoanCategory
import com.alexg.loanallocator.enum.LoanRiskBand

sealed class InvestorPreference {
    abstract fun fitsLoan(loan: Loan): Boolean
}

data class LoanCategories(val categories: Set<LoanCategory>) : InvestorPreference() {
    override fun fitsLoan(loan: Loan) = loan.category in categories
}

data class LoanRiskBands(val riskBands: Set<LoanRiskBand>) : InvestorPreference() {
    override fun fitsLoan(loan: Loan) = loan.riskBand in riskBands
}
