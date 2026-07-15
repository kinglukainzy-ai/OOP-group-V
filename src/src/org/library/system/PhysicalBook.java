package org.library.system;

public class PhysicalBook extends Book {
    private final int pageCount;
    private final double weightKg;

    public PhysicalBook(String title, String author, String isbn, int pageCount, double weightKg) {
        super(title, author, isbn);
        if (pageCount <= 0) {
            throw new IllegalArgumentException("Page count must be positive");
        }
        if (weightKg <= 0) {
            throw new IllegalArgumentException("Weight must be positive");
        }
        this.pageCount = pageCount;
        this.weightKg = weightKg;
    }

    public int getPageCount() { return pageCount; }
    public double getWeightKg() { return weightKg; }

    @Override
    public String toString() {
        return "PhysicalBook{" + super.toString() + ", pageCount=" + pageCount + ", weightKg=" + weightKg + '}';
    }
}
