package com.book_goblin2.dao;

import com.book_goblin2.model.Tag;
import java.util.List;

public interface TagDao {
    List<Tag> getAllTags();
    Tag getTagById(int tagId);
    Tag createTag(Tag tag);
    boolean updateTag(Tag tag);
    boolean deleteTag(int tagId);
    Tag getTagByName(String name);
}