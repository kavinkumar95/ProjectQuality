package com.personal.ProjectQuality.service;


import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.personal.ProjectQuality.entity.QualityIndexColumnInfo;
import com.personal.ProjectQuality.entity.QualityIndexFailureInfo;
import com.personal.ProjectQuality.entity.QualityIndexTableInfo;
import com.personal.ProjectQuality.repository.QualityIndexColumnRepository;
import com.personal.ProjectQuality.repository.QualityIndexFailureInfoRepository;
import com.personal.ProjectQuality.repository.QualityIndexTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PdfService {

    @Autowired
    QualityIndexTableRepository qualityIndexTableRepository;
    @Autowired
    QualityIndexColumnRepository qualityIndexColumnRepository;
    @Autowired
    QualityIndexFailureInfoRepository qualityIndexFailureInfoRepository;

    public ResponseEntity<byte[]> generatePdf(String datasource, String schema, String datasetName) throws IOException {
        QualityIndexTableInfo qualityIndexTableInfo =findLatestRecord(datasource,schema,datasetName);
        if(Objects.isNull(qualityIndexTableInfo)){
            throw new RuntimeException("Record not found for given Table");
        }else{
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                 PdfWriter pdfWriter = new PdfWriter(outputStream);
                 PdfDocument pdfDoc = new PdfDocument(pdfWriter)) {

                try (Document document = new Document(pdfDoc, PageSize.A4)) {
                    // Set background color
                   /* PdfCanvas pdfCanvas = new PdfCanvas(pdfDoc.addNewPage());
                    pdfCanvas.rectangle(0, 0, PageSize.A4.getWidth(), PageSize.A4.getHeight()).fill();
                    pdfCanvas.setColor(com.itextpdf.kernel.colors.Color.makeColor());*/

                    // Create a div to hold the image and title
                    Div div = new Div()
                            .setBackgroundColor(new DeviceRgb(Color.WHITE))
                            .setMargin(0) // Set margins to zero
                            .setBorder(null); // Remove border;

                    // Image
                    try (InputStream in = Files.newInputStream(Paths.get("/Users/kavmuthusamy/Documents/Personal/ProjectQuality/src/main/resources/RGB_EN.png"))) {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        byte[] bytes = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = in.read(bytes)) != -1) {
                            byteArrayOutputStream.write(bytes, 0, bytesRead);
                        }
                        byte[] imageData = byteArrayOutputStream.toByteArray();
                        Image image = new Image(ImageDataFactory.create(imageData))
                                .scaleToFit(100, 30) // Adjust size as needed
                                .setMarginLeft(450); // Adjust right margin to create space between image and title
                        div.add(image);
                    }

                    // Title
                    Paragraph title = new Paragraph("Data Quality Report")
                            .setFontColor(new DeviceRgb(Color.BLACK))
                            .setBold()
                            .setFontSize(24)
                            .setTextAlignment(TextAlignment.CENTER)
                            .setMarginTop(-30); // Adjust top margin to align with image
                    div.add(title);

                    Paragraph subtitle = new Paragraph("Table Information : ")
                            .setFontColor(new DeviceRgb(Color.BLACK))
                            .setFontSize(18)
                            .setTextAlignment(TextAlignment.LEFT)
                            .setMarginLeft(20) // Adjust left margin to align with title
                            .setMarginTop(30); // Adjust top margin to create space below title
                    div.add(subtitle);




                    String tableInfo = "Table Name  : " + datasetName + "\n" +
                            "Schema Name : " + schema + "\n" +
                            "Datasource  : " + datasource;

                    Paragraph tableName = new Paragraph(tableInfo)
                            .setFontColor(new DeviceRgb(Color.BLACK))
                            .setFontSize(12)
                            .setTextAlignment(TextAlignment.LEFT)
                            .setMarginLeft(30) // Adjust left margin to align with title
                            .setMarginTop(5); // Adjust top margin to create space below title
                    div.add(tableName);



                    Paragraph statistics = new Paragraph("Table Statistics : ")
                            .setFontColor(new DeviceRgb(Color.BLACK))
                            .setFontSize(18)
                            .setTextAlignment(TextAlignment.LEFT)
                            .setMarginLeft(20) // Adjust left margin to align with title
                            .setMarginTop(30); // Adjust top margin to create space below title

                    div.add(statistics);


                    // Create a table with 4 rows and 2 columns
                    Table table = new Table(2); // 2 columns with equal width
                    table.setWidth(UnitValue.createPercentValue(100));

                    // Add cells to the table
                    for (int i = 0; i <= 4; i++) {
                        Cell cell1 = new Cell();
                        Cell cell2 = new Cell();
                        cell1.setWidth(UnitValue.createPercentValue(80));
                        cell2.setWidth(UnitValue.createPercentValue(20));
                        if (i == 0) {
                            cell1.add(new Paragraph("Status"+"\t"));
                            DeviceRgb fontColor= qualityIndexTableInfo.getStatus()?new DeviceRgb(Color.GREEN):new DeviceRgb(Color.RED);
                            String text= qualityIndexTableInfo.getStatus()?"SUCCESS":"FAILED";
                            cell2.add(new Paragraph(text).setFontColor(fontColor));
                        }else if (i == 1) {
                            cell1.add(new Paragraph("Evaluated Expectations"+"\t"));
                            cell2.add(new Paragraph(String.valueOf(qualityIndexTableInfo.getTotalValidationCount())));
                        } else if (i == 2) {
                            cell1.add(new Paragraph("Successful Expectations"+"\t"));
                            cell2.add(new Paragraph(String.valueOf(qualityIndexTableInfo.getSuccessfullValidationCount())));
                        } else if (i == 3) {
                            cell1.add(new Paragraph("Unsuccessful Expectations"+"\t"));
                            cell2.add(new Paragraph(String.valueOf(qualityIndexTableInfo.getFailedValidationCount())));
                        } else {
                            cell1.add(new Paragraph("Success Percent"+"\t"));
                            cell2.add(new Paragraph(String.valueOf(qualityIndexTableInfo.getValidationResult())));
                        }
                        table.addCell(cell1);
                        table.addCell(cell2);
                    }

                    // Set dynamic spacing according to the internal text
                    table.setMarginTop(10);
                    table.setMarginLeft(20);
                    table.setMarginBottom(10);
                    div.add(table);

                    List<QualityIndexColumnInfo> qualityIndexColumnInfoList= qualityIndexColumnRepository.findByJobId(qualityIndexTableInfo.getJobId());
                    if(qualityIndexColumnInfoList.size()>0){
                        Paragraph expectation = new Paragraph("Column Report : ")
                                .setFontColor(new DeviceRgb(Color.BLACK))
                                .setFontSize(18)
                                .setTextAlignment(TextAlignment.LEFT)
                                .setMarginLeft(20) // Adjust left margin to align with title
                                .setMarginTop(30); // Adjust top margin to create space below title

                        div.add(expectation);

                        // Create a table with 4 rows and 2 columns
                        Table ColumnTable = new Table(3); // 2 columns with equal width
                        ColumnTable.setWidth(UnitValue.createPercentValue(100));

                        // Add cells to the table
                        for (int i = 0; i <= qualityIndexColumnInfoList.size()-1; i++) {
                            QualityIndexColumnInfo qualityIndexColumnInfo = qualityIndexColumnInfoList.get(i);
                            Cell cell1 = new Cell();
                            Cell cell2 = new Cell();
                            Cell cell3 = new Cell();
                            cell1.setWidth(UnitValue.createPercentValue(15));
                            cell2.setWidth(UnitValue.createPercentValue(70));
                            cell3.setWidth(UnitValue.createPercentValue(15));
                            if (i == 0) {
                                cell1.add(new Paragraph("Column Name"+"\t"));
                                cell2.add(new Paragraph("Validation Rule"+"\t"));
                                cell3.add(new Paragraph("Status"+"\t"));
                            } else {
                                DeviceRgb fontColor= qualityIndexColumnInfo.getValidationStatus()?new DeviceRgb(Color.GREEN):new DeviceRgb(Color.RED);
                                String text= qualityIndexColumnInfo.getValidationStatus()?"SUCCESS":"FAILED";
                                cell1.add(new Paragraph(qualityIndexColumnInfo.getColumnName()));
                                cell2.add(new Paragraph(qualityIndexColumnInfo.getExpectationType()));
                                cell3.add(new Paragraph(text).setFontColor(fontColor));
                            }
                            ColumnTable.addCell(cell1);
                            ColumnTable.addCell(cell2);
                            ColumnTable.addCell(cell3);
                        }

                        // Set dynamic spacing according to the internal text
                        ColumnTable.setMarginTop(10);
                        ColumnTable.setMarginLeft(20);
                        ColumnTable.setMarginBottom(10);
                        div.add(ColumnTable);
                    }
                    List<QualityIndexFailureInfo> qualityIndexFailureInfoList= qualityIndexFailureInfoRepository.findByJobId(qualityIndexTableInfo.getJobId());
                    if(qualityIndexFailureInfoList.size()>0){
                        Paragraph failureReport = new Paragraph("Failure Report : ")
                                .setFontColor(new DeviceRgb(Color.BLACK))
                                .setFontSize(18)
                                .setTextAlignment(TextAlignment.LEFT)
                                .setMarginLeft(20) // Adjust left margin to align with title
                                .setMarginTop(30); // Adjust top margin to create space below title

                        div.add(failureReport);

                        // Create a table with 4 rows and 2 columns
                        Table FailureTable = new Table(3); // 2 columns with equal width
                        FailureTable.setWidth(UnitValue.createPercentValue(100));

                        // Add cells to the table
                        for (int i = 0; i <= qualityIndexFailureInfoList.size()-1; i++) {
                            QualityIndexFailureInfo qualityIndexFailureInfo = qualityIndexFailureInfoList.get(i);
                            Cell cell1 = new Cell();
                            Cell cell2 = new Cell();
                            Cell cell3 = new Cell();
                            cell1.setWidth(UnitValue.createPercentValue(15));
                            cell2.setWidth(UnitValue.createPercentValue(70));
                            cell3.setWidth(UnitValue.createPercentValue(15));
                            if (i == 0) {
                                cell1.add(new Paragraph("Column Name"+"\t"));
                                cell2.add(new Paragraph("Failed Data"+"\t"));
                                cell3.add(new Paragraph("Count"+"\t"));
                            } else {
                                cell1.add(new Paragraph(qualityIndexFailureInfo.getColumnName()));
                                cell2.add(new Paragraph(qualityIndexFailureInfo.getFailedData()));
                                cell3.add(new Paragraph(String.valueOf(qualityIndexFailureInfo.getFailedCount())));
                            }
                            FailureTable.addCell(cell1);
                            FailureTable.addCell(cell2);
                            FailureTable.addCell(cell3);
                        }

                        // Set dynamic spacing according to the internal text
                        FailureTable.setMarginTop(10);
                        FailureTable.setMarginLeft(20);
                        FailureTable.setMarginBottom(10);
                        div.add(FailureTable);
                    }
                    document.add(div);
                }
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+datasetName +"_Table_DQI_Report.pdf")
                        .body(outputStream.toByteArray());
            }
        }
    }
    public QualityIndexTableInfo findLatestRecord(String datasource, String schemaName, String tableName) {
        // Define ExampleMatcher to match properties

        // Find the record with the latest createdTimestamp
        return qualityIndexTableRepository.findByTableName(datasource, schemaName,tableName);
    }
}
