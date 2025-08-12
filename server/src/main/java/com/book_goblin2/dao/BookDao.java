package com.book_goblin2.dao;

import com.book_goblin2.model.Book;
import java.util.List;

public interface BookDao {
    /**
     * Get all books from the database
     */
    List<Book> getAllBooks();

    /**
     * Get a book by its ID
     */
    Book getBookById(int bookId);

    /**
     * Get a book by its ISBN
     */
    Book getBookByIsbn(String isbn);

    /**
     * Create a new book in the database
     */
    Book createBook(Book book);

    /**
     * Update an existing book
     */
    boolean updateBook(Book book);

    /**
     * Delete a book by its ID
     */
    boolean deleteBook(int bookId);

    /**
     * Search books by title (partial match)
     */
    List<Book> searchBooksByTitle(String title);

    /**
     * Search books by author (partial match)
     */
    List<Book> searchBooksByAuthor(String author);
}
