package com.book_goblin2.controller;

import com.book_goblin2.dao.BookDao;
import com.book_goblin2.model.Book;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
@CrossOrigin
public class BookController {

    private final BookDao bookDao;
    private final RestTemplate restTemplate;
    private static final String OPEN_LIBRARY_URL = "https://openlibrary.org/search.json";

    public BookController(BookDao bookDao, RestTemplate restTemplate) {
        this.bookDao = bookDao;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam String query) {
        String url = OPEN_LIBRARY_URL + "?q=" + query + "&fields=title,author_name,isbn,cover_i,first_publish_year&limit=10";

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            List<Map<String, Object>> docs = (List<Map<String, Object>>) response.get("docs");

            List<Book> books = new ArrayList<>();
            for (Map<String, Object> doc : docs) {
                Book book = new Book(
                        (String) doc.get("title"),
                        doc.containsKey("author_name") ? ((List<String>) doc.get("author_name")).get(0) : "Unknown Author",
                        doc.containsKey("isbn") ? ((List<String>) doc.get("isbn")).get(0) : null,
                        doc.containsKey("cover_i") ? "https://covers.openlibrary.org/b/id/" + doc.get("cover_i") + "-M.jpg" : null,
                        doc.containsKey("first_publish_year") ? (Integer) doc.get("first_publish_year") : 0
                );
                books.add(book);
            }
            return books;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error searching books", e);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Book> addBookToLibrary(@RequestBody Book book) {
        try {
            // Check if book already exists in DB by ISBN
            Book existingBook = bookDao.getBookByIsbn(book.getIsbn());
            if (existingBook == null) {
                // Book doesn't exist, create new record
                existingBook = bookDao.createBook(book);
            }
            return ResponseEntity.ok(existingBook);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding book to library", e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable int id) {
        Book book = bookDao.getBookById(id);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }
}