package com.ankush.poc.controller;


import com.ankush.poc.entity.ImageData;
import com.ankush.poc.response.ImageResponse;
import com.ankush.poc.response.RemoteImageDataResponse;
import com.ankush.poc.service.interfaces.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/create")
    public ResponseEntity<?> saveImage(@RequestParam MultipartFile file) throws IOException {
        String imgName = fileUploadService.saveImage(file);
        return ResponseEntity.status(HttpStatus.OK).body(ImageResponse.builder()
                        .imageName(imgName)
                        .message("Img saved successfully")
                .build());
    }

    @PostMapping("remote/create")
    public ResponseEntity<?> saveImageInLocation(@RequestParam MultipartFile file) throws IOException {
        String imgName = fileUploadService.saveImageInLocation(file);
        return ResponseEntity.status(HttpStatus.OK).body(ImageResponse.builder()
                .imageName(imgName)
                .message("Img saved successfully")
                .build());
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> getImageByName(@PathVariable String name) throws IOException {
        ImageData imageData = fileUploadService.getImageByName(name);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(imageData.getType()))
                .body(imageData.getFileData());
    }

    @GetMapping("remote/{name}")
    public ResponseEntity<?> getImageByNameFromLocation(@PathVariable String name) throws IOException {
        RemoteImageDataResponse imageByNameFromLocation = fileUploadService.getImageByNameFromLocation(name);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(imageByNameFromLocation.getType()))
                .body(imageByNameFromLocation.getFileData());
    }


}
