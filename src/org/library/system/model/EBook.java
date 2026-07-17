package org.library.system.model;

public class EBook extends Book {
    private String fileFormat;
    private double fileSizeMb;
    private String downloadUrl;

    // Constructor
    public EBook(String bookId, String title, String author, String publisher, String fileFormat, double fileSizeMb, String downloadUrl) {
        super(bookId, title, author, publisher);
        this.fileFormat = fileFormat;
        this.fileSizeMb = fileSizeMb;
        this.downloadUrl = downloadUrl;
    }

    // Polymorphism: 21-day borrow period for digital books
    @Override
    public int getBorrowDurationDays() {
        return 21;
    }

    // Getters and Setters
    public String getFileFormat() { return fileFormat; }
    public void setFileFormat(String fileFormat) { this.fileFormat = fileFormat; }

    public double getFileSizeMb() { return fileSizeMb; }
    public void setFileSizeMb(double fileSizeMb) { this.fileSizeMb = fileSizeMb; }

    public String getDownloadUrl() { return downloadUrl; }
    public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
}