package com.ankush.poc.helper;

import com.ankush.poc.entity.Product;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class PdfHelper {

    public static ByteArrayInputStream converListOfProductToPdf(List<Product> products) {

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document,out);
            document.open();
            // making header
            Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE,49);
            Paragraph paragraph = new Paragraph("Product Data",fontHeader);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            document.add(new Chunk(Chunk.NEWLINE));

            PdfPTable table = new PdfPTable(4);
            // Add PDF Table Header ->
            Stream.of("ID", "'NAME","DESCRIPTION","PRICE").forEach(headerTitle -> {
                PdfPCell header = new PdfPCell();
                Font headFont = FontFactory.getFont(FontFactory.TIMES_BOLD);
                header.setBackgroundColor(Color.CYAN);
                header.setHorizontalAlignment(Element.ALIGN_CENTER);
                header.setBorderWidth(2);
                header.setPhrase(new Phrase(headerTitle, headFont));
                table.addCell(header);
            });

            for (Product product : products) {
                PdfPCell idCell = new PdfPCell(new Phrase(product.getId().toString()));
                commonCellCharactersitcis(idCell);
                table.addCell(idCell);

                PdfPCell name = new PdfPCell(new Phrase(product.getName()));
                commonCellCharactersitcis(name);
                table.addCell(name);

                PdfPCell desc = new PdfPCell(new Phrase(product.getDescription()));
                commonCellCharactersitcis(desc);
                table.addCell(desc);

                PdfPCell price = new PdfPCell(new Phrase(String.valueOf(product.getPrice())));
                commonCellCharactersitcis(price);
                table.addCell(price);
            }
            document.add(table);

        } catch (Exception e) {
            log.info("Exception occuered in converListOfProductToPdf : {} ",e.getMessage());
            throw new RuntimeException(e);
        } finally {
            document.close();
        }
       return new ByteArrayInputStream(out.toByteArray());

    }

    private static void commonCellCharactersitcis(PdfPCell idCell) {
        idCell.setPaddingLeft(4);
        idCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        idCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    }
}
