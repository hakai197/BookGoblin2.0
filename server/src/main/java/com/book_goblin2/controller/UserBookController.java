package com.book_goblin2.controller;

import com.book_goblin2.dao.UserBookDao;
import com.book_goblin2.model.UserBook;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/user-books")
@CrossOrigin
public class UserBookController {

    private final UserBookDao userBookDao;

    public UserBookController(UserBookDao userBookDao) {
        this.userBookDao = userBookDao;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<UserBook> getUserBooks(Principal principal) {
        // In a real app, you'd get the user ID from the principal
        // For demo, we'll use a hardcoded user ID
        int userId = 1; // Replace with actual user ID from authentication
        return userBookDao.getUserBooks(userId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public UserBook addUserBook(@RequestBody UserBook userBook, Principal principal) {
        // Set user ID from authentication
        int userId = 1; // Replace with actual user ID from authentication
        userBook.setUserId(userId);
        return userBookDao.addUserBook(userBook);
    }

    @PutMapping("/{userBookId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public UserBook updateUserBook(@PathVariable int userBookId, @RequestBody UserBook userBook) {
        userBook.setUserBookId(userBookId);
        if (!userBookDao.updateUserBook(userBook)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "UserBook not found");
        }
        return userBook;
    }

    @DeleteMapping("/{userBookId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void removeUserBook(@PathVariable int userBookId) {
        if (!userBookDao.removeUserBook(userBookId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "UserBook not found");
        }
    }
}