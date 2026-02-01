package service;

import model.Book;
import model.EBook;
import model.PrintedBook;
import repository.interfaces.BookRepository;
import repository.interfaces.AuthorRepository;
import service.interfaces.BookServiceInterface;
import exception.*;
import utils.SortingUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BookServiceImpl implements BookServiceInterface {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public void createBook(Book book)
            throws InvalidInputException, ResourceNotFoundException, DuplicateResourceException {
        validateBook(book);

        if (!authorRepository.existsById(book.getAuthor().getId())) {
            throw new ResourceNotFoundException("Author with ID " + book.getAuthor().getId() + " not found");
        }

        Book existing = bookRepository.findByIsbn(book.getIsbn());
        if (existing != null) {
            throw new DuplicateResourceException("Book with ISBN '" + book.getIsbn() + "' already exists");
        }

        bookRepository.save(book);
    }

    @Override
    public Book getBookById(int id) throws ResourceNotFoundException {
        Book book = bookRepository.findById(id);
        if (book == null) {
            throw new ResourceNotFoundException("Book with ID " + id + " not found");
        }
        return book;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> getBooksSortedByTitle() {
        List<Book> books = bookRepository.findAll();
        return SortingUtils.sortBy(books, (b1, b2) -> b1.getTitle().compareToIgnoreCase(b2.getTitle()));
    }

    @Override
    public List<Book> getBooksSortedByYear() {
        List<Book> books = bookRepository.findAll();
        return SortingUtils.sortBy(books, (b1, b2) -> Integer.compare(b1.getYear(), b2.getYear()));
    }

    @Override
    public void updateBook(Book book) throws ResourceNotFoundException, InvalidInputException {
        if (!bookRepository.existsById(book.getId())) {
            throw new ResourceNotFoundException("Book with ID " + book.getId() + " not found");
        }

        validateBook(book);

        bookRepository.update(book);
    }

    @Override
    public void deleteBook(int id) throws ResourceNotFoundException {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book with ID " + id + " not found");
        }
        bookRepository.deleteById(id);
    }

    @Override
    public List<Book> searchByTitle(String keyword) {
        List<Book> allBooks = bookRepository.findAll();
        return allBooks.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    private void validateBook(Book book) throws InvalidInputException {
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new InvalidInputException("Title cannot be empty");
        }
        if (book.getYear() < 0 || book.getYear() > 2026) {
            throw new InvalidInputException("Invalid year");
        }
        if (book.getAuthor() == null) {
            throw new InvalidInputException("Author cannot be null");
        }

        if (book instanceof EBook) {
            EBook ebook = (EBook) book;
            if (ebook.getFileSize() < 0) {
                throw new InvalidInputException("File size cannot be negative");
            }
        } else if (book instanceof PrintedBook) {
            PrintedBook pbook = (PrintedBook) book;
            if (pbook.getWeight() < 0) {
                throw new InvalidInputException("Weight cannot be negative");
            }
        }
    }
}
