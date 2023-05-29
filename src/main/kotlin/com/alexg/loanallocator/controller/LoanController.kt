package com.alexg.loanallocator.controller

import com.alexg.loanallocator.dto.LoanDto
import com.alexg.loanallocator.entity.Loan
import com.alexg.loanallocator.service.LoanService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("loans")
class LoanController(private val service: LoanService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createLoan(@Valid loanDto: LoanDto): LoanDto = service.createLoan(loanDto)

    @GetMapping
    fun getLoans(): Collection<Loan> = service.getLoans()

    @DeleteMapping("{id}")
    fun deleteLoan(@PathVariable id: UUID) = service.deleteLoan(id)
}
