package com.ankush.poc.service.interfaces;

import com.ankush.poc.entity.ImageData;
import com.ankush.poc.response.RemoteImageDataResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadService {
   public String saveImage(MultipartFile file) throws IOException;
   public ImageData getImageByName(String name);

   String saveImageInLocation(MultipartFile file) throws IOException;

   RemoteImageDataResponse getImageByNameFromLocation(String name) throws IOException;
}
