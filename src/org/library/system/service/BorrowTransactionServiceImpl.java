package org.library.system.service;

import java.util.List;
import java.util.stream.Collectors;
import org.library.system.model.Book;
import org.library.system.model.Member;
import org.library.system.model.BorrowTransaction;
import org.library.system.util.LibraryData;

public class BorrowTransactionServiceImpl implements BorrowTransactionService {

    private final LibraryData libraryData;

    public BorrowTransactionServiceImpl(LibraryData libraryData) {
        this.libraryData = libraryData;
    }

    @Override
    public BorrowTransaction borrowBook(String memberId, String bookId) throws IllegalArgumentException {
        Member member = libraryData.getLibrary().getMembers()
                .stream()
                .filter(m -> m.getMemberId().equals(memberId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        if (member.isSuspended()) {
            throw new IllegalArgumentException("Member is suspended and cannot borrow books.");
        }

        Book book = libraryData.getLibrary().getBooks()
                .stream()
                .filter(b -> b.getBookId().equals(bookId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        // Delegate borrowing logic to Library aggregate
        return libraryData.getLibrary().borrowBook(memberId, bookId);
    }

    @Override
    public BorrowTransaction returnBook(String transactionId) {
        return libraryData.getLibrary().returnBook(transactionId);
    }

    @Override
    public List<BorrowTransaction> getActiveTransactions(String memberId) {
        return libraryData.getLibrary().getTransactions()
                .stream()
                .filter(t -> t.getMember().getMemberId().equals(memberId) && t.getReturnDate() == null)
                .collect(Collectors.toList());
    }
}
