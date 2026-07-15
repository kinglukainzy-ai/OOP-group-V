package org.library.system.model;

/**
 * Concrete class representing a physical book.
 * Demonstrates inheritance and overrides getBorrowDurationDays() to return 14 days (polymorphism).
 */
public class PhysicalBook extends Book {
    private final String shelfLocation;
    private final int totalCopies;
    private int availableCopies;

    /**
     * Creates a PhysicalBook where all copies start as available.
     * Delegates to the full constructor.
     */
    public PhysicalBook(String bookId, String title, String author, String publisher, 
                        String shelfLocation, int totalCopies) {
        this(bookId, title, author, publisher, shelfLocation, totalCopies, totalCopies);
    }

    /**
     * Creates a PhysicalBook with a specific available-copy count.
     * Used when loading saved data from file, where copies may already be borrowed.
     */
    public PhysicalBook(String bookId, String title, String author, String publisher, 
                        String shelfLocation, int totalCopies, int availableCopies) {
        super(bookId, title, author, publisher);
        if (shelfLocation == null || shelfLocation.trim().isEmpty()) {
            throw new IllegalArgumentException("Shelf location cannot be empty.");
        }
        if (totalCopies <= 0) {
            throw new IllegalArgumentException("Total copies must be greater than zero.");
        }
        if (availableCopies < 0 || availableCopies > totalCopies) {
            throw new IllegalArgumentException("Invalid available copies count.");
        }
        this.shelfLocation = shelfLocation.trim();
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
    }

    public String getShelfLocation() {
        return shelfLocation;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public boolean borrowCopy() {
        if (availableCopies > 0) {
            availableCopies--;
            return true;
        }
        return false;
    }

    public void returnCopy() {
        if (availableCopies < totalCopies) {
            availableCopies++;
        }
    }

    @Override
    public int getBorrowDurationDays() {
        return 14; // Standard 2-week borrow for physical items
    }
}
