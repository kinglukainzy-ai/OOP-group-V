package org.library.system.service;

import org.library.system.model.Book;
import org.library.system.model.PhysicalBook;
import org.library.system.model.Library;
import org.library.system.util.LibraryData;

import java.util.List;

public class BookServiceImpl implements BookService {
    
    private Library library;
    
    public BookServiceImpl(LibraryData libraryData) {
        this.library = libraryData.getLibrary();
    }
    
    @Override
    public boolean addBook(Book book) {
        if (book == null) return false;
        try {
            library.addBook(book);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    @Override
    public boolean removeBook(String bookId) {
        if (bookId == null || bookId.trim().isEmpty()) return false;
        try {
            library.removeBook(bookId);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    @Override
    public List<Book> searchBooks(String query) {
        if (query == null || query.trim().isEmpty()) return List.of();
        return library.searchBook(query);
    }
    
    @Override
    public Book getBookById(String bookId) {
        if (bookId == null || bookId.trim().isEmpty()) return null;
        for (Book book : library.getBooks()) {
            if (book.getBookId().equals(bookId)) return book;
        }
        return null;
    }
}
