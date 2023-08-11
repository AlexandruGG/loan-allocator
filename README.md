# Loan Allocator Problem

This project presents a solution to a loan allocation problem. The requirement is to write code that takes
as input a set of loans and a set of investors and returns as output a structure indicating which loans
have been allocated to each investor. Each loan can only be allocated to a single investor, and investors
will only fund loans that meet their specified criteria.

## Loans

Loans are modelled such that they have the following attributes:

- _id_ - The unique ID of the loan
- _category_ - The category of the loan (Property, Retail, Medical)
- _risk_band_ - The risk band associated with the loan (A+, A, B, C, D, E)
- _amount_ - The amount of the loan in £

An example loan would look something like:

```json
{
  "id": 1234567,
  "category": "property",
  "risk_band": "A",
  "amount": 1000
}
```

## Investors

The code should fairly allocate loans according to certain investor specified criteria. Investors should
also not be able to fund more loans than they have money to invest.

**Part I**

- _Bob_ - only wants to invest in property loans.
- _Susan_ - will invest in either property or retail loans.
- _George_ - only wants to invest in "A" grade loans.

**Part II**

- _Helen_ - wants to invest in a maximum of 40% property loans.
- _Jamie_ - wants to invest only in high grade (>= B) property loans.

## Notes

- All state should be handled in memory
- The way I/O is handled does not matter
- There is no fixed input other than the investors' requirements
