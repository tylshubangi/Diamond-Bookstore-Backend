package com.bookstore.onlinebookstore.service;

import com.bookstore.onlinebookstore.entity.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class InvoiceService {

    public byte[] generateInvoice(Order order, Payment payment) {

        try (PDDocument document = new PDDocument()) {

            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream content = new PDPageContentStream(document, page);

            // ✅ LOAD UNICODE FONT (FIXES ₹ ISSUE)
            InputStream fontStream = getClass().getResourceAsStream("/fonts/NotoSans-Regular.ttf");

            if (fontStream == null) {
                throw new RuntimeException("Invoice font not found");
            }

            PDType0Font font = PDType0Font.load(document, fontStream);

            // ================= HEADER =================
            content.setFont(font, 16);
            content.beginText();
            content.newLineAtOffset(50, 750);
            content.showText("ONLINE BOOKSTORE INVOICE");
            content.endText();

            // ================= DETAILS =================
            content.setFont(font, 12);
            int y = 720;

            y = writeLine(content, font, "Order ID: " + order.getId(), y);
            y = writeLine(content, font,
                    "Customer: " + order.getUser().getName(), y);
            y = writeLine(content, font,
                    "Payment Method: " + payment.getPaymentMethod(), y);
            y = writeLine(content, font,
                    "Payment Status: " + payment.getStatus(), y);

            y -= 20;
            y = writeLine(content, font,
                    "----------------------------------------", y);

            // ================= ITEMS =================
            for (OrderItem item : order.getItems()) {

                String line = item.getBook().getTitle()
                        + " | Qty: " + item.getQuantity()
                        + " | ₹" + item.getPrice();

                y = writeLine(content, font, line, y);
            }

            y -= 10;
            writeLine(content, font,
                    "TOTAL: ₹" + order.getTotalAmount(), y);

            content.close();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate invoice PDF", e);
        }
    }

    private int writeLine(
            PDPageContentStream content,
            PDType0Font font,
            String text,
            int y) throws IOException {

        content.beginText();
        content.setFont(font, 12);
        content.newLineAtOffset(50, y);
        content.showText(text);
        content.endText();

        return y - 15;
    }
}
