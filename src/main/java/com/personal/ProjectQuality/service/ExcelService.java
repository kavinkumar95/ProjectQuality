package com.personal.ProjectQuality.service;


import com.personal.ProjectQuality.model.Check;
import com.personal.ProjectQuality.model.Configuration;
import com.personal.ProjectQuality.model.FileData;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ExcelService {

    public ResponseEntity<byte[]> generateConfigurationFile(){
        return generateConfigurationExcel();
    }


    public ResponseEntity<byte[]> generateRulesFile(){
        return generateRulesExcel();
    }



    private ResponseEntity<byte[]> generateConfigurationExcel() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "config.xls");
        try (Workbook workbook = new HSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Configuration");
            Row headerRow = sheet.createRow(0);
            String[] columns = {"DATASOURCE", "TYPE", "HOST", "PORT", "USERNAME", "PASSWORD", "DATABASE", "SCHEMA"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }
            workbook.write(outputStream);
            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private ResponseEntity<byte[]> generateRulesExcel() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "rules.xls");
        try (Workbook workbook = new HSSFWorkbook()) {
            Sheet sheet1 = workbook.createSheet("PII");
            Row headerRow = sheet1.createRow(0);
            Sheet sheet2 = workbook.createSheet("SENSITIVE_PII");
            Row headerRow1 = sheet2.createRow(0);
            Sheet sheet3 = workbook.createSheet("NON_PII");
            Row headerRow2 = sheet3.createRow(0);
            String[] columns = {"DATASOURCE", "TABLENAME", "FIELDNAME", "CHECKRULE"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow1.createCell(i);
                cell.setCellValue(columns[i]);
            }
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow2.createCell(i);
                cell.setCellValue(columns[i]);
            }
            String[] PiiList = {"FIRST_NAME", "LAST_NAME", "FULL_NAME", "UAE_NATIONAL_ID", "MOBILE_NUMBER", "WHATSAPP_NUMBER", "EMAIL", "DATE_OF_BIRTH"};
            String[] SensitiveList = {"PASSPORT_NUMBER", "DATE_OF_BIRTH", "PLACE_OF_BIRTH", "RELIGION"};
            String[] NonPiiList = {"NATIONALITY", "CITY", "COUNTRY", "COUNTRY_CODE", "AREA", "MAIN_AREA", "EMIRATE", "LONGITUDE", "LATITUDE"};
            for (int i = 0; i < 50; i++) {
                Row row = sheet1.getRow(i + 1);
                if (row == null) {
                    row = sheet1.createRow(i + 1);
                }
                DataValidationHelper validationHelper = sheet1.getDataValidationHelper();
                CellRangeAddressList addressList = new CellRangeAddressList(i + 1, i + 1, 3, 3);
                DataValidationConstraint validationConstraint =
                        validationHelper.createExplicitListConstraint(PiiList);
                DataValidation dataValidation = validationHelper.createValidation(validationConstraint, addressList);
                sheet1.addValidationData(dataValidation);
            }
            for (int i = 0; i < 50; i++) {
                Row row = sheet2.getRow(i + 1);
                if (row == null) {
                    row = sheet2.createRow(i + 1);
                }
                DataValidationHelper validationHelper = sheet2.getDataValidationHelper();
                CellRangeAddressList addressList = new CellRangeAddressList(i + 1, i + 1, 3, 3);
                DataValidationConstraint validationConstraint =
                        validationHelper.createExplicitListConstraint(SensitiveList);
                DataValidation dataValidation = validationHelper.createValidation(validationConstraint, addressList);
                sheet2.addValidationData(dataValidation);
            }
            for (int i = 0; i < 50; i++) {
                Row row = sheet3.getRow(i + 1);
                if (row == null) {
                    row = sheet3.createRow(i + 1);
                }
                DataValidationHelper validationHelper = sheet3.getDataValidationHelper();
                CellRangeAddressList addressList = new CellRangeAddressList(i + 1, i + 1, 3, 3);
                DataValidationConstraint validationConstraint =
                        validationHelper.createExplicitListConstraint(NonPiiList);
                DataValidation dataValidation = validationHelper.createValidation(validationConstraint, addressList);
                sheet3.addValidationData(dataValidation);
            }
            workbook.write(outputStream);
            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);

        }catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public ResponseEntity<Resource> generateYaml(FileData path) throws IOException {
        List<Configuration> configData = readConfigXlsFile(path.getConfigFilePath());
        List<Check> rulesData = readRuleXlsFile(path.getRuleFilePath());
        Set<String> filenameSet = new HashSet<>();
        filenameSet.addAll(formConfigurationYaml(configData));
        filenameSet.addAll(formCheckYaml(rulesData));
        zipFiles(filenameSet,"config.zip");
        Path zipPath = Paths.get("config.zip");
        Resource resource = new FileSystemResource(zipPath.toFile());
        // Set up headers for download
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=config.zip");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        // Delete the individual files
        for (String fileName : filenameSet) {
            Path filePath = Paths.get(fileName);
            Files.deleteIfExists(filePath);
        }

        // Return the ResponseEntity with headers and content
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(zipPath.toFile().length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    private static void zipFiles(Set<String> fileNames, String zipFilePath) throws IOException {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(Paths.get(zipFilePath)))) {
            for (String fileName : fileNames) {
                Path filePath = Paths.get(fileName);
                ZipEntry zipEntry = new ZipEntry(filePath.getFileName().toString());
                zipOutputStream.putNextEntry(zipEntry);
                Files.copy(filePath, zipOutputStream);
            }
            zipOutputStream.closeEntry();
        }
    }

    private List<Configuration> readConfigXlsFile(String path) {
        List<Configuration> configurationList = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(new File(path))) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Configuration configuration = new Configuration();
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    Map<String, String> rowData = new HashMap<>();
                    for (int columnIndex = 0; columnIndex < row.getLastCellNum(); columnIndex++) {
                        Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        if (columnIndex == 0) {
                            configuration.setDataSource(getCellValueAsString(cell));
                        } else if (columnIndex == 1) {
                            configuration.setType(getCellValueAsString(cell));
                        } else if (columnIndex == 2) {
                            configuration.setHost(getCellValueAsString(cell));
                        } else if (columnIndex == 3) {
                            configuration.setPort(getCellValueAsString(cell));
                        } else if (columnIndex == 4) {
                            configuration.setUsername(getCellValueAsString(cell));
                        } else if (columnIndex == 5) {
                            configuration.setPassword(getCellValueAsString(cell));
                        } else if (columnIndex == 6) {
                            configuration.setDatabase(getCellValueAsString(cell));
                        } else if (columnIndex == 7) {
                            configuration.setSchema(getCellValueAsString(cell));
                        }
                    }
                    configurationList.add(configuration);
                }
            }
            return configurationList;
        } catch (IOException e) {
            throw new RuntimeException("Error reading XLS file: " + e.getMessage());
        }
    }

    private String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (String.valueOf(cell.getNumericCellValue()).contains(".")) {
                    return String.valueOf(cell.getNumericCellValue()).split("\\.")[0];
                }
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    private List<Check> readRuleXlsFile(String path){
        try (Workbook workbook = WorkbookFactory.create(new File(path))) {
            String[] PiiList = {"FIRST_NAME", "LAST_NAME", "FULL_NAME", "UAE_NATIONAL_ID","MOBILE_NUMBER","WHATSAPP_NUMBER","EMAIL","DATE_OF_BIRTH"};
            Set<String> PiiSet = new HashSet<>();
            PiiSet.addAll(Arrays.asList(PiiList));
            String[] SensitiveList = {"PASSPORT_NUMBER","DATE_OF_BIRTH","PLACE_OF_BIRTH","RELIGION"};
            Set<String> SesitiveSet = new HashSet<>();
            SesitiveSet.addAll(Arrays.asList(SensitiveList));
            String[] NonPiiList = {"NATIONALITY","CITY","COUNTRY","COUNTRY_CODE","AREA","MAIN_AREA","EMIRATE","LONGITUDE","LATITUDE"};
            Set<String> nonPiiSet = new HashSet<>();
            nonPiiSet.addAll(Arrays.asList(NonPiiList));

            Set<String> manditateChecks = new HashSet<>();

            Sheet sheet1 = workbook.getSheet("PII");
            Sheet sheet2 = workbook.getSheet("SENSITIVE_PII");
            Sheet sheet3 = workbook.getSheet("NON_PII"); // Assuming data is in the first sheet
            List<Check> dataList = new ArrayList<>();
            Set<String> checkRule = new HashSet<>();
            for (int rowIndex = 1; rowIndex <= sheet2.getLastRowNum(); rowIndex++) {
                Row row = sheet2.getRow(rowIndex);
                Check check  = new Check();
                if (row != null) {
                    for (int columnIndex = 0; columnIndex < row.getLastCellNum(); columnIndex++) {
                        Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        if (columnIndex == 0) {
                            check.setDataSource(getCellValueAsString(cell));
                        } else if (columnIndex == 1) {
                            check.setTablename(getCellValueAsString(cell));
                        } else if (columnIndex == 2) {
                            check.setFieldName(getCellValueAsString(cell));
                        } else if (columnIndex == 3) {
                            if(!manditateChecks.contains(getCellValueAsString(cell))){
                                manditateChecks.add(getCellValueAsString(cell));
                                check.setCheckRule(getCellValueAsString(cell));
                                dataList.add(check);
                            }
                        }
                    }
                }
            }


            for (int rowIndex = 1; rowIndex <= sheet1.getLastRowNum(); rowIndex++) {
                Row row = sheet1.getRow(rowIndex);
                Check check  = new Check();
                if (row != null) {
                    for (int columnIndex = 0; columnIndex < row.getLastCellNum(); columnIndex++) {
                        Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        if (columnIndex == 0) {
                            check.setDataSource(getCellValueAsString(cell));
                        } else if (columnIndex == 1) {
                            check.setTablename(getCellValueAsString(cell));
                        } else if (columnIndex == 2) {
                            check.setFieldName(getCellValueAsString(cell));
                        } else if (columnIndex == 3) {
                            if(!manditateChecks.contains(getCellValueAsString(cell))){
                                manditateChecks.add(getCellValueAsString(cell));
                                check.setCheckRule(getCellValueAsString(cell));
                                dataList.add(check);
                            }
                        }
                    }
                }
            }

            for (int rowIndex = 1; rowIndex <= sheet3.getLastRowNum(); rowIndex++) {
                Row row = sheet3.getRow(rowIndex);
                Check check  = new Check();
                if (row != null) {
                    for (int columnIndex = 0; columnIndex < row.getLastCellNum(); columnIndex++) {
                        Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        if (columnIndex == 0) {
                            check.setDataSource(getCellValueAsString(cell));
                        } else if (columnIndex == 1) {
                            check.setTablename(getCellValueAsString(cell));
                        } else if (columnIndex == 2) {
                            check.setFieldName(getCellValueAsString(cell));
                        } else if (columnIndex == 3) {
                            if(!manditateChecks.contains(getCellValueAsString(cell))){
                                manditateChecks.add(getCellValueAsString(cell));
                                check.setCheckRule(getCellValueAsString(cell));
                                dataList.add(check);
                            }
                        }
                    }
                }
            }

            return dataList;
        } catch (IOException e) {
            throw new RuntimeException("Error reading XLS file: " + e.getMessage());
        }
    }

    private Set<String> formCheckYaml(List<Check> rulesData) throws IOException {
        Set<String> fileNames = new HashSet<>();
        Set<String> dataSourceSet = new HashSet<>();
        for (Check rule : rulesData) {
            BufferedWriter writer = new BufferedWriter(new FileWriter( rule.getDataSource()+ "_rules.yml", true));
            String fileName = rule.getDataSource()+ "_rules.yml";
            if(!dataSourceSet.contains(rule.getDataSource())){
                dataSourceSet.add(rule.getDataSource());
                writer.append("checks for " + rule.getTablename() + ":" + "\n");
            }
            if(rule.getCheckRule().equalsIgnoreCase("FIRST_NAME") ||
                    rule.getCheckRule().equalsIgnoreCase("LAST_NAME")){
                writer.append("   " + "- invalid_percent(" + rule.getFieldName() +"):"+ "\n");
                writer.append("      " + "fail: when != 0%"+ "\n");
                writer.append("      " + "valid regex: ^[A-Za-z0-9 ]+$"+ "\n");
                writer.append("   " + "- max_length(" + rule.getFieldName() +"):"+ "\n");
                writer.append("      " + "fail: when >= 50"+ "\n");
            }
            if (rule.getCheckRule().equalsIgnoreCase("FULL_NAME")){
                writer.append("   " + "- invalid_percent(" + rule.getFieldName() +"):"+ "\n");
                writer.append("      " + "fail: when != 0%"+ "\n");
                writer.append("      " + "valid regex: ^[A-Za-z0-9 ]+$"+ "\n");
                writer.append("   " + "- max_length(" + rule.getFieldName() +"):"+ "\n");
                writer.append("      " + "fail: when >= 500"+ "\n");
            }
            if (rule.getCheckRule().equalsIgnoreCase("PASSPORT_NUMBER")){
                writer.append("   " + "- invalid_percent(" + rule.getFieldName() +"):"+ "\n");
                writer.append("      " + "fail: when != 0%"+ "\n");
                writer.append("      " + "valid regex: ^[A-Za-z0-9]+$|^([A-Za-z]{1,2}[0-9]{6,9})$"+ "\n");
            }
            if (rule.getCheckRule().equalsIgnoreCase("UAE_NATIONAL_ID")){
                writer.append("   " + "- invalid_percent(" + rule.getFieldName() +"):"+ "\n");
                writer.append("      " + "fail: when != 0%"+ "\n");
                writer.append("      " + "valid regex: ^[0-9]+$"+ "\n");
            }
            if (rule.getCheckRule().equalsIgnoreCase("UAE_NATIONAL_ID")){
                writer.append("   " + "- invalid_percent(" + rule.getFieldName() +"):"+ "\n");
                writer.append("      " + "fail: when != 0%"+ "\n");
                writer.append("      " + "valid regex: ^[0-9]+$"+ "\n");
            }
            if (rule.getCheckRule().equalsIgnoreCase("EMAIL")){
                writer.append("   " + "- invalid_percent(" + rule.getFieldName() +"):"+ "\n");
                writer.append("      " + "fail: when != 0%"+ "\n");
                writer.append("      " + "valid regex: ^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"+ "\n");
                writer.append("   " + "- max_length(" + rule.getFieldName() +"):"+ "\n");
                writer.append("      " + "fail: when >= 256"+ "\n");
            }
            if (rule.getCheckRule().equalsIgnoreCase("DATE_OF_BIRTH")){
                writer.append("   " + "- invalid_percent(" + rule.getFieldName() +"):"+ "\n");
                writer.append("      " + "fail: when != 0%"+ "\n");
                writer.append("      " + "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$"+ "\n");
                writer.append("   " + "- max_length(" + rule.getFieldName() +"):"+ "\n");
                writer.append("      " + "fail: when >= 11"+ "\n");
            }
            if (rule.getCheckRule().equalsIgnoreCase("PLACE_OF_BIRTH")){
                writer.append("   " + "- max_length(" + rule.getFieldName() +"):"+ "\n");
                writer.append("      " + "fail: when >= 50"+ "\n");
            }
            if (rule.getCheckRule().equalsIgnoreCase("RELIGION")){
                writer.append("   " + "- max_length(" + rule.getFieldName() +"):"+ "\n");
                writer.append("      " + "fail: when >= 50"+ "\n");
            }
            if (rule.getCheckRule().equalsIgnoreCase("LONGITUDE")){
                writer.append("   " + "- invalid_percent(" + rule.getFieldName() +"):"+ "\n");
                writer.append("      " + "fail: when != 0%"+ "\n");
                writer.append("      " + "^(-?((180(\\.0{1,10})?)|((1[0-7]\\d)|(\\d{1,2}))(\\.\\d{1,10})?))$"+ "\n");
                writer.append("   " + "- max_length(" + rule.getFieldName() +"):"+ "\n");
                writer.append("      " + "fail: when >= 50"+ "\n");
            }
            if (rule.getCheckRule().equalsIgnoreCase("LATITUDE")){
                writer.append("   " + "- invalid_percent(" + rule.getFieldName() +"):"+ "\n");
                writer.append("      " + "fail: when != 0%"+ "\n");
                writer.append("      " + "^(-?((90(\\.0{1,10})?)|((\\d{1,2})|([1-8]\\d))(\\.\\d{1,10})?))$"+ "\n");
                writer.append("   " + "- max_length(" + rule.getFieldName() +"):"+ "\n");
                writer.append("      " + "fail: when >= 50"+ "\n");
                writer.flush();
            }
            fileNames.add(fileName);
            writer.close();
        }
        return fileNames;
    }

    private static Set<String> formConfigurationYaml(List<Configuration> configData) throws IOException {
        Set<String> fileNames = new HashSet<>();
        for (Configuration configValues : configData) {
            BufferedWriter writer = new BufferedWriter(new FileWriter(configValues.getDataSource() +"_config" + ".yml",true));
            String fileName = configValues.getDataSource()+"_config" + ".yml";
            writer.append("soda_cloud:"+"\n");
            writer.append("   host: cloud.soda.io"+"\n");
            writer.append("   api_key_id: 4099c544-1dfe-4ba9-86fa-b5830cd67f97"+"\n");
            writer.append("   api_key_secret: bgTGxArEAi2RNJKIqZq1D2SmTZLxPTd_4_Moh32GAAlL6JcyQCeFVQ"+"\n");
            writer.append("data_source " + configValues.getDataSource() + ":" + "\n");
            writer.append("   " + "type: " + configValues.getType() + "\n");
            writer.append("   connection:" + "\n");
            writer.append("      " + "host: " + configValues.getHost() + "\n");
            writer.append("      " + "port: " + configValues.getPort() + "\n");
            writer.append("      " + "username: " + configValues.getUsername() + "\n");
            writer.append("      " + "password: " + configValues.getPassword() + "\n");
            writer.append("      " + "database: " + configValues.getDatabase() + "\n");
            writer.append("      " + "schema: " + configValues.getSchema() + "\n");
            writer.flush();
            writer.close();
            fileNames.add(fileName);
        }

        return fileNames;
    }
}
