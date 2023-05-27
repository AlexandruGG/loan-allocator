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

    private val investorBob = Investor(
        id = UUID.randomUUID(),
        name = "Bob",
        funds = BigDecimal(1000),
        preferences = setOf(LoanCategories(setOf(LoanCategory.PROPERTY))),
    )
    private val investorSusan = Investor(
        id = UUID.randomUUID(),
        name = "Susan",
        funds = BigDecimal(1000),
        preferences = setOf(LoanCategories(setOf(LoanCategory.PROPERTY, LoanCategory.RETAIL))),
    )
    private val investorGeorge = Investor(
        id = UUID.randomUUID(),
        name = "George",
        funds = BigDecimal(1000),
        preferences = setOf(LoanRiskBands(setOf(LoanRiskBand.A))),
    )

    private val investorHelen = Investor(
        id = UUID.randomUUID(),
        name = "Helen",
        funds = BigDecimal(1000),
        allocationPreference = mapOf(LoanCategory.PROPERTY to BigDecimal(0.40)),
    )
    private val investorJamie = Investor(
        id = UUID.randomUUID(),
        name = "Jamie",
        funds = BigDecimal(1000),
        preferences = setOf(LoanCategories(setOf(LoanCategory.PROPERTY)), LoanRiskBands(setOf(LoanRiskBand.A, LoanRiskBand.B))),
    )

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
                investorGeorge to setOf(retailLoan1, retailLoan2),
                investorSusan to setOf(propertyLoan2),
            ),
        )
    }

    @Test
    fun `maximises the total loan amount allocated based on investor preferences`() {
        given(investorService.getInvestors()).willReturn(getInvestorsPartI())

        val propertyLoan1 = Loan(LoanCategory.PROPERTY, LoanRiskBand.A, BigDecimal(1000))
        val propertyLoan2 = Loan(LoanCategory.PROPERTY, LoanRiskBand.B, BigDecimal(1000))
        val retailLoan1 = Loan(LoanCategory.RETAIL, LoanRiskBand.B, BigDecimal(1000))

        val loans = mutableListOf(
            propertyLoan1,
            propertyLoan2,
            retailLoan1,
        )
        given(loanRepository.findAll()).willReturn(loans)

        val result = sut.allocate()
        assertThat(result).isEqualTo(
            mapOf(
                investorGeorge to setOf(propertyLoan1),
                investorBob to setOf(propertyLoan2),
                investorSusan to setOf(retailLoan1),
            ),
        )
    }

    @Test
    fun `correctly allocates loans for Part II - no property allocation for Helen`() {
        given(investorService.getInvestors()).willReturn(getInvestorsPartII())

        val propertyLoan1 = Loan(LoanCategory.PROPERTY, LoanRiskBand.B, BigDecimal(300))
        val propertyLoan2 = Loan(LoanCategory.PROPERTY, LoanRiskBand.C, BigDecimal(500))
        val propertyLoan3 = Loan(LoanCategory.PROPERTY, LoanRiskBand.B, BigDecimal(400))
        val medicalLoan = Loan(LoanCategory.MEDICAL, LoanRiskBand.A, BigDecimal(300))

        val loans = mutableListOf(
            propertyLoan1,
            medicalLoan,
            propertyLoan2,
            propertyLoan3,
        )
        given(loanRepository.findAll()).willReturn(loans)

        val result = sut.allocate()
        assertThat(result).isEqualTo(
            mapOf(
                investorHelen to setOf(medicalLoan),
                investorJamie to setOf(propertyLoan1, propertyLoan3),
            ),
        )
    }

    @Test
    fun `correctly allocates loans for Part II - with property allocation for Helen`() {
        given(investorService.getInvestors()).willReturn(getInvestorsPartII())

        val propertyLoan1 = Loan(LoanCategory.PROPERTY, LoanRiskBand.B, BigDecimal(300))
        val propertyLoan2 = Loan(LoanCategory.PROPERTY, LoanRiskBand.C, BigDecimal(400))
        val propertyLoan3 = Loan(LoanCategory.PROPERTY, LoanRiskBand.B, BigDecimal(400))
        val medicalLoan = Loan(LoanCategory.MEDICAL, LoanRiskBand.A, BigDecimal(300))

        val loans = mutableListOf(
            propertyLoan1,
            medicalLoan,
            propertyLoan2,
            propertyLoan3,
        )
        given(loanRepository.findAll()).willReturn(loans)

        val result = sut.allocate()
        assertThat(result).isEqualTo(
            mapOf(
                investorHelen to setOf(medicalLoan, propertyLoan2),
                investorJamie to setOf(propertyLoan1, propertyLoan3),
            ),
        )
    }

    private fun getInvestorsPartI() = setOf(investorBob, investorSusan, investorGeorge)
    private fun getInvestorsPartII() = setOf(investorHelen, investorJamie)
}
