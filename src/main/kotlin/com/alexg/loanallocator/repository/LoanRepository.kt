package com.alexg.loanallocator.repository

import com.alexg.loanallocator.entity.Loan
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface LoanRepository : JpaRepository<Loan, UUID>
