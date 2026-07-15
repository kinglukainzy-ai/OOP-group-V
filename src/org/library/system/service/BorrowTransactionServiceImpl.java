package org.library.system.service;

import java.util.List;
import java.util.stream.Collectors;
import org.library.system.model.BorrowTransaction;
import org.library.system.util.LibraryData;

public class BorrowTransactionServiceImpl implements BorrowTransactionService {

    private final LibraryData libraryData;

    public BorrowTransactionServiceImpl(LibraryData libraryData) {
        this.libraryData = libraryData;
    }

    @Override
    public BorrowTransaction borrowBook(String memberId, String bookId) {
        // Delegate directly to Library; it handles validation
        return libraryData.getLibrary().borrowBook(memberId, bookId);
    }

    @Override
    public BorrowTransaction returnBook(String transactionId) {
        // Option B workaround if Library.returnBook is void
        libraryData.getLibrary().returnBook(transactionId);
        return libraryData.getLibrary().getTransactions()
                .stream()
                .filter(t -> t.getTransactionId().equals(transactionId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<BorrowTransaction> getActiveTransactions(String memberId) {
        return libraryData.getLibrary().getTransactions()
                .stream()
                .filter(t -> t.getMember().getMemberId().equals(memberId) && t.getReturnDate() == null)
                .collect(Collectors.toList());
    }
}
