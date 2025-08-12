package com.book_goblin2.model;

public class BookTag {
    private int bookTagId;
    private int bookId;
    private int tagId;

    // Constructor
    public BookTag(int bookId, int tagId) {
        this.bookId = bookId;
        this.tagId = tagId;
    }

    // Getters and Setters
    public int getBookTagId() { return bookTagId; }
    public void setBookTagId(int bookTagId) { this.bookTagId = bookTagId; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public int getTagId() { return tagId; }
    public void setTagId(int tagId) { this.tagId = tagId; }
}
