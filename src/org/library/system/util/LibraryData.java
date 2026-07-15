package org.library.system.util;

import java.io.File;
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
    // Resolve paths relative to the directory the program is launched from,
    // not relative to wherever the JVM happens to be. This avoids "file not
    // found" surprises when launching from an IDE vs. the command line.
    private static final String DATA_DIR       = System.getProperty("user.dir") + File.separator + "data";
    private static final String BOOKS_FILE       = DATA_DIR + File.separator + "books.csv";
    private static final String MEMBERS_FILE     = DATA_DIR + File.separator + "members.csv";
    private static final String TRANSACTIONS_FILE = DATA_DIR + File.separator + "transactions.csv";

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

        // Use load*() methods — NOT getBooks().clear()/addAll() — because
        // the public getters now return unmodifiable views.
        List<Book> loadedBooks = FileIOHelper.readBooks(BOOKS_FILE);
        libraryInstance.loadBooks(loadedBooks);
        System.out.println("Loaded " + loadedBooks.size() + " books.");

        List<Member> loadedMembers = FileIOHelper.readMembers(MEMBERS_FILE);
        libraryInstance.loadMembers(loadedMembers);
        System.out.println("Loaded " + loadedMembers.size() + " members.");

        List<BorrowTransaction> loadedTx = FileIOHelper.readTransactions(TRANSACTIONS_FILE, loadedBooks, loadedMembers);
        libraryInstance.loadTransactions(loadedTx);
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
