package com.ankush.poc.service;

import com.ankush.poc.entity.ImageData;
import com.ankush.poc.entity.RemoteImageData;
import com.ankush.poc.repository.FileUploadRepository;
import com.ankush.poc.repository.RemoteImageUploadRepository;
import com.ankush.poc.response.RemoteImageDataResponse;
import com.ankush.poc.service.interfaces.FileUploadService;
import com.ankush.poc.utils.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    private FileUploadRepository fileUploadRepository;

    @Autowired
    private RemoteImageUploadRepository remoteImageUploadRepository;

    @Value("${image.path}")
    private String path;
    private static final String IMG_PATH = "D:/images/";

    @Override
    public String saveImage(MultipartFile file) throws IOException {
        ImageData imageData = ImageData.builder()
                .fileData(ImageUtils.compressImage(file.getBytes()))
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .build();
        ImageData savedImage = fileUploadRepository.save(imageData);
        if(savedImage !=null)
        return file.getOriginalFilename();
    else
        throw new RuntimeException();
    }

    @Override
    public ImageData getImageByName(String name) {
        Optional<ImageData> imageData = fileUploadRepository.findByName(name);
        if(imageData.isPresent()){
            imageData.get().setFileData(ImageUtils.decompressImage(imageData.get().getFileData()));
            return imageData.get();
        }
        else
            throw new RuntimeException();
    }

    @Override
    public String saveImageInLocation(MultipartFile file) throws IOException {
        String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
        String fileName = UUID.randomUUID() + fileType;
        String compeleteImgPath = path.concat(fileName);
        log.info("Complete Img Path : {}",compeleteImgPath);
        // copying file
        file.transferTo(new File(compeleteImgPath));
        RemoteImageData remoteImageData = RemoteImageData.builder()
                .name(fileName)
                .type(file.getContentType())
                .imgAddress(compeleteImgPath)
                .build();
        RemoteImageData savedImg = remoteImageUploadRepository.save(remoteImageData);
        if(savedImg !=null)
            return fileName;
        else
            throw new RuntimeException();
    }

    @Override
    public RemoteImageDataResponse getImageByNameFromLocation(String name) throws IOException {
        Optional<RemoteImageData> remoteImageData = remoteImageUploadRepository.findByName(name);
         if(remoteImageData.isPresent()){
             String filePath = remoteImageData.get().getImgAddress();
             byte[] bytes = Files.readAllBytes(new File(filePath).toPath());
           RemoteImageDataResponse response = RemoteImageDataResponse.builder()
                   .fileData(bytes)
                   .name(remoteImageData.get().getName())
                   .type(remoteImageData.get().getType())
                   .build();
         return response;
         }
         else
             throw new RuntimeException();
    }

}
