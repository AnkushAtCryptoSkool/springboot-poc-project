package com.ankush.poc.repository;

import com.ankush.poc.entity.RemoteImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface RemoteImageUploadRepository extends JpaRepository<RemoteImageData,Long> {

    Optional<RemoteImageData> findByName(String name);
}
