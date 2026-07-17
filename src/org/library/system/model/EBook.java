package org.library.system.model;

public class EBook extends Book {
    private String fileFormat;
    private double fileSizeMb;
    private String downloadUrl;

    // Constructor with validation
    public EBook(String bookId, String title, String author, String publisher, String fileFormat, double fileSizeMb, String downloadUrl) {
        super(bookId, title, author, publisher);
        
        validateInput(fileFormat, "File format cannot be null or empty.");
        validateInput(downloadUrl, "Download URL cannot be null or empty.");
        if (fileSizeMb <= 0) {
            throw new IllegalArgumentException("File size must be greater than 0 MB.");
        }
        
        this.fileFormat = fileFormat;
        this.fileSizeMb = fileSizeMb;
        this.downloadUrl = downloadUrl;
    }

    // Input Validation Helper Method
    private void validateInput(String value, String errorMessage) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    // Polymorphism: 21-day borrow period for digital books
    @Override
    public int getBorrowDurationDays() {
        return 21;
    }

    // Getters and Setters with Validation
    public String getFileFormat() { return fileFormat; }
    public void setFileFormat(String fileFormat) { 
        validateInput(fileFormat, "File format cannot be null or empty.");
        this.fileFormat = fileFormat; 
    }

    public double getFileSizeMb() { return fileSizeMb; }
    public void setFileSizeMb(double fileSizeMb) { 
        if (fileSizeMb <= 0) {
            throw new IllegalArgumentException("File size must be greater than 0 MB.");
        }
        this.fileSizeMb = fileSizeMb; 
    }

    public String getDownloadUrl() { return downloadUrl; }
    public void setDownloadUrl(String downloadUrl) { 
        validateInput(downloadUrl, "Download URL cannot be null or empty.");
        this.downloadUrl = downloadUrl; 
    }
}