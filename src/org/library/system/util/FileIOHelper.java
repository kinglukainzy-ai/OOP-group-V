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
 * Utility class to handle simple CSV file reading and writing.
 * Designed to be clean and simple using standard Java library features.
 */
public class FileIOHelper {

    // Ensures parent directories exist for a file path
    private static void ensureDirectoryExists(String filepath) {
        File file = new File(filepath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
    }

    /**
     * Reads book data from a CSV file.
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
                String[] parts = line.split(",", -1);
                if (parts.length < 8) continue; // Invalid row

                String type = parts[0].trim();
                String bookId = parts[1].trim();
                String title = parts[2].trim();
                String author = parts[3].trim();
                String publisher = parts[4].trim();

                if ("PHYSICAL".equalsIgnoreCase(type)) {
                    String shelfLocation = parts[5].trim();
                    int totalCopies = Integer.parseInt(parts[6].trim());
                    int availableCopies = Integer.parseInt(parts[7].trim());
                    books.add(new PhysicalBook(bookId, title, author, publisher, shelfLocation, totalCopies, availableCopies));
                } else if ("EBOOK".equalsIgnoreCase(type)) {
                    String fileFormat = parts[5].trim();
                    double fileSizeMb = Double.parseDouble(parts[6].trim());
                    String downloadUrl = parts[7].trim();
                    books.add(new EBook(bookId, title, author, publisher, fileFormat, fileSizeMb, downloadUrl));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading books file: " + e.getMessage());
        }
        return books;
    }

    /**
     * Writes book data to a CSV file.
     */
    public static void writeBooks(String filepath, List<Book> books) {
        ensureDirectoryExists(filepath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            for (Book book : books) {
                if (book instanceof PhysicalBook) {
                    PhysicalBook pBook = (PhysicalBook) book;
                    writer.write(String.format("PHYSICAL,%s,%s,%s,%s,%s,%d,%d\n",
                            pBook.getBookId(), pBook.getTitle(), pBook.getAuthor(), pBook.getPublisher(),
                            pBook.getShelfLocation(), pBook.getTotalCopies(), pBook.getAvailableCopies()));
                } else if (book instanceof EBook) {
                    EBook eBook = (EBook) book;
                    writer.write(String.format("EBOOK,%s,%s,%s,%s,%s,%.2f,%s\n",
                            eBook.getBookId(), eBook.getTitle(), eBook.getAuthor(), eBook.getPublisher(),
                            eBook.getFileFormat(), eBook.getFileSizeMb(), eBook.getDownloadUrl()));
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing books file: " + e.getMessage());
        }
    }

    /**
     * Reads member data from a CSV file.
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
                String[] parts = line.split(",", -1);
                if (parts.length < 4) continue;

                String memberId = parts[0].trim();
                String name = parts[1].trim();
                String email = parts[2].trim();
                boolean isSuspended = Boolean.parseBoolean(parts[3].trim());

                members.add(new Member(memberId, name, email, isSuspended));
            }
        } catch (IOException e) {
            System.err.println("Error reading members file: " + e.getMessage());
        }
        return members;
    }

    /**
     * Writes member data to a CSV file.
     */
    public static void writeMembers(String filepath, List<Member> members) {
        ensureDirectoryExists(filepath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            for (Member m : members) {
                writer.write(String.format("%s,%s,%s,%b\n",
                        m.getMemberId(), m.getName(), m.getEmail(), m.isSuspended()));
            }
        } catch (IOException e) {
            System.err.println("Error writing members file: " + e.getMessage());
        }
    }

    /**
     * Reads borrow transactions from a CSV file, resolving relationships with loaded Books and Members.
     */
    public static List<BorrowTransaction> readTransactions(String filepath, List<Book> books, List<Member> members) {
        List<BorrowTransaction> transactions = new ArrayList<>();
        File file = new File(filepath);
        if (!file.exists()) {
            return transactions;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length < 6) continue;

                String txId = parts[0].trim();
                String bookId = parts[1].trim();
                String memberId = parts[2].trim();
                LocalDate borrowDate = LocalDate.parse(parts[3].trim());
                LocalDate dueDate = LocalDate.parse(parts[4].trim());
                
                String returnStr = parts[5].trim();
                LocalDate returnDate = (returnStr.isEmpty() || "null".equalsIgnoreCase(returnStr)) 
                        ? null : LocalDate.parse(returnStr);

                // Resolve object dependencies from reference lists
                Book matchedBook = null;
                for (Book b : books) {
                    if (b.getBookId().equals(bookId)) {
                        matchedBook = b;
                        break;
                    }
                }

                Member matchedMember = null;
                for (Member m : members) {
                    if (m.getMemberId().equals(memberId)) {
                        matchedMember = m;
                        break;
                    }
                }

                if (matchedBook != null && matchedMember != null) {
                    transactions.add(new BorrowTransaction(txId, matchedBook, matchedMember, borrowDate, dueDate, returnDate));
                }
            }
        } catch (IOException | java.time.format.DateTimeParseException e) {
            System.err.println("Error reading transactions file: " + e.getMessage());
        }
        return transactions;
    }

    /**
     * Writes borrow transactions to a CSV file.
     */
    public static void writeTransactions(String filepath, List<BorrowTransaction> transactions) {
        ensureDirectoryExists(filepath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            for (BorrowTransaction tx : transactions) {
                String returnStr = (tx.getReturnDate() == null) ? "null" : tx.getReturnDate().toString();
                writer.write(String.format("%s,%s,%s,%s,%s,%s\n",
                        tx.getTransactionId(),
                        tx.getBook().getBookId(),
                        tx.getMember().getMemberId(),
                        tx.getBorrowDate().toString(),
                        tx.getDueDate().toString(),
                        returnStr));
            }
        } catch (IOException e) {
            System.err.println("Error writing transactions file: " + e.getMessage());
        }
    }
}
