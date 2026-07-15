# BorrowTransaction Service Integration Guide

## 1. Purpose
This document defines how the `BorrowTransactionService` and `BorrowTransactionServiceImpl` interact with the `Library` and `LibraryData` classes. It clarifies what other team members need to provide for smooth integration.

---

## 2. Service Contract
**Interface:** `BorrowTransactionService`

- `BorrowTransaction borrowBook(String memberId, String bookId)`
- `BorrowTransaction returnBook(String transactionId)`
- `List<BorrowTransaction> getActiveTransactions(String memberId)`

**Notes for teammates:**
- `BorrowTransaction` must be fully implemented in `org.library.system.model`.
- `Library.borrowBook` and `Library.returnBook` must align with these signatures. Ideally, `Library.returnBook` should return the updated `BorrowTransaction`. Whoever is taking care of the Library should should update the BorrowTransactionServiceImpl by removing the lookup and the null line in the returnBook method call hence shinking it to;

@Override
public BorrowTransaction returnBook(String transactionId) {
    return libraryData.getLibrary().returnBook(transactionId);
}


---

## 3. Implementation Details
**Class:** `BorrowTransactionServiceImpl`

- Delegates directly to `Library.borrowBook` for validation and transaction creation.
- Calls `Library.returnBook` and then looks up the transaction by ID (unless `Library.returnBook` is updated to return the transaction).
- Filters active transactions by checking `returnDate == null`.

### Code Notes & TODOs
```java
@Override
public BorrowTransaction returnBook(String transactionId) {
    libraryData.getLibrary().returnBook(transactionId); 
    // Invariant: Library.returnBook() throws if ID doesn't exist,
    // so this lookup should always succeed. Null here is not a normal case.
    return libraryData.getLibrary().getTransactions()
            .stream()
            .filter(t -> t.getTransactionId().equals(transactionId))
            .findFirst()
            .orElse(null); // TODO: simplify if Library.returnBook returns BorrowTransaction directly
}
