package com.book_goblin2.dao;

import com.book_goblin2.model.BookTag;
import java.util.List;

public interface BookTagDao {
    List<BookTag> getTagsForBook(int bookId);
    BookTag addTagToBook(BookTag bookTag);
    boolean removeTagFromBook(int bookTagId);
    boolean doesBookTagExist(int bookId, int tagId);
}