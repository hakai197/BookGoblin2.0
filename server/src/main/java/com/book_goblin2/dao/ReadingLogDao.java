package com.book_goblin2.dao;

import com.book_goblin2.model.ReadingLog;
import java.util.List;

public interface ReadingLogDao {
    List<ReadingLog> getReadingLogsForUserBook(int userBookId);
    ReadingLog createReadingLog(ReadingLog readingLog);
    boolean updateReadingLog(ReadingLog readingLog);
    boolean deleteReadingLog(int logId);
    ReadingLog getReadingLogById(int logId);
}