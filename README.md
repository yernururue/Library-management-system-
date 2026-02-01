I lost all commit, tried to fix with gemini but couldn't, there were should be about 16-17 commits
# Library Management System - SOLID Architecture & Advanced OOP

## Project Overview

This project is a **Library Management System** implemented in Java demonstrating **SOLID principles**, **advanced OOP features**, and a **layered architecture**. It was refactored from Assignment 3 to apply best practices in object-oriented design.

**Architecture:** `Controller → Service → Repository → Database`

**Technology Stack:**
- Java 17+
- PostgreSQL
- JDBC

---

## A. SOLID Documentation

### Single Responsibility Principle (SRP)

| Class | Responsibility |
|-------|---------------|
| `Book.java` | Entity definition and validation |
| `BookRepositoryImpl` | Data access operations only |
| `BookServiceImpl` | Business logic and validation |
| `LibraryController` | User input handling, no logic |
| `ReflectionUtils` | Runtime inspection utilities |
| `SortingUtils` | Sorting utilities with lambdas |

### Open-Closed Principle (OCP)

- **`Book`** abstract class can be extended without modification
- New book types (e.g., `AudioBook`) can be added by:
  1. Creating new class extending `Book`
  2. Implementing abstract methods
  3. No changes to existing code required

```java
// Adding new book type - extends without modifying Book.java
public class AudioBook extends Book implements Borrowable {
    @Override
    public double calculateLateFee(int days) { /* implementation */ }
    @Override
    public String getAccessInstructions() { /* implementation */ }
}
```

### Liskov Substitution Principle (LSP)

All `Book` subclasses are substitutable:

```java
// Works with any Book subclass - polymorphism
Book book1 = new EBook(...);
Book book2 = new PrintedBook(...);
book1.calculateLateFee(10);  // Works correctly
book2.calculateLateFee(10);  // Works correctly
```

### Interface Segregation Principle (ISP)

Interfaces are narrow and focused:

| Interface | Purpose |
|-----------|---------|
| `Borrowable` | Borrowing operations only |
| `DigitalAccess` | Digital content access only |
| `Validatable<T>` | Validation operations only |
| `CrudRepository<T,ID>` | CRUD operations only |

### Dependency Inversion Principle (DIP)

High-level modules depend on abstractions:

```java
// Service depends on repository INTERFACE, not implementation
public class BookServiceImpl implements BookServiceInterface {
    private final BookRepository bookRepository;  // Interface!
    
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;  // Constructor injection
    }
}
```

---

## B. Advanced OOP Features

### Generics

**Repository Interfaces:**
```java
public interface CrudRepository<T, ID> {
    void save(T entity);
    T findById(ID id);
    List<T> findAll();
    void update(T entity);
    void deleteById(ID id);
}

public interface BookRepository extends CrudRepository<Book, Integer> { }
```

**Validatable Interface:**
```java
public interface Validatable<T> {
    boolean validate();
    String getValidationError();
}
```

### Lambda Expressions

**Sorting in Service Layer:**
```java
// Sort by title using lambda
return SortingUtils.sortBy(books, 
    (b1, b2) -> b1.getTitle().compareToIgnoreCase(b2.getTitle()));

// Filter using stream and lambda
return allBooks.stream()
    .filter(book -> book.getTitle().toLowerCase().contains(keyword))
    .collect(Collectors.toList());
```

**SortingUtils:**
```java
public static <T> List<T> sortBy(List<T> list, Comparator<T> comparator) {
    List<T> sorted = new ArrayList<>(list);
    sorted.sort(comparator);  // Lambda passed as Comparator
    return sorted;
}
```

### Reflection/RTTI

**ReflectionUtils.java:**
```java
public static void printClassInfo(Class<?> clazz) {
    System.out.println("Name: " + clazz.getName());
    System.out.println("Superclass: " + clazz.getSuperclass().getSimpleName());
}

public static void printFields(Class<?> clazz) {
    for (Field field : clazz.getDeclaredFields()) {
        System.out.println(field.getType().getSimpleName() + " " + field.getName());
    }
}

public static void inspectObject(Object obj) {
    Field[] fields = obj.getClass().getDeclaredFields();
    for (Field field : fields) {
        field.setAccessible(true);
        Object value = field.get(obj);
        System.out.println(field.getName() + " = " + value);
    }
}
```

### Interface Default & Static Methods

**Validatable Interface:**
```java
public interface Validatable<T> {
    // Abstract methods
    boolean validate();
    String getValidationError();
    
    // Default method
    default String getValidationSummary() {
        return String.format("[%s] Valid: %s", 
            this.getClass().getSimpleName(), validate());
    }
    
    // Static methods
    static <T> boolean isNotNull(T obj) { return obj != null; }
    static boolean isValidYear(int year) { return year > 0 && year <= 2026; }
}
```

**Borrowable Interface:**
```java
public interface Borrowable {
    // Abstract
    boolean isAvailable();
    void borrow();
    void returnItem();
    
    // Default method
    default String getBorrowingStatus() {
        return isAvailable() ? "Available" : "Borrowed";
    }
    
    // Static method
    static boolean isValidBorrowingPeriod(int days) {
        return days > 0 && days <= 30;
    }
}
```

---

## C. OOP Documentation

### Abstract Class & Subclasses

```
Book (abstract)
├── EBook
└── PrintedBook
```

**Book.java (Abstract Base Entity):**
- **Fields:** `id`, `title`, `author`, `year`, `isbn`, `bookType`
- **Abstract methods:** `calculateLateFee()`, `getAccessInstructions()`
- **Concrete method:** `displayInfo()`

### Composition Relationship

```
Book ───HAS-A───► Author
```

- A `Book` cannot exist without an `Author`
- Author is embedded as object, stored via foreign key in database
- Demonstrated via `book.getAuthor().getName()`

### Polymorphism

```java
// Same method call, different behavior based on runtime type
Book book1 = new EBook(...);
Book book2 = new PrintedBook(...);

book1.calculateLateFee(10);  // Returns 10 * 0.25 = 2.50
book2.calculateLateFee(10);  // Returns 10 * 0.50 = 5.00
```

### UML Diagram

```
┌─────────────────────┐
│    <<abstract>>     │
│        Book         │
├─────────────────────┤
│ - id: int           │
│ - title: String     │
│ - author: Author    │
│ - year: int         │
│ - isbn: String      │
├─────────────────────┤
│ + calculateLateFee()│
│ + getAccessInstructions()│
│ + displayInfo()     │
└────────┬────────────┘
         │ extends
    ┌────┴────┐
    │         │
┌───┴───┐ ┌───┴────┐
│ EBook │ │Printed │
│       │ │  Book  │
└───────┘ └────────┘

┌─────────────────────┐
│  <<interface>>      │
│ CrudRepository<T,ID>│
├─────────────────────┤
│ + save(T)           │
│ + findById(ID): T   │
│ + findAll(): List<T>│
│ + update(T)         │
│ + deleteById(ID)    │
└────────┬────────────┘
         │ extends
    ┌────┴────┐
    │         │
┌───┴────┐ ┌──┴──────┐
│ Book   │ │ Author  │
│ Repo   │ │  Repo   │
└────────┘ └─────────┘
```

---

## D. Database Section

### Schema

```sql
CREATE TABLE authors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    nationality VARCHAR(255) NOT NULL,
    birthyear INTEGER CHECK (birthyear > 0 AND birthyear < 2026)
);

CREATE TABLE books (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    isbn VARCHAR(50) UNIQUE,
    author_id INTEGER REFERENCES authors(id),
    publish_year INTEGER CHECK (publish_year > 0 AND publish_year <= 2026),
    book_type VARCHAR(20) NOT NULL,
    download_url VARCHAR(500),
    file_size DOUBLE PRECISION,
    shelf_location VARCHAR(100),
    weight DOUBLE PRECISION,
    available BOOLEAN DEFAULT TRUE
);
```

### Constraints

| Constraint | Table | Description |
|------------|-------|-------------|
| PRIMARY KEY | authors, books | Unique identifier |
| FOREIGN KEY | books.author_id | References authors.id |
| UNIQUE | books.isbn | No duplicate ISBNs |
| CHECK | birthyear, publish_year | Valid year range |

### Sample Inserts

```sql
INSERT INTO authors (name, nationality, birthyear) VALUES
    ('George Orwell', 'British', 1903),
    ('J.K. Rowling', 'British', 1965);

INSERT INTO books (title, isbn, author_id, publish_year, book_type, download_url, file_size)
VALUES ('1984', '9780451524935', 1, 1949, 'EBOOK', 'https://ebooks.example.com/1984', 1.2);
```

---

## E. Architecture Explanation

### Layered Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    CONTROLLER LAYER                      │
│  LibraryController.java                                  │
│  - Handles user input (CLI menu)                         │
│  - NO business logic                                     │
│  - Delegates to service interfaces                       │
└───────────────────────┬─────────────────────────────────┘
                        │ uses
                        ▼
┌─────────────────────────────────────────────────────────┐
│                     SERVICE LAYER                        │
│  BookServiceImpl, AuthorServiceImpl                      │
│  - Business logic and validation                         │
│  - Uses repository interfaces (DIP)                      │
│  - Throws custom exceptions                              │
└───────────────────────┬─────────────────────────────────┘
                        │ uses
                        ▼
┌─────────────────────────────────────────────────────────┐
│                   REPOSITORY LAYER                       │
│  BookRepositoryImpl, AuthorRepositoryImpl                │
│  - CRUD operations only                                  │
│  - Implements generic CrudRepository<T, ID>              │
│  - JDBC with PreparedStatements                          │
└───────────────────────┬─────────────────────────────────┘
                        │ JDBC
                        ▼
┌─────────────────────────────────────────────────────────┐
│                      DATABASE                            │
│  PostgreSQL (authors, books tables)                      │
└─────────────────────────────────────────────────────────┘
```

### Request/Response Flow Example

**Create Book Request:**
1. `LibraryController.addEBook()` - Gets user input
2. `BookServiceImpl.createBook()` - Validates, checks author exists
3. `BookRepositoryImpl.save()` - Executes INSERT query
4. Response bubbles back up

---

## F. Execution Instructions

### Requirements

- Java 17 or higher
- PostgreSQL database
- PostgreSQL JDBC Driver (`postgresql-*.jar`)

### Setup

1. **Create Database:**
```bash
psql -U postgres
CREATE DATABASE librarydb;
\c librarydb
```

2. **Run Schema:**
```bash
psql -U postgres -d librarydb -f src/resources/schema.sql
```

3. **Update Database Credentials:**
Edit `src/utils/DatabaseConnection.java`:
```java
private static final String URL = "jdbc:postgresql://localhost:5432/librarydb";
private static final String USER = "postgres";
private static final String PASSWORD = "your_password";
```

4. **Compile:**
```bash
cd src
javac -cp ".:../lib/postgresql-*.jar" -d ../out \
    model/*.java exception/*.java utils/*.java \
    repository/interfaces/*.java repository/*.java \
    service/interfaces/*.java service/*.java \
    controller/*.java Main.java
```

5. **Run (Interactive Mode):**
```bash
cd ..
java -cp "out:lib/postgresql-*.jar" Main
```

6. **Run (Demo Mode):**
```bash
java -cp "out:lib/postgresql-*.jar" Main --demo
```

---

## G. Screenshots

Screenshots are located in `docs/screenshots/`:

1. **CRUD Operations** - Successful create, read, update, delete
2. **Validation Failures** - Exception handling demonstration
3. **Reflection Output** - ReflectionUtils class inspection
4. **Sorted Lists** - Lambda sorting demonstration
5. **Interface Demo** - Default and static method usage

---

## H. Reflection

### What I Learned

- **SOLID Principles** provide a framework for maintainable, extensible code
- **Dependency Injection** makes code more testable and loosely coupled
- **Generics** enable type-safe, reusable components
- **Lambdas** simplify functional operations like sorting and filtering
- **Reflection** enables powerful runtime introspection capabilities

### Challenges

- Refactoring existing code to follow DIP required careful restructuring
- Ensuring LSP compliance in subclasses needed attention to behavioral correctness
- Balancing interface granularity (ISP) with usability

### Value of SOLID Architecture

1. **Maintainability** - Changes in one layer don't affect others
2. **Testability** - Interfaces enable mock implementations
3. **Extensibility** - New features added without modifying existing code
4. **Readability** - Clear separation of concerns

---

## Project Structure

```
library-management-system/
├── src/
│   ├── controller/
│   │   └── LibraryController.java
│   ├── service/
│   │   ├── interfaces/
│   │   │   ├── BookServiceInterface.java
│   │   │   └── AuthorServiceInterface.java
│   │   ├── BookServiceImpl.java
│   │   └── AuthorServiceImpl.java
│   ├── repository/
│   │   ├── interfaces/
│   │   │   ├── CrudRepository.java
│   │   │   ├── BookRepository.java
│   │   │   └── AuthorRepository.java
│   │   ├── BookRepositoryImpl.java
│   │   └── AuthorRepositoryImpl.java
│   ├── model/
│   │   ├── Book.java
│   │   ├── EBook.java
│   │   ├── PrintedBook.java
│   │   ├── Author.java
│   │   ├── Borrowable.java
│   │   ├── DigitalAccess.java
│   │   └── Validatable.java
│   ├── exception/
│   │   ├── InvalidInputException.java
│   │   ├── DuplicateResourceException.java
│   │   ├── ResourceNotFoundException.java
│   │   └── DatabaseOperationException.java
│   ├── utils/
│   │   ├── DatabaseConnection.java
│   │   ├── ReflectionUtils.java
│   │   └── SortingUtils.java
│   ├── resources/
│   │   └── schema.sql
│   └── Main.java
├── docs/
│   ├── screenshots/
│   └── uml.png
├── README.md
└── .gitignore
```


