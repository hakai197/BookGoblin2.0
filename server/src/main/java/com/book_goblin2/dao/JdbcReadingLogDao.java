package com.book_goblin2.dao;

import com.book_goblin2.dao.ReadingLogDao;
import com.book_goblin2.model.ReadingLog;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcReadingLogDao implements ReadingLogDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcReadingLogDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<ReadingLog> getReadingLogsForUserBook(int userBookId) {
        List<ReadingLog> readingLogs = new ArrayList<>();
        String sql = "SELECT log_id, user_book_id, start_date, end_date, rating, notes " +
                "FROM reading_logs WHERE user_book_id = ? ORDER BY start_date DESC";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userBookId);
        while (results.next()) {
            readingLogs.add(mapRowToReadingLog(results));
        }
        return readingLogs;
    }

    @Override
    public ReadingLog createReadingLog(ReadingLog readingLog) {
        String sql = "INSERT INTO reading_logs (user_book_id, start_date, end_date, rating, notes) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING log_id";
        Integer newId = jdbcTemplate.queryForObject(sql, Integer.class,
                readingLog.getUserBookId(),
                new java.sql.Date(readingLog.getStartDate()),
                readingLog.getEndDate() != null ? new java.sql.Date(readingLog.getEndDate()) : null,
                readingLog.getRating(),
                readingLog.getNotes());
        return getReadingLogById(newId);
    }

    @Override
    public boolean updateReadingLog(ReadingLog readingLog) {
        String sql = "UPDATE reading_logs SET end_date = ?, rating = ?, notes = ? " +
                "WHERE log_id = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                readingLog.getEndDate() != null ? new java.sql.Date(readingLog.getEndDate()) : null,
                readingLog.getRating(),
                readingLog.getNotes(),
                readingLog.getLogId());
        return rowsAffected == 1;
    }

    @Override
    public boolean deleteReadingLog(int logId) {
        String sql = "DELETE FROM reading_logs WHERE log_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, logId);
        return rowsAffected == 1;
    }

    @Override
    public ReadingLog getReadingLogById(int logId) {
        String sql = "SELECT log_id, user_book_id, start_date, end_date, rating, notes " +
                "FROM reading_logs WHERE log_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, logId);
        if (results.next()) {
            return mapRowToReadingLog(results);
        }
        return null;
    }

    private ReadingLog mapRowToReadingLog(SqlRowSet rs) {
        ReadingLog readingLog = new ReadingLog(
                rs.getInt("user_book_id"),
                rs.getDate("start_date").getTime()
        );
        readingLog.setLogId(rs.getInt("log_id"));
        if (rs.getDate("end_date") != null) {
            readingLog.setEndDate(rs.getDate("end_date").getTime());
        }
        readingLog.setRating(rs.getInt("rating"));
        readingLog.setNotes(rs.getString("notes"));
        return readingLog;
    }
}