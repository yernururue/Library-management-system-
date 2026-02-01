package service;

import model.Author;
import repository.interfaces.AuthorRepository;
import service.interfaces.AuthorServiceInterface;
import exception.*;
import utils.SortingUtils;

import java.util.List;

public class AuthorServiceImpl implements AuthorServiceInterface {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public void createAuthor(Author author) throws InvalidInputException, DuplicateResourceException {
        if (author.getName() == null || author.getName().trim().isEmpty()) {
            throw new InvalidInputException("Name cannot be empty");
        }
        if (author.getBirthYear() < 0) {
            throw new InvalidInputException("Birth year cannot be negative");
        }
        if (author.getNationality() == null || author.getNationality().trim().isEmpty()) {
            throw new InvalidInputException("Nationality cannot be empty");
        }

        Author existing = authorRepository.findByName(author.getName());
        if (existing != null) {
            throw new DuplicateResourceException("Author with name '" + author.getName() + "' already exists");
        }

        authorRepository.save(author);
    }

    @Override
    public Author getAuthorById(int id) throws ResourceNotFoundException {
        Author author = authorRepository.findById(id);
        if (author == null) {
            throw new ResourceNotFoundException("Author with ID " + id + " not found");
        }
        return author;
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public List<Author> getAuthorsSortedByName() {
        List<Author> authors = authorRepository.findAll();
        return SortingUtils.sortBy(authors, (a1, a2) -> a1.getName().compareToIgnoreCase(a2.getName()));
    }

    @Override
    public void updateAuthor(Author author) throws ResourceNotFoundException, InvalidInputException {
        if (!authorRepository.existsById(author.getId())) {
            throw new ResourceNotFoundException("Author with ID " + author.getId() + " not found");
        }

        if (author.getName() == null || author.getName().trim().isEmpty()) {
            throw new InvalidInputException("Name cannot be empty");
        }

        authorRepository.update(author);
    }

    @Override
    public void deleteAuthor(int id) throws ResourceNotFoundException, InvalidInputException {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Author with ID " + id + " not found");
        }

        try {
            authorRepository.deleteById(id);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof DatabaseOperationException) {
                throw new InvalidInputException("Cannot delete author: books reference this author");
            }
            throw e;
        }
    }
}
