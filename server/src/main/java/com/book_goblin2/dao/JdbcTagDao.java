package com.book_goblin2.dao;

import com.book_goblin2.dao.TagDao;
import com.book_goblin2.model.Tag;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTagDao implements TagDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTagDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Tag> getAllTags() {
        List<Tag> tags = new ArrayList<>();
        String sql = "SELECT tag_id, name FROM tags";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            tags.add(mapRowToTag(results));
        }
        return tags;
    }

    @Override
    public Tag getTagById(int tagId) {
        String sql = "SELECT tag_id, name FROM tags WHERE tag_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, tagId);
        if (results.next()) {
            return mapRowToTag(results);
        }
        return null;
    }

    @Override
    public Tag getTagByName(String name) {
        String sql = "SELECT tag_id, name FROM tags WHERE name = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, name);
        if (results.next()) {
            return mapRowToTag(results);
        }
        return null;
    }

    @Override
    public Tag createTag(Tag tag) {
        String sql = "INSERT INTO tags (name) VALUES (?) RETURNING tag_id";
        Integer newId = jdbcTemplate.queryForObject(sql, Integer.class, tag.getName());
        return getTagById(newId);
    }

    @Override
    public boolean updateTag(Tag tag) {
        String sql = "UPDATE tags SET name = ? WHERE tag_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, tag.getName(), tag.getTagId());
        return rowsAffected == 1;
    }

    @Override
    public boolean deleteTag(int tagId) {
        String sql = "DELETE FROM tags WHERE tag_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, tagId);
        return rowsAffected == 1;
    }

    private Tag mapRowToTag(SqlRowSet rs) {
        Tag tag = new Tag(rs.getString("name"));
        tag.setTagId(rs.getInt("tag_id"));
        return tag;
    }
}
