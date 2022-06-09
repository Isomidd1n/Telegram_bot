package com.company.controller;

import com.company.container.ComponentContainer;
import com.company.db.Database;
import com.company.model.Customer;
import com.company.model.Operations;
import com.company.model.Order;
import com.company.service.ProductService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static com.company.service.OrderService.loadOrderList;

public class FileController {
    public static File getCustomersPDF() {

        File file = new File(ComponentContainer.PATH + "files/db/customers.pdf");

        try (PdfWriter pdfWriter = new PdfWriter(file)) {

            loadCustomerList();

            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.addNewPage();

            Document document = new Document(pdfDocument);

            Paragraph paragraph = new Paragraph();
            paragraph.add(paragraph);

            float[] widthColumns = {50f , 200f , 200f , 200f , 200f};

            int number = 0;

            Table table = new Table(widthColumns);

            table.addCell("№");
            table.addCell("Id");
            table.addCell("Ismi");
            table.addCell("Familyasi");
            table.addCell("Telefon raqami");


            for (Customer customer : Database.customerList) {

                number++;


                table.addCell(String.valueOf(number));
                table.addCell(customer.getId());
                table.addCell(customer.getFirstName() == null ? "" : customer.getFirstName());
                table.addCell(customer.getLastName() == null ? "" : customer.getLastName());
                table.addCell(customer.getPhoneNumber() == null ? "" : customer.getPhoneNumber());
            }
            document.add(table);

            document.close();
            pdfDocument.close();
            pdfWriter.close();
            return file;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void loadCustomerList() {
    }

    public static File getOperationsPDF() {

        File file = new File(ComponentContainer.PATH + "files/db/operations.pdf");

        try (PdfWriter pdfWriter = new PdfWriter(file)) {

            ProductService.operationsList();

            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.addNewPage();

            Document document = new Document(pdfDocument);

            Paragraph paragraph = new Paragraph();
            paragraph.add(paragraph);

            float[] widthColumns = {50f , 50f , 200f , 200f , 200f , 200f};

            int number = 0;

            Table table = new Table(widthColumns);
            table.addCell("№");
            table.addCell("Id");
            table.addCell("Category_id");
            table.addCell("Product_id");
            table.addCell("Updated_at");
            table.addCell("Operation_name");



            for (Operations operation : Database.operationList) {

                number++;

                table.addCell(String.valueOf(number));
                table.addCell(String.valueOf(operation.getId()));
                table.addCell(String.valueOf(operation.getCategory_id()));
                table.addCell(String.valueOf(operation.getProduct_id()));
                table.addCell(String.valueOf(operation.getUpdated_at()));
                table.addCell(operation.getOperation_name());

            }
            document.add(table);
            document.close();
            pdfDocument.close();
            pdfWriter.close();
            return file;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static File generateOrderPDF() {

        File file = new File(ComponentContainer.PATH + "files/db/orders.pdf");

        try (PdfWriter pdfWriter = new PdfWriter(file)) {

            loadOrderList();

            PdfDocument pdfDocument = new PdfDocument(pdfWriter);

            pdfDocument.addNewPage();

            Document document = new Document(pdfDocument);

            Paragraph paragraph = new Paragraph();

            paragraph.add("Buyurtmalar tarixi");

            paragraph.add(paragraph);

            float[] widthColumns = {20f, 60f, 60f, 60f, 200f, 80f, 150f};

            int number = 0;

            Table table = new Table(widthColumns);
            table.addCell("№");
            table.addCell("Ismi");
            table.addCell("Familyasi");
            table.addCell("Raqami");
            table.addCell("Buyurtmasi");
            table.addCell("Buyurtma Summasi");
            table.addCell("Buyurtma Sanasi");


            for (Order order : Database.orderList) {

                number++;

                table.addCell(String.valueOf(number));
                table.addCell(order.getFirst_name());
                table.addCell(order.getLast_name() == null ? "" : order.getLast_name());
                table.addCell(order.getPhone_number() == null ? "" : order.getPhone_number());
                table.addCell(order.getUser_order() == null ? "" : order.getUser_order());
                table.addCell(String.valueOf(order.getTotalprice() == null ? "" : order.getTotalprice()));
                table.addCell(String.valueOf(order.getLocalDateTime() == null ? "" : order.getLocalDateTime()));
            }
            document.add(table);

            document.close();
            pdfDocument.close();
            pdfWriter.close();
            return file;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
