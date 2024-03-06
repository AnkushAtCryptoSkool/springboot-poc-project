package com.ankush.poc.controller;

import com.ankush.poc.entity.Product;
import com.ankush.poc.helper.ExcelHelper;
import com.ankush.poc.response.APIResponse;
import com.ankush.poc.response.ImageResponse;
import com.ankush.poc.service.interfaces.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<?> saveImage(@RequestParam MultipartFile file) throws IOException {
        if(!ExcelHelper.isDocumentValid(file))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is not a type of XLSS");

        return ResponseEntity.status(HttpStatus.OK).body(productService.saveExcelToDatabase(file));
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllProducts() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getAllProducts());
    }

    @GetMapping("/sorted/{field}")
    public ResponseEntity<?> getAllProductSortedByField(@PathVariable String field) throws IOException {
        List<Product> allProductSortedByField = productService.getAllProductSortedByField(field);
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.builder()
                        .response(allProductSortedByField)
                        .recordCount(allProductSortedByField.size())
                .build());
    }

    @GetMapping("/page/no/{offset}/size/{pageSize}")
    public ResponseEntity<?> getAllPaginatedProducts(@PathVariable int offset, @PathVariable int pageSize) throws IOException {
        Page<Product> allProductInPaginatedFormat = productService.getAllProductInPaginatedFormat(offset, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.builder()
                .response(allProductInPaginatedFormat.get())
                .recordCount((int) allProductInPaginatedFormat.getTotalElements())
                .build());
    }

    @GetMapping("/page/no/{offset}/size/{pageSize}/sort/{item}")
    public ResponseEntity<?> getAllPaginatedProductsInSortedManner(@PathVariable int offset, @PathVariable int pageSize,@PathVariable String item) throws IOException {
        Page<Product> allProductInPaginatedFormat = productService.getAllProductInPaginatedFormatSortedByFiled(offset, pageSize,item);
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.builder()
                .response(allProductInPaginatedFormat.get())
                .recordCount((int) allProductInPaginatedFormat.getTotalElements())
                .build());
    }

    @GetMapping("/excel")
    public ResponseEntity<?> getAllProductsFromExcel() throws IOException {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=products_" + currentDateTime + ".xlsx";
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(headerKey,headerValue)
                .body(productService.getExcelOFAllProducts());
    }

    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<?> generatePdfFromDBData() throws IOException {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=products_" + currentDateTime + ".pdf";
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_PDF)
                .header(headerKey,headerValue)
                .body(productService.generatePdfFromDBData());
    }

}
