package com.ankush.poc.repository;

import com.ankush.poc.entity.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileUploadRepository extends JpaRepository<ImageData,Long> {
    Optional<ImageData> findByName(String name);;
}
