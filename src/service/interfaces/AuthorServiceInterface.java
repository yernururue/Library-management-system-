package service.interfaces;

import model.Author;
import exception.*;
import java.util.List;

public interface AuthorServiceInterface {

    void createAuthor(Author author) throws InvalidInputException, DuplicateResourceException;

    Author getAuthorById(int id) throws ResourceNotFoundException;

    List<Author> getAllAuthors();

    List<Author> getAuthorsSortedByName();

    void updateAuthor(Author author) throws ResourceNotFoundException, InvalidInputException;

    void deleteAuthor(int id) throws ResourceNotFoundException, InvalidInputException;
}
