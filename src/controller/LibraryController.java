package controller;

import model.*;
import service.interfaces.BookServiceInterface;
import service.interfaces.AuthorServiceInterface;
import exception.*;
import utils.ReflectionUtils;
import utils.SortingUtils;

import java.util.List;
import java.util.Scanner;

public class LibraryController {

    private final BookServiceInterface bookService;
    private final AuthorServiceInterface authorService;
    private final Scanner scanner;

    public LibraryController(BookServiceInterface bookService, AuthorServiceInterface authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("       LIBRARY MANAGEMENT SYSTEM");
        System.out.println("=".repeat(50));
        System.out.println("1.  List all books");
        System.out.println("2.  List all books (sorted by title)");
        System.out.println("3.  List all books (sorted by year)");
        System.out.println("4.  Search books by title");
        System.out.println("5.  Get book by ID");
        System.out.println("6.  Add new EBook");
        System.out.println("7.  Add new Printed Book");
        System.out.println("8.  Delete book");
        System.out.println("9.  List all authors");
        System.out.println("10. Add new author");
        System.out.println("11. Delete author");
        System.out.println("12. Show reflection demo");
        System.out.println("13. Show interface features demo");
        System.out.println("0.  Exit");
        System.out.println("=".repeat(50));
        System.out.print("Enter choice: ");
    }

    public void run() {
        boolean running = true;

        while (running) {
            displayMenu();

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) {
                    case 0:
                        running = false;
                        System.out.println("Goodbye!");
                        break;
                    case 1:
                        listAllBooks();
                        break;
                    case 2:
                        listBooksSortedByTitle();
                        break;
                    case 3:
                        listBooksSortedByYear();
                        break;
                    case 4:
                        searchBooks();
                        break;
                    case 5:
                        getBookById();
                        break;
                    case 6:
                        addEBook();
                        break;
                    case 7:
                        addPrintedBook();
                        break;
                    case 8:
                        deleteBook();
                        break;
                    case 9:
                        listAllAuthors();
                        break;
                    case 10:
                        addAuthor();
                        break;
                    case 11:
                        deleteAuthor();
                        break;
                    case 12:
                        showReflectionDemo();
                        break;
                    case 13:
                        showInterfaceDemo();
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private void listAllBooks() {
        System.out.println("\n--- All Books ---");
        List<Book> books = bookService.getAllBooks();
        printBooks(books);
    }

    private void listBooksSortedByTitle() {
        System.out.println("\n--- Books Sorted by Title (Lambda Demo) ---");
        List<Book> books = bookService.getBooksSortedByTitle();
        printBooks(books);
    }

    private void listBooksSortedByYear() {
        System.out.println("\n--- Books Sorted by Year (Lambda Demo) ---");
        List<Book> books = bookService.getBooksSortedByYear();
        printBooks(books);
    }

    private void searchBooks() {
        System.out.print("Enter search keyword: ");
        String keyword = scanner.nextLine().trim();

        System.out.println("\n--- Search Results (Lambda Filtering Demo) ---");
        List<Book> books = bookService.searchByTitle(keyword);
        if (books.isEmpty()) {
            System.out.println("No books found matching: " + keyword);
        } else {
            printBooks(books);
        }
    }

    private void getBookById() {
        System.out.print("Enter book ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Book book = bookService.getBookById(id);
            System.out.println("\n--- Book Details ---");
            book.displayInfo();
            System.out.println("Type: " + book.getClass().getSimpleName());
            System.out.println("Access: " + book.getAccessInstructions());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        } catch (ResourceNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void addEBook() {
        try {
            System.out.println("\n--- Add New EBook ---");

            List<Author> authors = authorService.getAllAuthors();
            if (authors.isEmpty()) {
                System.out.println("No authors available. Please add an author first.");
                return;
            }

            System.out.println("Available authors:");
            for (Author a : authors) {
                System.out.println("  ID: " + a.getId() + " - " + a.getName());
            }

            System.out.print("Enter title: ");
            String title = scanner.nextLine().trim();

            System.out.print("Enter ISBN: ");
            String isbn = scanner.nextLine().trim();

            System.out.print("Enter year: ");
            int year = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter author ID: ");
            int authorId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter download URL: ");
            String url = scanner.nextLine().trim();

            System.out.print("Enter file size (MB): ");
            double fileSize = Double.parseDouble(scanner.nextLine().trim());

            Author author = authorService.getAuthorById(authorId);
            EBook ebook = new EBook(0, title, author, year, isbn, fileSize, url);

            bookService.createBook(ebook);
            System.out.println("EBook created successfully!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void addPrintedBook() {
        try {
            System.out.println("\n--- Add New Printed Book ---");

            List<Author> authors = authorService.getAllAuthors();
            if (authors.isEmpty()) {
                System.out.println("No authors available. Please add an author first.");
                return;
            }

            System.out.println("Available authors:");
            for (Author a : authors) {
                System.out.println("  ID: " + a.getId() + " - " + a.getName());
            }

            System.out.print("Enter title: ");
            String title = scanner.nextLine().trim();

            System.out.print("Enter ISBN: ");
            String isbn = scanner.nextLine().trim();

            System.out.print("Enter year: ");
            int year = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter author ID: ");
            int authorId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter shelf location: ");
            String location = scanner.nextLine().trim();

            System.out.print("Enter weight (kg): ");
            double weight = Double.parseDouble(scanner.nextLine().trim());

            Author author = authorService.getAuthorById(authorId);
            PrintedBook pbook = new PrintedBook(0, title, author, year, isbn, location, weight);

            bookService.createBook(pbook);
            System.out.println("Printed book created successfully!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteBook() {
        System.out.print("Enter book ID to delete: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            bookService.deleteBook(id);
            System.out.println("Book deleted successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        } catch (ResourceNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listAllAuthors() {
        System.out.println("\n--- All Authors ---");
        List<Author> authors = authorService.getAllAuthors();
        if (authors.isEmpty()) {
            System.out.println("No authors found.");
        } else {
            for (Author a : authors) {
                System.out.printf("ID: %d | %s (%d) - %s%n",
                        a.getId(), a.getName(), a.getBirthYear(), a.getNationality());
            }
        }
    }

    private void addAuthor() {
        try {
            System.out.println("\n--- Add New Author ---");

            System.out.print("Enter name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter birth year: ");
            int birthYear = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter nationality: ");
            String nationality = scanner.nextLine().trim();

            Author author = new Author(0, name, birthYear, nationality);
            authorService.createAuthor(author);
            System.out.println("Author created successfully!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteAuthor() {
        System.out.print("Enter author ID to delete: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            authorService.deleteAuthor(id);
            System.out.println("Author deleted successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        } catch (ResourceNotFoundException | InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showReflectionDemo() {
        System.out.println("\n--- REFLECTION/RTTI DEMONSTRATION ---");

        ReflectionUtils.analyzeClass(Book.class);
        ReflectionUtils.analyzeClass(EBook.class);

        ReflectionUtils.compareClasses(EBook.class, PrintedBook.class);

        List<Book> books = bookService.getAllBooks();
        if (!books.isEmpty()) {
            System.out.println("\n--- Inspecting Book Instance ---");
            ReflectionUtils.inspectObject(books.get(0));
        }
    }

    private void showInterfaceDemo() {
        System.out.println("\n--- INTERFACE FEATURES DEMONSTRATION ---");

        System.out.println("\n[Validatable Static Methods]");
        System.out.println("isNotNull(\"test\"): " + Validatable.isNotNull("test"));
        System.out.println("isNotNull(null): " + Validatable.isNotNull(null));
        System.out.println("isNotEmpty(\"hello\"): " + Validatable.isNotEmpty("hello"));
        System.out.println("isNotEmpty(\"\"): " + Validatable.isNotEmpty(""));
        System.out.println("isValidYear(2020): " + Validatable.isValidYear(2020));
        System.out.println("isValidYear(3000): " + Validatable.isValidYear(3000));

        System.out.println("\n[Borrowable Static Methods]");
        System.out.println("isValidBorrowingPeriod(14): " + Borrowable.isValidBorrowingPeriod(14));
        System.out.println("isValidBorrowingPeriod(45): " + Borrowable.isValidBorrowingPeriod(45));
        System.out.println("getMaxLateFee(0.5): " + Borrowable.getMaxLateFee(0.5));

        List<Book> books = bookService.getAllBooks();
        if (!books.isEmpty()) {
            Book book = books.get(0);

            if (book instanceof Validatable) {
                Validatable<?> v = (Validatable<?>) book;
                System.out.println("\n[Validatable Default Methods on: " + book.getTitle() + "]");
                System.out.println("validate(): " + v.validate());
                System.out.println("getValidationSummary(): " + v.getValidationSummary());
                v.validateAndLog();
            }

            if (book instanceof Borrowable) {
                Borrowable b = (Borrowable) book;
                System.out.println("\n[Borrowable Default Methods]");
                System.out.println("getBorrowingStatus(): " + b.getBorrowingStatus());
                System.out.println("calculateDefaultLateFee(10, 0.5): " + b.calculateDefaultLateFee(10, 0.5));
            }
        }
    }

    private void printBooks(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }

        for (Book b : books) {
            System.out.printf("ID: %d | %s (%d) by %s [%s]%n",
                    b.getId(),
                    b.getTitle(),
                    b.getYear(),
                    b.getAuthor() != null ? b.getAuthor().getName() : "Unknown",
                    b.getClass().getSimpleName());
        }
        System.out.println("Total: " + books.size() + " books");
    }
}
