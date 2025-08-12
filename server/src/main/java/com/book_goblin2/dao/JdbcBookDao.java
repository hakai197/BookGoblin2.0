package com.book_goblin2.dao;

import com.book_goblin2.dao.BookDao;
import com.book_goblin2.model.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcBookDao implements BookDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcBookDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT book_id, title, author, isbn, cover_image_url, publication_year FROM books";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            books.add(mapRowToBook(results));
        }
        return books;
    }

    @Override
    public Book getBookById(int bookId) {
        String sql = "SELECT book_id, title, author, isbn, cover_image_url, publication_year FROM books WHERE book_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, bookId);
        if (results.next()) {
            return mapRowToBook(results);
        }
        return null;
    }

    @Override
    public Book getBookByIsbn(String isbn) {
        String sql = "SELECT book_id, title, author, isbn, cover_image_url, publication_year FROM books WHERE isbn = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, isbn);
        if (results.next()) {
            return mapRowToBook(results);
        }
        return null;
    }

    @Override
    public Book createBook(Book book) {
        String sql = "INSERT INTO books (title, author, isbn, cover_image_url, publication_year) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING book_id";
        Integer newId = jdbcTemplate.queryForObject(sql, Integer.class,
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getCoverImageUrl(),
                book.getPublicationYear());
        return getBookById(newId);
    }

    @Override
    public boolean updateBook(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, cover_image_url = ?, publication_year = ? " +
                "WHERE book_id = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getCoverImageUrl(),
                book.getPublicationYear(),
                book.getBookId());
        return rowsAffected == 1;
    }

    @Override
    public boolean deleteBook(int bookId) {
        String sql = "DELETE FROM books WHERE book_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, bookId);
        return rowsAffected == 1;
    }

    @Override
    public List<Book> searchBooksByTitle(String title) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT book_id, title, author, isbn, cover_image_url, publication_year " +
                "FROM books WHERE title ILIKE ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, "%" + title + "%");
        while (results.next()) {
            books.add(mapRowToBook(results));
        }
        return books;
    }

    @Override
    public List<Book> searchBooksByAuthor(String author) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT book_id, title, author, isbn, cover_image_url, publication_year " +
                "FROM books WHERE author ILIKE ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, "%" + author + "%");
        while (results.next()) {
            books.add(mapRowToBook(results));
        }
        return books;
    }

    private Book mapRowToBook(SqlRowSet rs) {
        Book book = new Book(
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("isbn"),
                rs.getString("cover_image_url"),
                rs.getInt("publication_year")
        );
        book.setBookId(rs.getInt("book_id"));
        return book;
    }
}