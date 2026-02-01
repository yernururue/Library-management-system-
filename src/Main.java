import controller.LibraryController;
import exception.*;
import model.*;
import repository.AuthorRepositoryImpl;
import repository.BookRepositoryImpl;
import repository.interfaces.AuthorRepository;
import repository.interfaces.BookRepository;
import service.AuthorServiceImpl;
import service.BookServiceImpl;
import service.interfaces.AuthorServiceInterface;
import service.interfaces.BookServiceInterface;
import utils.ReflectionUtils;
import utils.SortingUtils;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("  LIBRARY MANAGEMENT SYSTEM - ASSIGNMENT 4: SOLID ARCHITECTURE");
        System.out.println("=".repeat(70));

        System.out.println("\n[SOLID ARCHITECTURE SETUP]");
        System.out.println("Creating layers with Dependency Injection (DIP)...");

        AuthorRepository authorRepository = new AuthorRepositoryImpl();
        BookRepository bookRepository = new BookRepositoryImpl(authorRepository);
        System.out.println("✓ Repository layer initialized (Generic CrudRepository<T, ID>)");

        AuthorServiceInterface authorService = new AuthorServiceImpl(authorRepository);
        BookServiceInterface bookService = new BookServiceImpl(bookRepository, authorRepository);
        System.out.println("✓ Service layer initialized (Using repository interfaces - DIP)");

        LibraryController controller = new LibraryController(bookService, authorService);
        System.out.println("✓ Controller layer initialized (Using service interfaces - DIP)");

        System.out.println("\nArchitecture: Controller → Service → Repository → Database");

        if (args.length > 0 && args[0].equals("--demo")) {
            runDemoMode(authorService, bookService);
        } else {
            System.out.println("\n[STARTING INTERACTIVE MODE]");
            controller.run();
        }
    }

    private static void runDemoMode(AuthorServiceInterface authorService, BookServiceInterface bookService) {
        System.out.println("\n[RUNNING DEMONSTRATION MODE]");

        try {
            demonstratePolymorphism(bookService);
            demonstrateInterfaces(bookService);
            demonstrateGenerics();
            demonstrateLambdas(bookService);
            demonstrateReflection(bookService);
            demonstrateCRUD(authorService, bookService);
            demonstrateExceptions(bookService);
            demonstrateComposition(bookService);

            System.out.println("\n" + "=".repeat(70));
            System.out.println("  DEMONSTRATION COMPLETE - All features shown successfully!");
            System.out.println("=".repeat(70));

        } catch (Exception e) {
            System.err.println("Demo error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void demonstratePolymorphism(BookServiceInterface bookService) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("1. POLYMORPHISM DEMONSTRATION");
        System.out.println("=".repeat(60));

        List<Book> books = bookService.getAllBooks();
        if (books.size() >= 2) {
            Book book1 = books.get(0);
            Book book2 = books.get(1);

            System.out.println("\nUsing Book (base class) references:");
            System.out.println("  Book 1: " + book1.getClass().getSimpleName() + " - " + book1.getTitle());
            System.out.println("  Book 2: " + book2.getClass().getSimpleName() + " - " + book2.getTitle());

            System.out.println("\nPolymorphic method calls (same method, different behavior):");
            System.out.println("  book1.calculateLateFee(10): $" + book1.calculateLateFee(10));
            System.out.println("  book2.calculateLateFee(10): $" + book2.calculateLateFee(10));

            System.out.println("\n  book1.getAccessInstructions(): " + book1.getAccessInstructions());
            System.out.println("  book2.getAccessInstructions(): " + book2.getAccessInstructions());
        }
    }

    private static void demonstrateInterfaces(BookServiceInterface bookService) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("2. INTERFACE FEATURES (Default & Static Methods)");
        System.out.println("=".repeat(60));

        System.out.println("\n[Validatable<T> Interface - Static Methods]");
        System.out.println("  Validatable.isNotNull(\"test\"): " + Validatable.isNotNull("test"));
        System.out.println("  Validatable.isNotNull(null): " + Validatable.isNotNull(null));
        System.out.println("  Validatable.isNotEmpty(\"hello\"): " + Validatable.isNotEmpty("hello"));
        System.out.println("  Validatable.isValidYear(2020): " + Validatable.isValidYear(2020));

        System.out.println("\n[Borrowable Interface - Static Methods]");
        System.out.println("  Borrowable.isValidBorrowingPeriod(14): " + Borrowable.isValidBorrowingPeriod(14));
        System.out.println("  Borrowable.getMaxLateFee(0.5): $" + Borrowable.getMaxLateFee(0.5));

        List<Book> books = bookService.getAllBooks();
        if (!books.isEmpty()) {
            Book book = books.get(0);

            if (book instanceof Validatable) {
                Validatable<?> v = (Validatable<?>) book;
                System.out.println("\n[Validatable<T> Interface - Default Methods on " + book.getTitle() + "]");
                System.out.println("  getValidationSummary(): " + v.getValidationSummary());
                System.out.print("  validateAndLog(): ");
                v.validateAndLog();
            }

            if (book instanceof Borrowable) {
                Borrowable b = (Borrowable) book;
                System.out.println("\n[Borrowable Interface - Default Methods]");
                System.out.println("  getBorrowingStatus(): " + b.getBorrowingStatus());
                System.out.println("  calculateDefaultLateFee(10, 0.5): $" + b.calculateDefaultLateFee(10, 0.5));
            }
        }
    }

    private static void demonstrateGenerics() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("3. GENERICS DEMONSTRATION");
        System.out.println("=".repeat(60));

        System.out.println("\n[Generic Repository Interface]");
        System.out.println("  CrudRepository<T, ID> - Base generic interface");
        System.out.println("  BookRepository extends CrudRepository<Book, Integer>");
        System.out.println("  AuthorRepository extends CrudRepository<Author, Integer>");

        System.out.println("\n[Generic Validatable Interface]");
        System.out.println("  Validatable<T> - Generic validation interface");
        System.out.println("  EBook implements Validatable<EBook>");
        System.out.println("  PrintedBook implements Validatable<PrintedBook>");

        System.out.println("\n[Generic SortingUtils Methods]");
        System.out.println("  <T> List<T> sortBy(List<T> list, Comparator<T> comparator)");
        System.out.println("  <T> List<T> sortByDescending(List<T> list, Comparator<T> comparator)");
    }

    private static void demonstrateLambdas(BookServiceInterface bookService) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("4. LAMBDA EXPRESSIONS DEMONSTRATION");
        System.out.println("=".repeat(60));

        List<Book> books = bookService.getAllBooks();

        System.out.println("\n[Sorting by Title using Lambda]");
        System.out.println("  Lambda: (b1, b2) -> b1.getTitle().compareToIgnoreCase(b2.getTitle())");
        List<Book> sortedByTitle = bookService.getBooksSortedByTitle();
        sortedByTitle.stream().limit(5).forEach(b -> System.out.println("    " + b.getTitle()));

        System.out.println("\n[Sorting by Year using Lambda]");
        System.out.println("  Lambda: (b1, b2) -> Integer.compare(b1.getYear(), b2.getYear())");
        List<Book> sortedByYear = bookService.getBooksSortedByYear();
        sortedByYear.stream().limit(5).forEach(b -> System.out.println("    " + b.getYear() + " - " + b.getTitle()));

        System.out.println("\n[Filtering using Lambda/Stream]");
        System.out.println("  Lambda filter: book -> book.getTitle().toLowerCase().contains(\"harry\")");
        List<Book> filtered = bookService.searchByTitle("harry");
        filtered.forEach(b -> System.out.println("    Found: " + b.getTitle()));

        System.out.println("\n[SortingUtils with Custom Lambda]");
        SortingUtils.sortAndPrint(
                books.subList(0, Math.min(5, books.size())),
                (b1, b2) -> Integer.compare(b2.getYear(), b1.getYear()),
                "Top 5 Books by Year (Descending)");
    }

    private static void demonstrateReflection(BookServiceInterface bookService) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("5. REFLECTION/RTTI DEMONSTRATION");
        System.out.println("=".repeat(60));

        ReflectionUtils.analyzeClass(Book.class);
        ReflectionUtils.analyzeClass(EBook.class);

        ReflectionUtils.compareClasses(EBook.class, PrintedBook.class);

        List<Book> books = bookService.getAllBooks();
        if (!books.isEmpty()) {
            System.out.println("\n[Runtime Object Inspection]");
            ReflectionUtils.inspectObject(books.get(0));
        }
    }

    private static void demonstrateCRUD(AuthorServiceInterface authorService,
            BookServiceInterface bookService) throws Exception {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("6. CRUD OPERATIONS DEMONSTRATION");
        System.out.println("=".repeat(60));

        System.out.println("\n[READ] Getting all books...");
        List<Book> books = bookService.getAllBooks();
        System.out.println("  Found " + books.size() + " books");

        if (!books.isEmpty()) {
            int id = books.get(0).getId();
            System.out.println("\n[READ] Getting book by ID " + id + "...");
            try {
                Book book = bookService.getBookById(id);
                System.out.println("  Found: " + book.getTitle());
            } catch (ResourceNotFoundException e) {
                System.out.println("  Not found: " + e.getMessage());
            }
        }

        System.out.println("\n[CREATE] Creating new book requires:");
        System.out.println("  - Valid title (not empty)");
        System.out.println("  - Valid year (1 - 2026)");
        System.out.println("  - Existing author");
        System.out.println("  - Unique ISBN");

        System.out.println("\n[UPDATE] Update operation available via service.updateBook()");

        System.out.println("\n[DELETE] Delete operation available via service.deleteBook()");
        System.out.println("  (Skipping actual delete to preserve demo data)");
    }

    private static void demonstrateExceptions(BookServiceInterface bookService) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("7. EXCEPTION HANDLING DEMONSTRATION");
        System.out.println("=".repeat(60));

        System.out.println("\n[Test 1] ResourceNotFoundException (Non-existent book)");
        try {
            bookService.getBookById(99999);
        } catch (ResourceNotFoundException e) {
            System.out.println("  ✓ Caught: " + e.getClass().getSimpleName());
            System.out.println("  Message: " + e.getMessage());
        }

        System.out.println("\n[Test 2] InvalidInputException (Empty title)");
        try {
            Author testAuthor = new Author(1, "Test", 1990, "Test");
            EBook invalidBook = new EBook(0, "", testAuthor, 2020, "9991111111111", 2.0, "url");
            bookService.createBook(invalidBook);
        } catch (InvalidInputException e) {
            System.out.println("  ✓ Caught: " + e.getClass().getSimpleName());
            System.out.println("  Message: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("  Caught: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        System.out.println("\n[Exception Hierarchy]");
        System.out.println("  InvalidInputException");
        System.out.println("    └── DuplicateResourceException");
        System.out.println("  ResourceNotFoundException");
        System.out.println("  DatabaseOperationException");
    }

    private static void demonstrateComposition(BookServiceInterface bookService) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("8. COMPOSITION DEMONSTRATION (Book HAS-A Author)");
        System.out.println("=".repeat(60));

        List<Book> books = bookService.getAllBooks();
        if (!books.isEmpty()) {
            Book book = books.get(0);
            Author author = book.getAuthor();

            System.out.println("\n[Composition: Book contains Author object]");
            System.out.println("  Book: " + book.getTitle());
            System.out.println("  Author (via composition): " + author.getName());
            System.out.println("  Author's Birth Year: " + author.getBirthYear());
            System.out.println("  Author's Nationality: " + author.getNationality());

            System.out.println("\n[Relationship]");
            System.out.println("  Book HAS-A Author (composition)");
            System.out.println("  A Book cannot exist without an Author");
            System.out.println("  Author is stored via foreign key in database");
        }
    }
}