package com.book_goblin2.controller;

import com.book_goblin2.dao.ReadingLogDao;
import com.book_goblin2.model.ReadingLog;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/reading-logs")
@CrossOrigin
public class ReadingLogController {

    private final ReadingLogDao readingLogDao;

    public ReadingLogController(ReadingLogDao readingLogDao) {
        this.readingLogDao = readingLogDao;
    }

    @GetMapping("/user-book/{userBookId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<ReadingLog> getReadingLogsForUserBook(@PathVariable int userBookId) {
        return readingLogDao.getReadingLogsForUserBook(userBookId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ReadingLog createReadingLog(@RequestBody ReadingLog readingLog) {
        return readingLogDao.createReadingLog(readingLog);
    }

    @PutMapping("/{logId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ReadingLog updateReadingLog(@PathVariable int logId, @RequestBody ReadingLog readingLog) {
        readingLog.setLogId(logId);
        if (!readingLogDao.updateReadingLog(readingLog)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reading log not found");
        }
        return readingLog;
    }

    @DeleteMapping("/{logId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteReadingLog(@PathVariable int logId) {
        if (!readingLogDao.deleteReadingLog(logId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reading log not found");
        }
    }
}
