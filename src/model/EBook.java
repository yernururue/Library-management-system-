package model;

public class EBook extends Book implements DigitalAccess, Borrowable, Validatable<EBook> {
    private boolean available = true;
    private double fileSize;
    private String downloadUrl;
    private double lateFee = 0.25;

    public EBook(int id, String title, Author author, int year, String isbn, double fileSize, String downloadUrl) {
        super(id, title, author, year, isbn);
        this.downloadUrl = downloadUrl;
        this.fileSize = fileSize;
        this.bookType = "EBOOK";
    }

    @Override
    public double calculateLateFee(int days) {
        if (days <= 0)
            return 0;
        return days * lateFee;
    }

    @Override
    public String getAccessInstructions() {
        return "Visit the website: " + downloadUrl;
    }

    @Override
    public String getDownloadURL() {
        return downloadUrl;
    }

    @Override
    public double getFileSize() {
        return fileSize;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public void borrow() {
        if (!available) {
            System.out.println("Book is already borrowed");
            return;
        }
        available = false;
    }

    @Override
    public void returnItem() {
        available = true;
    }

    @Override
    public boolean validate() {
        return Validatable.isNotEmpty(getTitle()) &&
                Validatable.isNotNull(getAuthor()) &&
                Validatable.isValidYear(getYear()) &&
                Validatable.isNotEmpty(downloadUrl) &&
                fileSize >= 0;
    }

    @Override
    public String getValidationError() {
        if (!Validatable.isNotEmpty(getTitle()))
            return "Title cannot be empty";
        if (!Validatable.isNotNull(getAuthor()))
            return "Author cannot be null";
        if (!Validatable.isValidYear(getYear()))
            return "Invalid year";
        if (!Validatable.isNotEmpty(downloadUrl))
            return "Download URL cannot be empty";
        if (fileSize < 0)
            return "File size cannot be negative";
        return "";
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public double getLateFee() {
        return lateFee;
    }

    public void setLateFee(double lateFee) {
        if (lateFee < 0) {
            throw new IllegalArgumentException("Late fee cannot be negative");
        }
        this.lateFee = lateFee;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        if (downloadUrl == null || downloadUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Download URL cannot be empty");
        }
        this.downloadUrl = downloadUrl;
    }

    public double getFilesize() {
        return fileSize;
    }

    public void setFilesize(double fileSize) {
        if (fileSize < 0) {
            throw new IllegalArgumentException("File size cannot be negative");
        }
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        return String.format("EBook[id=%d, title=%s, author=%s, year=%d, url=%s]",
                getId(), getTitle(), getAuthor().getName(), getYear(), downloadUrl);
    }
}
