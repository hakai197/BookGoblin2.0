package com.book_goblin2.dao;

import com.book_goblin2.model.UserBook;
import java.util.List;

public interface UserBookDao {
    List<UserBook> getUserBooks(int userId);
    UserBook addUserBook(UserBook userBook);
    boolean updateUserBook(UserBook userBook);
    boolean removeUserBook(int userBookId);
    UserBook getUserBookById(int userBookId);
    boolean doesUserBookExist(int userId, int bookId);
}