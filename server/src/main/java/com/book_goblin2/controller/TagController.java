package com.book_goblin2.controller;

import com.book_goblin2.dao.TagDao;
import com.book_goblin2.model.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@CrossOrigin
public class TagController {

    private final TagDao tagDao;

    public TagController(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @GetMapping
    public List<Tag> getAllTags() {
        return tagDao.getAllTags();
    }

    @GetMapping("/{id}")
    public Tag getTagById(@PathVariable int id) {
        Tag tag = tagDao.getTagById(id);
        if (tag == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found");
        }
        return tag;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Tag createTag(@RequestBody Tag tag) {
        return tagDao.createTag(tag);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Tag updateTag(@PathVariable int id, @RequestBody Tag tag) {
        tag.setTagId(id);
        if (!tagDao.updateTag(tag)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found");
        }
        return tag;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteTag(@PathVariable int id) {
        if (!tagDao.deleteTag(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found");
        }
    }
}
