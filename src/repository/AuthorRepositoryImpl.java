package repository;

import model.Author;
import repository.interfaces.AuthorRepository;
import utils.DatabaseConnection;
import exception.DatabaseOperationException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorRepositoryImpl implements AuthorRepository {

    @Override
    public void save(Author author) {
        String sql = "INSERT INTO authors(name, birthyear, nationality) VALUES(?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, author.getName());
            ps.setInt(2, author.getBirthYear());
            ps.setString(3, author.getNationality());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                author.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(new DatabaseOperationException("Failed to save author", e));
        }
    }

    @Override
    public Author findById(Integer id) {
        String sql = "SELECT * FROM authors WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToAuthor(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(new DatabaseOperationException("Failed to find author by ID", e));
        }
    }

    @Override
    public List<Author> findAll() {
        String sql = "SELECT * FROM authors";
        List<Author> authors = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                authors.add(mapResultSetToAuthor(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(new DatabaseOperationException("Failed to retrieve authors", e));
        }
        return authors;
    }

    @Override
    public void update(Author author) {
        String sql = "UPDATE authors SET name = ?, birthyear = ?, nationality = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, author.getName());
            ps.setInt(2, author.getBirthYear());
            ps.setString(3, author.getNationality());
            ps.setInt(4, author.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(new DatabaseOperationException("Failed to update author", e));
        }
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM authors WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key") || e.getMessage().contains("violates")) {
                throw new RuntimeException(new DatabaseOperationException(
                        "Cannot delete author: books reference this author", e));
            }
            throw new RuntimeException(new DatabaseOperationException("Failed to delete author", e));
        }
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT COUNT(*) FROM authors WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(new DatabaseOperationException("Failed to check author existence", e));
        }
    }

    @Override
    public Author findByName(String name) {
        String sql = "SELECT * FROM authors WHERE name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToAuthor(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(new DatabaseOperationException("Failed to find author by name", e));
        }
    }

    @Override
    public List<Author> findByNationality(String nationality) {
        String sql = "SELECT * FROM authors WHERE nationality = ?";
        List<Author> authors = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nationality);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                authors.add(mapResultSetToAuthor(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(new DatabaseOperationException("Failed to find authors by nationality", e));
        }
        return authors;
    }

    private Author mapResultSetToAuthor(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        int birthYear = rs.getInt("birthyear");
        String nationality = rs.getString("nationality");
        return new Author(id, name, birthYear, nationality);
    }
}
