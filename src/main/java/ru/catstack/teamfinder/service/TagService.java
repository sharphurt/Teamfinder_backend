package ru.catstack.teamfinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.catstack.teamfinder.model.Tag;
import ru.catstack.teamfinder.repository.TagRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    public Optional<Tag> findById(long roleId) {
        return tagRepository.findById(roleId);
    }

    public Tag save(Tag tag) {
        return tagRepository.save(tag);
    }

}
