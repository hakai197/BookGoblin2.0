package com.book_goblin2.dao;

import com.book_goblin2.dao.BookTagDao;
import com.book_goblin2.model.BookTag;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcBookTagDao implements BookTagDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcBookTagDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<BookTag> getTagsForBook(int bookId) {
        List<BookTag> bookTags = new ArrayList<>();
        String sql = "SELECT book_tag_id, book_id, tag_id FROM book_tags WHERE book_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, bookId);
        while (results.next()) {
            bookTags.add(mapRowToBookTag(results));
        }
        return bookTags;
    }

    @Override
    public BookTag addTagToBook(BookTag bookTag) {
        String sql = "INSERT INTO book_tags (book_id, tag_id) VALUES (?, ?) RETURNING book_tag_id";
        Integer newId = jdbcTemplate.queryForObject(sql, Integer.class,
                bookTag.getBookId(), bookTag.getTagId());
        return getBookTagById(newId);
    }

    @Override
    public boolean removeTagFromBook(int bookTagId) {
        String sql = "DELETE FROM book_tags WHERE book_tag_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, bookTagId);
        return rowsAffected == 1;
    }

    @Override
    public boolean doesBookTagExist(int bookId, int tagId) {
        String sql = "SELECT COUNT(*) FROM book_tags WHERE book_id = ? AND tag_id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, bookId, tagId);
        return count > 0;
    }

    private BookTag getBookTagById(int bookTagId) {
        String sql = "SELECT book_tag_id, book_id, tag_id FROM book_tags WHERE book_tag_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, bookTagId);
        if (results.next()) {
            return mapRowToBookTag(results);
        }
        return null;
    }

    private BookTag mapRowToBookTag(SqlRowSet rs) {
        BookTag bookTag = new BookTag(
                rs.getInt("book_id"),
                rs.getInt("tag_id")
        );
        bookTag.setBookTagId(rs.getInt("book_tag_id"));
        return bookTag;
    }
}
