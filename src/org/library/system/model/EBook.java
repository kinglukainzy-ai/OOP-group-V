package org.library.system.model;

/**
 * Concrete class representing an digital book (EBook).
 * Demonstrates inheritance and overrides getBorrowDurationDays() to return 21 days (polymorphism).
 */
public class EBook extends Book {
    private final String fileFormat;
    private final double fileSizeMb;
    private final String downloadUrl;

    public EBook(String bookId, String title, String author, String publisher, 
                 String fileFormat, double fileSizeMb, String downloadUrl) {
        super(bookId, title, author, publisher);
        if (fileFormat == null || fileFormat.trim().isEmpty()) {
            throw new IllegalArgumentException("File format cannot be empty.");
        }
        if (fileSizeMb <= 0) {
            throw new IllegalArgumentException("File size must be positive.");
        }
        if (downloadUrl == null || downloadUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Download URL cannot be empty.");
        }
        this.fileFormat = fileFormat.trim();
        this.fileSizeMb = fileSizeMb;
        this.downloadUrl = downloadUrl.trim();
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public double getFileSizeMb() {
        return fileSizeMb;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    @Override
    public int getBorrowDurationDays() {
        return 21; // Digital eBooks are loaned for 3 weeks
    }
}
