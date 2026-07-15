package org.library.system.service;

import java.util.List;
import org.library.system.model.BorrowTransaction;

public interface BorrowTransactionService {
    /**
     * Attempts to check out a book.
     * @param memberId The ID of the member.
     * @param bookId The ID of the book.
     * @return The created BorrowTransaction record.
     * @throws IllegalArgumentException if member is suspended, 
     *                                  book does not exist, or no copies available.
     */
    BorrowTransaction borrowBook(String memberId, String bookId) throws IllegalArgumentException;

    /**
     * Returns a borrowed book.
     * @param transactionId The ID of the active transaction.
     * @return The updated BorrowTransaction record with return date set.
     */
    BorrowTransaction returnBook(String transactionId);

    /**
     * Gets all currently active (unreturned) borrow transactions for a member.
     * @param memberId The ID of the member.
     * @return List of active BorrowTransaction records.
     */
    List<BorrowTransaction> getActiveTransactions(String memberId);
}
