package org.library.system.model;

import java.time.LocalDate;

/**
 * Class representing a book borrowing transaction.
 * Links a Book and a Member together with date tracking.
 */
public class BorrowTransaction {
    private final String transactionId;
    private final Book book;
    private final Member member;
    private final LocalDate borrowDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;

    public BorrowTransaction(String transactionId, Book book, Member member, int durationDays) {
        if (transactionId == null || transactionId.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction ID cannot be empty.");
        }
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null.");
        }
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null.");
        }
        this.transactionId = transactionId.trim();
        this.book = book;
        this.member = member;
        this.borrowDate = LocalDate.now();
        this.dueDate = this.borrowDate.plusDays(durationDays);
        this.returnDate = null;
    }

    public BorrowTransaction(String transactionId, Book book, Member member, 
                             LocalDate borrowDate, LocalDate dueDate, LocalDate returnDate) {
        if (transactionId == null || transactionId.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction ID cannot be empty.");
        }
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null.");
        }
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null.");
        }
        if (borrowDate == null || dueDate == null) {
            throw new IllegalArgumentException("Borrow date and due date cannot be null.");
        }
        this.transactionId = transactionId.trim();
        this.book = book;
        this.member = member;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Book getBook() {
        return book;
    }

    public Member getMember() {
        return member;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public boolean isOverdue() {
        if (returnDate != null) {
            return false; // Already returned
        }
        return LocalDate.now().isAfter(dueDate);
    }

    public void returnBook() {
        this.returnDate = LocalDate.now();
    }

    @Override
    public String toString() {
        return String.format("Tx: %s | Book: %s | Member: %s | Due: %s%s", 
            transactionId, book.getTitle(), member.getName(), dueDate,
            returnDate != null ? " (Returned: " + returnDate + ")" : (isOverdue() ? " [OVERDUE]" : " [ACTIVE]"));
    }
}
