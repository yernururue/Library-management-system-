package service.interfaces;

import model.Book;
import exception.*;
import java.util.List;

public interface BookServiceInterface {

    void createBook(Book book) throws InvalidInputException, ResourceNotFoundException, DuplicateResourceException;

    Book getBookById(int id) throws ResourceNotFoundException;

    List<Book> getAllBooks();

    List<Book> getBooksSortedByTitle();

    List<Book> getBooksSortedByYear();

    void updateBook(Book book) throws ResourceNotFoundException, InvalidInputException;

    void deleteBook(int id) throws ResourceNotFoundException;

    List<Book> searchByTitle(String keyword);
}
