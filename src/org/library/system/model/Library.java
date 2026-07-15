package org.library.system.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing the aggregate library catalog and membership.
 * Integrates collections and core business boundaries.
 */
public class Library {
    private final List<Book> books;
    private final List<Member> members;
    private final List<BorrowTransaction> transactions;

    public Library() {
        this.books = new ArrayList<>();
        this.members = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }

    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null.");
        }
        for (Book b : books) {
            if (b.getBookId().equals(book.getBookId())) {
                throw new IllegalArgumentException("Book with ID " + book.getBookId() + " already exists.");
            }
        }
        books.add(book);
    }

    public void removeBook(String bookId) {
        if (bookId == null || bookId.trim().isEmpty()) {
            throw new IllegalArgumentException("Book ID cannot be empty.");
        }
        books.removeIf(b -> b.getBookId().equals(bookId.trim()));
    }

    public void registerMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null.");
        }
        for (Member m : members) {
            if (m.getMemberId().equals(member.getMemberId())) {
                throw new IllegalArgumentException("Member with ID " + member.getMemberId() + " already registered.");
            }
        }
        members.add(member);
    }

    public List<Book> searchBook(String query) {
        List<Book> results = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>(books);
        }
        String cleanQuery = query.trim().toLowerCase();
        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(cleanQuery) || 
                b.getAuthor().toLowerCase().contains(cleanQuery) || 
                b.getBookId().toLowerCase().contains(cleanQuery)) {
                results.add(b);
            }
        }
        return results;
    }

    public BorrowTransaction borrowBook(String memberId, String bookId) {
        Member member = null;
        for (Member m : members) {
            if (m.getMemberId().equals(memberId)) {
                member = m;
                break;
            }
        }
        if (member == null) {
            throw new IllegalArgumentException("Member not found.");
        }
        if (member.isSuspended()) {
            throw new IllegalArgumentException("Member is suspended and cannot borrow books.");
        }

        Book book = null;
        for (Book b : books) {
            if (b.getBookId().equals(bookId)) {
                book = b;
                break;
            }
        }
        if (book == null) {
            throw new IllegalArgumentException("Book not found.");
        }

        // If PhysicalBook, check available copies
        if (book instanceof PhysicalBook) {
            PhysicalBook physicalBook = (PhysicalBook) book;
            if (!physicalBook.borrowCopy()) {
                throw new IllegalArgumentException("No copies of physical book available.");
            }
        }

        // Create transaction
        String txId = "TX" + (transactions.size() + 1001);
        BorrowTransaction transaction = new BorrowTransaction(txId, book, member, book.getBorrowDurationDays());
        transactions.add(transaction);
        return transaction;
    }

    public void returnBook(String transactionId) {
        BorrowTransaction transaction = null;
        for (BorrowTransaction tx : transactions) {
            if (tx.getTransactionId().equals(transactionId)) {
                transaction = tx;
                break;
            }
        }
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction not found.");
        }
        if (transaction.getReturnDate() != null) {
            throw new IllegalArgumentException("Book has already been returned for this transaction.");
        }

        // Mark as returned
        transaction.returnBook();

        // Increment copies if physical
        Book book = transaction.getBook();
        if (book instanceof PhysicalBook) {
            ((PhysicalBook) book).returnCopy();
        }
    }

    /**
     * Returns a read-only view of the book catalog.
     * Use addBook() / removeBook() to modify it.
     */
    public List<Book> getBooks() {
        return Collections.unmodifiableList(books);
    }

    /**
     * Returns a read-only view of the member list.
     * Use registerMember() to modify it.
     */
    public List<Member> getMembers() {
        return Collections.unmodifiableList(members);
    }

    /**
     * Returns a read-only view of all borrow transactions.
     * Use borrowBook() / returnBook() to modify it.
     */
    public List<BorrowTransaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    // -------------------------------------------------------------------------
    // Load methods — called ONLY by LibraryData during file loading.
    // These bypass addBook/registerMember validation intentionally, because the
    // data being loaded was already validated when it was first saved.
    // -------------------------------------------------------------------------

    public void loadBooks(List<Book> loadedBooks) {
        books.clear();
        books.addAll(loadedBooks);
    }

    public void loadMembers(List<Member> loadedMembers) {
        members.clear();
        members.addAll(loadedMembers);
    }

    public void loadTransactions(List<BorrowTransaction> loadedTransactions) {
        transactions.clear();
        transactions.addAll(loadedTransactions);
    }
}
