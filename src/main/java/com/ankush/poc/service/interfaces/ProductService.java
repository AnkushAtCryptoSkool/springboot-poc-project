package com.ankush.poc.service.interfaces;

import com.ankush.poc.entity.Product;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    String saveExcelToDatabase(MultipartFile file) throws IOException;

    List<Product> getAllProducts();

    List<Product> getAllProductSortedByField(String item);

    Page<Product> getAllProductInPaginatedFormat(int offset, int pageSize);

    Page<Product> getAllProductInPaginatedFormatSortedByFiled(int offset, int pageSize, String field);

    InputStreamResource getExcelOFAllProducts() throws IOException;

    InputStreamResource generatePdfFromDBData();
}
