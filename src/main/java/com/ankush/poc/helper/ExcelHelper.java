package com.ankush.poc.helper;

import com.ankush.poc.constants.CommonConstants;
import com.ankush.poc.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class ExcelHelper {
    public static Boolean isDocumentValid(MultipartFile file){
        if(file.getContentType().equals(CommonConstants.EXCEL_CONTENT_TYPE))
            return true;
        else
            return false;
    }

    public static List<Product> convertExcelToListOfProduct(InputStream is) {
        List<Product> list = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            XSSFSheet sheet = workbook.getSheet("Sheet1");

            int rowNumber = 0;
            Iterator<Row> iterator = sheet.iterator();

            while (iterator.hasNext()) {
                Row row = iterator.next();
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cells = row.iterator();
                int cid = 0;
                Product p = new Product();

                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    Integer data = null;
                    switch (cid) {
                        case 0:
                            p.setId((int) cell.getNumericCellValue());
                            break;
                        case 1:
                            p.setName(cell.getStringCellValue());
                            break;
                        case 2:
                            p.setDescription(cell.getStringCellValue());
                            break;
                        case 3:
                            p.setPrice(cell.getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    cid++;
                }
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    private static Integer checkCellTypeAndGetData(Cell cell) {
        Integer data = 0;
        if(cell.getCellType() == CellType.STRING) {
            data = Integer.valueOf(cell.getStringCellValue());
        } else if(cell.getCellType() == CellType.NUMERIC) {
            data = Integer.valueOf(String.valueOf(cell.getNumericCellValue()));
        }
        return data;
    }

    public static String[] HEADERS = {"id","name","description","price"};


    public static ByteArrayInputStream convertDataIntoExcel(List<Product> productList) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //
        XSSFSheet sheet = workbook.createSheet("product_info");
        XSSFRow row = sheet.createRow(0);
        for(int i=0;i<HEADERS.length;i++){
            XSSFCell cell = row.createCell(i);
             cell.setCellValue(HEADERS[i]);
        }

        log.info("Row created : {}",row);
        int rowCount = 1;
        for(Product p : productList){
            XSSFRow sheetRow = sheet.createRow(rowCount);
            sheetRow.createCell(0).setCellValue(p.getId());
            sheetRow.createCell(1).setCellValue(p.getName());
            sheetRow.createCell(2).setCellValue(p.getDescription());
            sheetRow.createCell(3).setCellValue(p.getPrice());
            rowCount++;
        }
         workbook.write(out);
        return new ByteArrayInputStream(out.toByteArray());
    }

}
