package com.book_goblin2.controller;

import com.book_goblin2.dao.BookTagDao;
import com.book_goblin2.model.BookTag;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/book-tags")
@CrossOrigin
public class BookTagController {

    private final BookTagDao bookTagDao;

    public BookTagController(BookTagDao bookTagDao) {
        this.bookTagDao = bookTagDao;
    }

    @GetMapping("/book/{bookId}")
    public List<BookTag> getTagsForBook(@PathVariable int bookId) {
        return bookTagDao.getTagsForBook(bookId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public BookTag addTagToBook(@RequestBody BookTag bookTag) {
        return bookTagDao.addTagToBook(bookTag);
    }

    @DeleteMapping("/{bookTagId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void removeTagFromBook(@PathVariable int bookTagId) {
        if (!bookTagDao.removeTagFromBook(bookTagId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "BookTag association not found");
        }
    }
}