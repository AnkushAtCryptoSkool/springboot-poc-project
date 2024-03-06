package com.ankush.poc.service;

import com.ankush.poc.entity.Product;
import com.ankush.poc.helper.ExcelHelper;
import com.ankush.poc.helper.PdfHelper;
import com.ankush.poc.repository.ProductRepository;
import com.ankush.poc.service.interfaces.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

        @PostConstruct
    public void initDB() {
            productRepository.deleteAll();
        List<Product> products = IntStream.rangeClosed(1, 200)
                .mapToObj(i -> new Product(i,"product" + i,"product description" + i , (double) new Random().nextInt(100)))
                .collect(Collectors.toList());
        productRepository.saveAll(products);
    }
    @Override
    public String saveExcelToDatabase(MultipartFile file) throws IOException {
        try {
            List<Product> productList = ExcelHelper.convertExcelToListOfProduct(file.getInputStream());
            log.info("Product List : {}",productList);
            List<Product> products = productRepository.saveAll(productList);
            return "Data saved to DB successfully";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getAllProductSortedByField(String field) {
        return productRepository.findAll(Sort.by(Sort.Direction.ASC,field));
    }

    @Override
    public Page<Product> getAllProductInPaginatedFormat(int offset, int pageSize) {
        Page<Product> pages = productRepository.findAll(PageRequest.of(offset,pageSize));
        return pages;
    }

    @Override
    public Page<Product> getAllProductInPaginatedFormatSortedByFiled(int offset,int pageSize,String field) {
        Page<Product> pages = productRepository.findAll(PageRequest.of(offset,pageSize).withSort(Sort.Direction.ASC,field));
        return pages;
    }

    @Override
    public InputStreamResource getExcelOFAllProducts() throws IOException {
        List<Product> allProducts = getAllProducts();
        if (!allProducts.isEmpty()) {
            ByteArrayInputStream byteArrayInputStream = ExcelHelper.convertDataIntoExcel(allProducts);
            return new InputStreamResource(byteArrayInputStream);
        } else
            throw new RuntimeException("Empty data");
    }

    @Override
    public InputStreamResource generatePdfFromDBData() {
        List<Product> allProducts = getAllProducts();
        if (!allProducts.isEmpty()) {
            ByteArrayInputStream byteArrayInputStream = PdfHelper.converListOfProductToPdf(allProducts);
            return new InputStreamResource(byteArrayInputStream);
        } else
            throw new RuntimeException("Empty data");

    }

}
