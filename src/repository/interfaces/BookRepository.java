package repository.interfaces;

import model.Book;
import java.util.List;

public interface BookRepository extends CrudRepository<Book, Integer> {

    Book findByIsbn(String isbn);

    List<Book> findByAuthorId(int authorId);

    List<Book> findByType(String bookType);
}
