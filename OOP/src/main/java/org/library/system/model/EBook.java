package org.library.system.model;

public class EBook extends Book {
    private String fileFormat;
    private double fileSizeMb;
    private String downloadUrl;

    public EBook(String bookId, String title, String author, String publisher, 
                 String fileFormat, double fileSizeMb, String downloadUrl) {
        super(bookId, title, author, publisher);
        this.fileFormat = fileFormat;
        this.fileSizeMb = fileSizeMb;
        this.downloadUrl = downloadUrl;
    }

    @Override
    public int getBorrowDurationDays() {
        return 21;
    }

    public String getFileFormat() { return fileFormat; }
    public double getFileSizeMb() { return fileSizeMb; }
    public String getDownloadUrl() { return downloadUrl; }

    public void setFileFormat(String fileFormat) { this.fileFormat = fileFormat; }
    public void setFileSizeMb(double fileSizeMb) { this.fileSizeMb = fileSizeMb; }
    public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
}