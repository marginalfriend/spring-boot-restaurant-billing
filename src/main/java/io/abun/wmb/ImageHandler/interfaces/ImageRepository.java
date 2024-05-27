package io.abun.wmb.ImageHandler.interfaces;

import io.abun.wmb.ImageHandler.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {
}
