package com.alexg.loanallocator.service

import com.alexg.loanallocator.dto.LoanDto
import com.alexg.loanallocator.entity.Loan
import com.alexg.loanallocator.repository.LoanRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class LoanService(private val repository: LoanRepository) {
    fun createLoan(loanDto: LoanDto): LoanDto = repository.save(loanDto.toEntity()).let { loanDto.copy(id = it.id) }

    fun getLoans(): Collection<Loan> = repository.findAll()

    fun deleteLoan(id: UUID) = repository.deleteById(id)
}
