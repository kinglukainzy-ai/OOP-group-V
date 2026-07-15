package org.library.system.model;

/**
 * Abstract parent class representing a library book.
 * Demonstrates encapsulation (private fields with public getters)
 * and inheritance (base for PhysicalBook and EBook).
 */
public abstract class Book {
    private final String bookId;
    private final String title;
    private final String author;
    private final String publisher;

    public Book(String bookId, String title, String author, String publisher) {
        if (bookId == null || bookId.trim().isEmpty()) {
            throw new IllegalArgumentException("Book ID cannot be empty.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty.");
        }
        this.bookId = bookId.trim();
        this.title = title.trim();
        this.author = (author != null) ? author.trim() : "Unknown";
        this.publisher = (publisher != null) ? publisher.trim() : "Unknown";
    }

    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    /**
     * Polymorphic method to be implemented by subclasses.
     * Returns the maximum duration a book can be borrowed.
     */
    public abstract int getBorrowDurationDays();

    @Override
    public String toString() {
        return String.format("[%s] %s by %s", bookId, title, author);
    }
}
