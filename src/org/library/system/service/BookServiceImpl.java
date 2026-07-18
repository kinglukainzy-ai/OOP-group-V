package org.library.system.service;

import org.library.system.model.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookServiceImpl implements BookService {
    
    private List<Book> books;
    
    public BookServiceImpl() {
        this.books = new ArrayList<>();
    }

    
    @Override
    public boolean addBook(Book book) {
        if (book == null) {
            return false;
        }
        
        // Check if book with same ID already exists
        if (getBookById(book.getIsbn()) != null) {
            return false;
        }
        
        books.add(book);
        System.out.println("Book added: " + book.getTitle());
        return true;
    }
    
    @Override
    public boolean removeBook(String bookId) {
        if (bookId == null || bookId.trim().isEmpty()) {
            return false;
        }
        
        Book bookToRemove = null;
        for (Book book : books) {
            if (book.getIsbn().equals(bookId)) {
                bookToRemove = book;
                break;
            }
        }
        
        if (bookToRemove == null) {
            return false;
        }
        
        // Check if book is available (not borrowed)
        if (!bookToRemove.isAvailable()) {
            System.out.println("Cannot remove a book that is currently borrowed");
            return false;
        }
        
        books.remove(bookToRemove);
        System.out.println("Book removed: " + bookToRemove.getTitle());
        return true;
    }
    
    @Override
    public List<Book> searchBooks(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String searchTerm = query.toLowerCase().trim();
        
        return books.stream()
                .filter(book -> 
                    book.getTitle().toLowerCase().contains(searchTerm) ||
                    book.getAuthor().toLowerCase().contains(searchTerm) ||
                    book.getIsbn().toLowerCase().contains(searchTerm)
                )
                .collect(Collectors.toList());
    }
    
    @Override
    public Book getBookById(String bookId) {
        if (bookId == null || bookId.trim().isEmpty()) {
            return null;
        }
        
        for (Book book : books) {
            if (book.getIsbn().equals(bookId)) {
                return book;
            }
        }
        return null;
    }
    
    // ========== Additional Helper Methods ==========
    
    /**
     * Get all books in the library
     */
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }
    
    /**
     * Get all available books
     */
    public List<Book> getAvailableBooks() {
        return books.stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
    }
    
    /**
     * Get total book count
     */
    public int getTotalBookCount() {
        return books.size();
    }
    
    /**
     * Display all books
     */
    public void displayAllBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in the library");
            return;
        }
        
        System.out.println("\n=== LIBRARY BOOKS (" + books.size() + " total) ===");
        for (Book book : books) {
            String status = book.isAvailable() ? "Available" : "Borrowed";
            System.out.println("• " + book.getTitle() + " by " + book.getAuthor() + 
                             " [" + book.getIsbn() + "] - " + status);
        }
        System.out.println();
   }
}