package model;

public class PrintedBook extends Book implements Borrowable, Validatable<PrintedBook> {
    private boolean available = true;
    private String shelfLocation;
    private double weight;
    private double lateFee = 0.5;

    public PrintedBook(int id, String title, Author author, int year, String isbn, String shelfLocation,
            double weight) {
        super(id, title, author, year, isbn);
        this.shelfLocation = shelfLocation;
        this.weight = weight;
        this.bookType = "PRINTED";
    }

    @Override
    public double calculateLateFee(int days) {
        if (days <= 0)
            return 0;
        return days * lateFee;
    }

    @Override
    public String getAccessInstructions() {
        return "Located at: " + shelfLocation;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public void borrow() {
        if (!available) {
            System.out.println("Book is already borrowed.");
            return;
        }
        available = false;
    }

    @Override
    public void returnItem() {
        if (!available) {
            available = true;
        }
    }

    @Override
    public boolean validate() {
        return Validatable.isNotEmpty(getTitle()) &&
                Validatable.isNotNull(getAuthor()) &&
                Validatable.isValidYear(getYear()) &&
                Validatable.isNotEmpty(shelfLocation) &&
                weight >= 0;
    }

    @Override
    public String getValidationError() {
        if (!Validatable.isNotEmpty(getTitle()))
            return "Title cannot be empty";
        if (!Validatable.isNotNull(getAuthor()))
            return "Author cannot be null";
        if (!Validatable.isValidYear(getYear()))
            return "Invalid year";
        if (!Validatable.isNotEmpty(shelfLocation))
            return "Shelf location cannot be empty";
        if (weight < 0)
            return "Weight cannot be negative";
        return "";
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getShelfLocation() {
        return shelfLocation;
    }

    public void setShelfLocation(String shelfLocation) {
        if (shelfLocation == null || shelfLocation.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be empty.");
        }
        this.shelfLocation = shelfLocation;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        if (weight < 0) {
            throw new IllegalArgumentException("Weight cannot be negative.");
        }
        this.weight = weight;
    }

    public double getLateFee() {
        return lateFee;
    }

    public void setLateFee(double lateFee) {
        if (lateFee < 0) {
            throw new IllegalArgumentException("Late fee cannot be negative.");
        }
        this.lateFee = lateFee;
    }

    @Override
    public String toString() {
        return String.format("PrintedBook[id=%d, title=%s, author=%s, year=%d, location=%s]",
                getId(), getTitle(), getAuthor().getName(), getYear(), shelfLocation);
    }
}
