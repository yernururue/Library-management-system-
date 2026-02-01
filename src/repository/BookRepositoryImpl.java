package repository;

import model.Author;
import model.Book;
import model.EBook;
import model.PrintedBook;
import repository.interfaces.BookRepository;
import repository.interfaces.AuthorRepository;
import utils.DatabaseConnection;
import exception.DatabaseOperationException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRepositoryImpl implements BookRepository {

    private final AuthorRepository authorRepository;

    public BookRepositoryImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public void save(Book book) {
        if (book instanceof EBook) {
            saveEBook((EBook) book);
        } else if (book instanceof PrintedBook) {
            savePrintedBook((PrintedBook) book);
        }
    }

    private void saveEBook(EBook book) {
        String sql = "INSERT INTO books(title, isbn, author_id, publish_year, book_type, download_url, file_size, available) "
                +
                "VALUES(?, ?, ?, ?, 'EBOOK', ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getIsbn());
            ps.setInt(3, book.getAuthor().getId());
            ps.setInt(4, book.getYear());
            ps.setString(5, book.getDownloadURL());
            ps.setDouble(6, book.getFileSize());
            ps.setBoolean(7, book.isAvailable());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                book.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(new DatabaseOperationException("Failed to save EBook", e));
        }
    }

    private void savePrintedBook(PrintedBook book) {
        String sql = "INSERT INTO books(title, isbn, author_id, publish_year, book_type, shelf_location, weight, available) "
                +
                "VALUES(?, ?, ?, ?, 'PRINTED', ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getIsbn());
            ps.setInt(3, book.getAuthor().getId());
            ps.setInt(4, book.getYear());
            ps.setString(5, book.getShelfLocation());
            ps.setDouble(6, book.getWeight());
            ps.setBoolean(7, book.isAvailable());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                book.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(new DatabaseOperationException("Failed to save PrintedBook", e));
        }
    }

    @Override
    public Book findById(Integer id) {
        String sql = "SELECT * FROM books WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToBook(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(new DatabaseOperationException("Failed to find book by ID", e));
        }
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM books";
        List<Book> books = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Book book = mapResultSetToBook(rs);
                if (book != null) {
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(new DatabaseOperationException("Failed to retrieve books", e));
        }
        return books;
    }

    @Override
    public void update(Book book) {
        if (book instanceof EBook) {
            updateEBook((EBook) book);
        } else if (book instanceof PrintedBook) {
            updatePrintedBook((PrintedBook) book);
        }
    }

    private void updateEBook(EBook book) {
        String sql = "UPDATE books SET title = ?, isbn = ?, author_id = ?, publish_year = ?, " +
                "download_url = ?, file_size = ?, available = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getIsbn());
            ps.setInt(3, book.getAuthor().getId());
            ps.setInt(4, book.getYear());
            ps.setString(5, book.getDownloadURL());
            ps.setDouble(6, book.getFileSize());
            ps.setBoolean(7, book.isAvailable());
            ps.setInt(8, book.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(new DatabaseOperationException("Failed to update EBook", e));
        }
    }

    private void updatePrintedBook(PrintedBook book) {
        String sql = "UPDATE books SET title = ?, isbn = ?, author_id = ?, publish_year = ?, " +
                "shelf_location = ?, weight = ?, available = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getIsbn());
            ps.setInt(3, book.getAuthor().getId());
            ps.setInt(4, book.getYear());
            ps.setString(5, book.getShelfLocation());
            ps.setDouble(6, book.getWeight());
            ps.setBoolean(7, book.isAvailable());
            ps.setInt(8, book.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(new DatabaseOperationException("Failed to update PrintedBook", e));
        }
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(new DatabaseOperationException("Failed to delete book", e));
        }
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT COUNT(*) FROM books WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(new DatabaseOperationException("Failed to check book existence", e));
        }
    }

    @Override
    public Book findByIsbn(String isbn) {
        String sql = "SELECT * FROM books WHERE isbn = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToBook(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(new DatabaseOperationException("Failed to find book by ISBN", e));
        }
    }

    @Override
    public List<Book> findByAuthorId(int authorId) {
        String sql = "SELECT * FROM books WHERE author_id = ?";
        List<Book> books = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, authorId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Book book = mapResultSetToBook(rs);
                if (book != null) {
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(new DatabaseOperationException("Failed to find books by author ID", e));
        }
        return books;
    }

    @Override
    public List<Book> findByType(String bookType) {
        String sql = "SELECT * FROM books WHERE book_type = ?";
        List<Book> books = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, bookType);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Book book = mapResultSetToBook(rs);
                if (book != null) {
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(new DatabaseOperationException("Failed to find books by type", e));
        }
        return books;
    }

    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        String bookType = rs.getString("book_type");
        int authorId = rs.getInt("author_id");
        Author author = authorRepository.findById(authorId);

        if ("EBOOK".equals(bookType)) {
            return new EBook(
                    rs.getInt("id"),
                    rs.getString("title"),
                    author,
                    rs.getInt("publish_year"),
                    rs.getString("isbn"),
                    rs.getDouble("file_size"),
                    rs.getString("download_url"));
        } else if ("PRINTED".equals(bookType)) {
            return new PrintedBook(
                    rs.getInt("id"),
                    rs.getString("title"),
                    author,
                    rs.getInt("publish_year"),
                    rs.getString("isbn"),
                    rs.getString("shelf_location"),
                    rs.getDouble("weight"));
        }
        return null;
    }
}
