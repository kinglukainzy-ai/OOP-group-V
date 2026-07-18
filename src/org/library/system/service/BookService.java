package org.library.system.service;

import java.util.List;
import org.library.system.model.Book;

public interface BookService {
    /**
     * Adds a new book to the library catalog.
     * @param book The Book to add (PhysicalBook or EBook).
     * @return true if added successfully, false if ID already exists.
     */
    boolean addBook(Book book);

    /**
     * Deletes a book from the library by its ID.
     */
    boolean removeBook(String bookId);

    /**
     * Searches books containing the search text in the title or author.
     */
    List<Book> searchBooks(String query);

    /**
     * Retrieves a book by its unique ID.
     */
    Book getBookById(String bookId);
}