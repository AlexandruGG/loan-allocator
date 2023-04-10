package com.alexg.loanallocator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LoanAllocatorApplication

fun main(args: Array<String>) {
	runApplication<LoanAllocatorApplication>(*args)
}
