package com.book_goblin2.model;

public class ReadingLog {
    private int logId;
    private int userBookId;
    private long startDate; // Timestamp
    private Long endDate; // Nullable timestamp
    private Integer rating; // Nullable (1-5)
    private String notes;


    public ReadingLog(int userBookId, long startDate) {
        this.userBookId = userBookId;
        this.startDate = startDate;
    }


    public int getLogId() { return logId; }
    public void setLogId(int logId) { this.logId = logId; }

    public int getUserBookId() { return userBookId; }
    public void setUserBookId(int userBookId) { this.userBookId = userBookId; }

    public long getStartDate() { return startDate; }
    public void setStartDate(long startDate) { this.startDate = startDate; }

    public Long getEndDate() { return endDate; }
    public void setEndDate(Long endDate) { this.endDate = endDate; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}


