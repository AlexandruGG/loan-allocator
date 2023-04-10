package com.alexg.loanallocator.service

import com.alexg.loanallocator.entity.Investor
import com.alexg.loanallocator.entity.Loan
import com.alexg.loanallocator.entity.LoanCategories
import com.alexg.loanallocator.entity.LoanRiskBands
import com.alexg.loanallocator.enum.LoanCategory
import com.alexg.loanallocator.enum.LoanRiskBand
import com.alexg.loanallocator.repository.LoanRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class LoanAllocatorTest {

    @InjectMocks
    private lateinit var sut: LoanAllocator

    @Mock
    private lateinit var investorService: InvestorService

    @Mock
    private lateinit var loanRepository: LoanRepository

    private val investorBob = UUID.randomUUID()
    private val investorSusan = UUID.randomUUID()
    private val investorGeorge = UUID.randomUUID()

    @Test
    fun `correctly allocates loans for Part I`() {
        given(investorService.getInvestors()).willReturn(getInvestorsPartI())

        val propertyLoan1 = Loan(LoanCategory.PROPERTY, LoanRiskBand.B, BigDecimal(1000))
        val propertyLoan2 = Loan(LoanCategory.PROPERTY, LoanRiskBand.B, BigDecimal(300))
        val retailLoan1 = Loan(LoanCategory.RETAIL, LoanRiskBand.A, BigDecimal(700))
        val retailLoan2 = Loan(LoanCategory.RETAIL, LoanRiskBand.A, BigDecimal(300))

        val loans = mutableListOf(
            propertyLoan1,
            Loan(LoanCategory.MEDICAL, LoanRiskBand.B, BigDecimal(1000)),
            retailLoan1,
            propertyLoan2,
            retailLoan2,
        )
        given(loanRepository.findAll()).willReturn(loans)

        val result = sut.allocate()
        assertThat(result).isEqualTo(
            mapOf(
                investorBob to setOf(propertyLoan1),
                investorSusan to setOf(propertyLoan2, retailLoan1),
                investorGeorge to setOf(retailLoan2),
            ),
        )
    }

    private fun getInvestorsPartI() = setOf(
        Investor(
            id = investorBob,
            name = "Bob",
            funds = BigDecimal(1000),
            preferences = setOf(LoanCategories(setOf(LoanCategory.PROPERTY))),
        ),
        Investor(
            id = investorSusan,
            name = "Susan",
            funds = BigDecimal(1000),
            preferences = setOf(LoanCategories(setOf(LoanCategory.PROPERTY, LoanCategory.RETAIL))),
        ),
        Investor(
            id = investorGeorge,
            name = "George",
            funds = BigDecimal(1000),
            preferences = setOf(LoanRiskBands(setOf(LoanRiskBand.A))),
        ),
    )
}
