package ru.catstack.auth.service;

import org.springframework.stereotype.Service;
import ru.catstack.auth.model.ImageModel;
import ru.catstack.auth.repository.ImageRepository;

import java.util.Optional;

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public ImageModel save(ImageModel image) {
        return imageRepository.save(image);
    }

    public Optional<ImageModel> findByUserId(Long id) {
        return imageRepository.findByUserId(id);
    }

    public void deleteByUserId(Long id) {
        imageRepository.deleteByUserId(id);
    }
}
