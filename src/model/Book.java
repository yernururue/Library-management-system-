package model;

import exception.InvalidInputException;

public abstract class Book {
    private int id;
    private String title;
    private int year;
    private Author author;
    private String isbn;
    protected String bookType;

    public Book(int id, String title, Author author, int year, String isbn) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.author = author;
        this.isbn = isbn;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public Author getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getBookType() {
        return bookType;
    }

    public void setId(int id) {
        if (id > 0) {
            this.id = id;
        }
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        this.title = title;
    }

    public void setYear(int year) {
        if (year < 0 || year > 2026) {
            throw new IllegalArgumentException("Invalid year");
        }
        this.year = year;
    }

    public void setAuthor(Author author) {
        if (author == null) {
            throw new IllegalArgumentException("Author cannot be null");
        }
        this.author = author;
    }

    public void setBookType(String bookType) {
        if (bookType == null) {
            throw new IllegalArgumentException("Book type cannot be null");
        }
        this.bookType = bookType;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public abstract double calculateLateFee(int days);

    public abstract String getAccessInstructions();

    public void displayInfo() {
        System.out.println("ID: " + id +
                "\nName: " + title +
                "\nAuthor: " + author.getName() +
                "\nYear: " + year +
                "\nUniqueness: " + isbn);
    }
}
