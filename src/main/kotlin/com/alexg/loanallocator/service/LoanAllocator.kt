package com.alexg.loanallocator.service

import com.alexg.loanallocator.entity.Loan
import com.alexg.loanallocator.repository.LoanRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.UUID

@Service
class LoanAllocator(val investorService: InvestorService, val loanRepository: LoanRepository) {

    fun allocate(): Map<UUID, Set<Loan>> {
        val loans = loanRepository.findAll()
        val investors = investorService.getInvestors()

        return investors.associate {
            val allocatedLoans = mutableSetOf<Loan>()

            loans.forEach { loan ->
                if (it.funds <= BigDecimal.ZERO) {
                    return@forEach
                }

                if (it.fitsLoan(loan)) {
                    allocatedLoans.add(loan)
                    it.funds -= loan.amount
                }
            }
            loans.removeAll(allocatedLoans)

            it.id to allocatedLoans
        }
    }
}
