package org.library.system.util;

import java.util.List;
import org.library.system.model.Library;
import org.library.system.model.Book;
import org.library.system.model.Member;
import org.library.system.model.BorrowTransaction;

/**
 * Coordinates local CSV file loading and saving.
 * Holds the single library instance used across service logic classes.
 */
public class LibraryData {
    private static final String BOOKS_FILE = "data/books.csv";
    private static final String MEMBERS_FILE = "data/members.csv";
    private static final String TRANSACTIONS_FILE = "data/transactions.csv";

    private final Library libraryInstance;

    public LibraryData() {
        this.libraryInstance = new Library();
    }

    public Library getLibrary() {
        return libraryInstance;
    }

    /**
     * Loads catalog and transactions from local CSV files into memory.
     */
    public void loadDataFromFiles() {
        System.out.println("Loading library data from files...");
        
        // 1. Load books
        List<Book> loadedBooks = FileIOHelper.readBooks(BOOKS_FILE);
        libraryInstance.getBooks().clear();
        libraryInstance.getBooks().addAll(loadedBooks);
        System.out.println("Loaded " + loadedBooks.size() + " books.");

        // 2. Load members
        List<Member> loadedMembers = FileIOHelper.readMembers(MEMBERS_FILE);
        libraryInstance.getMembers().clear();
        libraryInstance.getMembers().addAll(loadedMembers);
        System.out.println("Loaded " + loadedMembers.size() + " members.");

        // 3. Load transactions
        List<BorrowTransaction> loadedTx = FileIOHelper.readTransactions(TRANSACTIONS_FILE, loadedBooks, loadedMembers);
        libraryInstance.getTransactions().clear();
        libraryInstance.getTransactions().addAll(loadedTx);
        System.out.println("Loaded " + loadedTx.size() + " borrow transactions.");
    }

    /**
     * Saves the current memory catalog and transaction records back to local CSV files.
     */
    public void saveDataToFiles() {
        System.out.println("Saving library data to files...");
        
        FileIOHelper.writeBooks(BOOKS_FILE, libraryInstance.getBooks());
        FileIOHelper.writeMembers(MEMBERS_FILE, libraryInstance.getMembers());
        FileIOHelper.writeTransactions(TRANSACTIONS_FILE, libraryInstance.getTransactions());
        
        System.out.println("Library data saved successfully.");
    }
}
