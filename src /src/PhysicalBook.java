public class PhysicalBook extends Book {
    private double weight;

    public PhysicalBook(String title, String author, String isbn, double weight) {
        super(title, author, isbn);
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return super.toString() + ", Weight: " + weight + "kg";
    }
}
