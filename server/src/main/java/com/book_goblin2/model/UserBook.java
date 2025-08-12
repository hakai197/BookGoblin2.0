package com.book_goblin2.model;

public class UserBook {
    private int userBookId;
    private int userId;
    private int bookId;
    private long dateAdded; // Timestamp
    private boolean isOwned;
    private String currentStatus; // "unread", "reading", "finished", "dnf"

    public UserBook(int userId, int bookId, boolean isOwned, String currentStatus) {
        this.userId = userId;
        this.bookId = bookId;
        this.isOwned = isOwned;
        this.currentStatus = currentStatus;
        this.dateAdded = System.currentTimeMillis();
    }


    public int getUserBookId() { return userBookId; }
    public void setUserBookId(int userBookId) { this.userBookId = userBookId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public long getDateAdded() { return dateAdded; }
    public void setDateAdded(long dateAdded) { this.dateAdded = dateAdded; }

    public boolean isOwned() { return isOwned; }
    public void setOwned(boolean owned) { isOwned = owned; }

    public String getCurrentStatus() { return currentStatus; }
    public void setCurrentStatus(String currentStatus) { this.currentStatus = currentStatus; }
}

