package com.alexg.loanallocator.entity

import com.alexg.loanallocator.enum.LoanCategory
import com.alexg.loanallocator.enum.LoanRiskBand
import com.alexg.loanallocator.repository.LoanRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal

@SpringBootTest
class BasicEntityTest(@Autowired private val loanRepository: LoanRepository) {

    @Test
    fun `basic entity checks`() {
        val loan = Loan(category = LoanCategory.PROPERTY, riskBand = LoanRiskBand.A, amount = BigDecimal(1000))
        val hashCodeBefore = loan.hashCode()
        val personSet = hashSetOf(loan)

        loanRepository.save(loan)
        val hashCodeAfter = loan.hashCode()

        assertThat(loanRepository.findAll()).hasSize(1)
        assertThat(personSet).contains(loan)
        assertThat(hashCodeAfter).isEqualTo(hashCodeBefore)
    }
}
