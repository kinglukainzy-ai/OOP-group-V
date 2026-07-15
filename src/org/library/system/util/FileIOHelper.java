package org.library.system.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.library.system.model.Book;
import org.library.system.model.PhysicalBook;
import org.library.system.model.EBook;
import org.library.system.model.Member;
import org.library.system.model.BorrowTransaction;

/**
 * Utility class to handle simple file reading and writing.
 *
 * DELIMITER CHOICE: Fields are separated by | (pipe) instead of comma.
 * This avoids column-shifting bugs when titles or author names contain a comma
 * (e.g. "Frankenstein, or The Modern Prometheus"). No external library needed.
 */
public class FileIOHelper {

    // Single place to change the delimiter if needed in future.
    private static final String DELIMITER = "|";
    private static final String DELIMITER_REGEX = "\\|";

    // Ensures parent directories exist before writing a file.
    private static void ensureDirectoryExists(String filepath) {
        File file = new File(filepath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
    }

    // -------------------------------------------------------------------------
    // Books
    // -------------------------------------------------------------------------

    /**
     * Reads book data from a pipe-delimited file.
     * A malformed row is skipped with a warning instead of aborting the whole file.
     *
     * PHYSICAL row format: PHYSICAL|bookId|title|author|publisher|shelfLocation|totalCopies|availableCopies
     * EBOOK row format:    EBOOK|bookId|title|author|publisher|fileFormat|fileSizeMb|downloadUrl
     */
    public static List<Book> readBooks(String filepath) {
        List<Book> books = new ArrayList<>();
        File file = new File(filepath);
        if (!file.exists()) {
            return books;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                // Per-row try/catch: one bad row is skipped, the rest of the file still loads.
                try {
                    String[] parts = line.split(DELIMITER_REGEX, -1);
                    if (parts.length < 8) {
                        System.err.println("Skipping short book row (expected 8+ fields): " + line);
                        continue;
                    }

                    String type      = parts[0].trim();
                    String bookId    = parts[1].trim();
                    String title     = parts[2].trim();
                    String author    = parts[3].trim();
                    String publisher = parts[4].trim();

                    if ("PHYSICAL".equalsIgnoreCase(type)) {
                        String shelfLocation  = parts[5].trim();
                        int totalCopies       = Integer.parseInt(parts[6].trim());
                        int availableCopies   = Integer.parseInt(parts[7].trim());
                        books.add(new PhysicalBook(bookId, title, author, publisher,
                                                   shelfLocation, totalCopies, availableCopies));
                    } else if ("EBOOK".equalsIgnoreCase(type)) {
                        String fileFormat = parts[5].trim();
                        double fileSizeMb = Double.parseDouble(parts[6].trim());
                        String downloadUrl = parts[7].trim();
                        books.add(new EBook(bookId, title, author, publisher,
                                            fileFormat, fileSizeMb, downloadUrl));
                    } else {
                        System.err.println("Skipping unknown book type: " + type);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Skipping malformed book row (bad number): " + line + " — " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading books file: " + e.getMessage());
        }
        return books;
    }

    /**
     * Writes book data to a pipe-delimited file.
     */
    public static void writeBooks(String filepath, List<Book> books) {
        ensureDirectoryExists(filepath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            for (Book book : books) {
                if (book instanceof PhysicalBook) {
                    PhysicalBook p = (PhysicalBook) book;
                    writer.write(String.join(DELIMITER,
                            "PHYSICAL", p.getBookId(), p.getTitle(), p.getAuthor(), p.getPublisher(),
                            p.getShelfLocation(),
                            String.valueOf(p.getTotalCopies()),
                            String.valueOf(p.getAvailableCopies())));
                } else if (book instanceof EBook) {
                    EBook e = (EBook) book;
                    writer.write(String.join(DELIMITER,
                            "EBOOK", e.getBookId(), e.getTitle(), e.getAuthor(), e.getPublisher(),
                            e.getFileFormat(),
                            String.format("%.2f", e.getFileSizeMb()),
                            e.getDownloadUrl()));
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing books file: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Members
    // -------------------------------------------------------------------------

    /**
     * Reads member data from a pipe-delimited file.
     * A malformed row is skipped with a warning instead of aborting the whole file.
     *
     * Row format: memberId|name|email|isSuspended
     */
    public static List<Member> readMembers(String filepath) {
        List<Member> members = new ArrayList<>();
        File file = new File(filepath);
        if (!file.exists()) {
            return members;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                try {
                    String[] parts = line.split(DELIMITER_REGEX, -1);
                    if (parts.length < 4) {
                        System.err.println("Skipping short member row (expected 4+ fields): " + line);
                        continue;
                    }
                    String memberId   = parts[0].trim();
                    String name       = parts[1].trim();
                    String email      = parts[2].trim();
                    boolean suspended = Boolean.parseBoolean(parts[3].trim());
                    members.add(new Member(memberId, name, email, suspended));
                } catch (IllegalArgumentException e) {
                    System.err.println("Skipping malformed member row: " + line + " — " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading members file: " + e.getMessage());
        }
        return members;
    }

    /**
     * Writes member data to a pipe-delimited file.
     */
    public static void writeMembers(String filepath, List<Member> members) {
        ensureDirectoryExists(filepath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            for (Member m : members) {
                writer.write(String.join(DELIMITER,
                        m.getMemberId(), m.getName(), m.getEmail(),
                        String.valueOf(m.isSuspended())));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing members file: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Borrow Transactions
    // -------------------------------------------------------------------------

    /**
     * Reads borrow transactions from a pipe-delimited file.
     * Resolves Book and Member references from the already-loaded lists.
     * A malformed row is skipped with a warning instead of aborting the whole file.
     *
     * Row format: transactionId|bookId|memberId|borrowDate|dueDate|returnDate (or "null")
     */
    public static List<BorrowTransaction> readTransactions(String filepath,
                                                            List<Book> books,
                                                            List<Member> members) {
        List<BorrowTransaction> transactions = new ArrayList<>();
        File file = new File(filepath);
        if (!file.exists()) {
            return transactions;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                try {
                    String[] parts = line.split(DELIMITER_REGEX, -1);
                    if (parts.length < 6) {
                        System.err.println("Skipping short transaction row (expected 6+ fields): " + line);
                        continue;
                    }

                    String txId      = parts[0].trim();
                    String bookId    = parts[1].trim();
                    String memberId  = parts[2].trim();
                    LocalDate borrowDate = LocalDate.parse(parts[3].trim());
                    LocalDate dueDate    = LocalDate.parse(parts[4].trim());
                    String returnStr     = parts[5].trim();
                    LocalDate returnDate = (returnStr.isEmpty() || "null".equalsIgnoreCase(returnStr))
                            ? null : LocalDate.parse(returnStr);

                    // Resolve object references from already-loaded lists
                    Book matchedBook = null;
                    for (Book b : books) {
                        if (b.getBookId().equals(bookId)) { matchedBook = b; break; }
                    }

                    Member matchedMember = null;
                    for (Member m : members) {
                        if (m.getMemberId().equals(memberId)) { matchedMember = m; break; }
                    }

                    if (matchedBook == null) {
                        System.err.println("Skipping transaction " + txId + ": book ID not found: " + bookId);
                        continue;
                    }
                    if (matchedMember == null) {
                        System.err.println("Skipping transaction " + txId + ": member ID not found: " + memberId);
                        continue;
                    }

                    transactions.add(new BorrowTransaction(txId, matchedBook, matchedMember,
                                                            borrowDate, dueDate, returnDate));
                } catch (java.time.format.DateTimeParseException e) {
                    System.err.println("Skipping malformed transaction row (bad date): " + line + " — " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading transactions file: " + e.getMessage());
        }
        return transactions;
    }

    /**
     * Writes borrow transactions to a pipe-delimited file.
     */
    public static void writeTransactions(String filepath, List<BorrowTransaction> transactions) {
        ensureDirectoryExists(filepath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            for (BorrowTransaction tx : transactions) {
                String returnStr = (tx.getReturnDate() == null) ? "null" : tx.getReturnDate().toString();
                writer.write(String.join(DELIMITER,
                        tx.getTransactionId(),
                        tx.getBook().getBookId(),
                        tx.getMember().getMemberId(),
                        tx.getBorrowDate().toString(),
                        tx.getDueDate().toString(),
                        returnStr));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing transactions file: " + e.getMessage());
        }
    }
}
