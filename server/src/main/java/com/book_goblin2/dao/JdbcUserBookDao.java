package com.book_goblin2.dao;

import com.book_goblin2.dao.UserBookDao;
import com.book_goblin2.model.UserBook;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcUserBookDao implements UserBookDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserBookDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<UserBook> getUserBooks(int userId) {
        List<UserBook> userBooks = new ArrayList<>();
        String sql = "SELECT user_book_id, user_id, book_id, date_added, is_owned, current_status " +
                "FROM user_books WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()) {
            userBooks.add(mapRowToUserBook(results));
        }
        return userBooks;
    }

    @Override
    public UserBook addUserBook(UserBook userBook) {
        String sql = "INSERT INTO user_books (user_id, book_id, is_owned, current_status) " +
                "VALUES (?, ?, ?, ?) RETURNING user_book_id";
        Integer newId = jdbcTemplate.queryForObject(sql, Integer.class,
                userBook.getUserId(),
                userBook.getBookId(),
                userBook.isOwned(),
                userBook.getCurrentStatus());
        return getUserBookById(newId);
    }

    @Override
    public boolean updateUserBook(UserBook userBook) {
        String sql = "UPDATE user_books SET is_owned = ?, current_status = ? " +
                "WHERE user_book_id = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                userBook.isOwned(),
                userBook.getCurrentStatus(),
                userBook.getUserBookId());
        return rowsAffected == 1;
    }

    @Override
    public boolean removeUserBook(int userBookId) {
        String sql = "DELETE FROM user_books WHERE user_book_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, userBookId);
        return rowsAffected == 1;
    }

    @Override
    public UserBook getUserBookById(int userBookId) {
        String sql = "SELECT user_book_id, user_id, book_id, date_added, is_owned, current_status " +
                "FROM user_books WHERE user_book_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userBookId);
        if (results.next()) {
            return mapRowToUserBook(results);
        }
        return null;
    }

    @Override
    public boolean doesUserBookExist(int userId, int bookId) {
        String sql = "SELECT COUNT(*) FROM user_books WHERE user_id = ? AND book_id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, userId, bookId);
        return count > 0;
    }

    private UserBook mapRowToUserBook(SqlRowSet rs) {
        UserBook userBook = new UserBook(
                rs.getInt("user_id"),
                rs.getInt("book_id"),
                rs.getBoolean("is_owned"),
                rs.getString("current_status")
        );
        userBook.setUserBookId(rs.getInt("user_book_id"));
        userBook.setDateAdded(rs.getDate("date_added").getTime());
        return userBook;
    }
}