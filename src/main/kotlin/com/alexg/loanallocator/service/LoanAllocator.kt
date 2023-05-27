package com.alexg.loanallocator.service

import com.alexg.loanallocator.entity.Investor
import com.alexg.loanallocator.entity.Loan
import com.alexg.loanallocator.repository.LoanRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class LoanAllocator(val investorService: InvestorService, val loanRepository: LoanRepository) {

    fun allocate(): Map<Investor, Set<Loan>> {
        val loans = loanRepository.findAll()
        val investors = investorService
            .getInvestors()
            .sortedBy { investor -> loans.count { loan -> investor.matches(loan) } }

        return investors.associate { investor ->
            val allocatedLoans = mutableSetOf<Loan>()

            loans.forEach { loan ->
                if (investor.funds <= BigDecimal.ZERO) {
                    return@forEach
                }

                if (investor.matches(loan)) {
                    allocatedLoans.add(loan)
                    investor.funds -= loan.amount
                    investor.currentAllocation.add(loan)
                }
            }

            loans.removeAll(allocatedLoans)

            investor to allocatedLoans
        }
    }
}
