package repository.interfaces;

import model.Author;
import java.util.List;

public interface AuthorRepository extends CrudRepository<Author, Integer> {

    Author findByName(String name);

    List<Author> findByNationality(String nationality);
}
