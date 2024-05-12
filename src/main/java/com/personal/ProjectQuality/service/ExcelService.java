package com.personal.ProjectQuality.service;


import com.personal.ProjectQuality.model.Check;
import com.personal.ProjectQuality.model.Configuration;
import com.personal.ProjectQuality.model.FileData;
import com.personal.ProjectQuality.model.response.ClientRuleMappingResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    TenantRuleMappingService tenantRuleMappingService;

    public ResponseEntity<byte[]> generateConfigurationFile() {
        return generateConfigurationExcel();
    }


    public ResponseEntity<byte[]> generateRulesFile(String tenantCode) {
        return generateRulesExcel(tenantCode);
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

    private ResponseEntity<byte[]> generateRulesExcel(String tenantCode) {
        List<ClientRuleMappingResponse> clientRuleMappingResponseList = tenantRuleMappingService.getRuleMappingDetailsByTenantCode(tenantCode);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "rules.xls");
        HashMap<String, Set<String>> ruleMap = new HashMap<>();
        for (ClientRuleMappingResponse clientRuleMappingResponse : clientRuleMappingResponseList) {
            if (!ruleMap.containsKey(clientRuleMappingResponse.getRuleMasterName())) {
                Set ruleData = new HashSet();
                ruleData.add(clientRuleMappingResponse.getRuleName());
                ruleMap.put(clientRuleMappingResponse.getRuleMasterName(), ruleData);
            } else {
                Set ruleData = ruleMap.get(clientRuleMappingResponse.getRuleMasterName());
                ruleData.add(clientRuleMappingResponse.getRuleName());
                ruleMap.put(clientRuleMappingResponse.getRuleMasterName(), ruleData);
            }

        }
        try (Workbook workbook = new HSSFWorkbook()) {
            for (Map.Entry<String, Set<String>> entry : ruleMap.entrySet()) {
                Sheet sheet = workbook.createSheet(entry.getKey());
                Row headerRow = sheet.createRow(0);
                String[] columns = {"DATASOURCE", "TABLENAME", "FIELDNAME", "CHECKRULE"};
                for (int i = 0; i < columns.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columns[i]);
                }
                String[] rule = entry.getValue().toArray(new String[0]);
                for (int i = 0; i < 50; i++) {
                    Row row = sheet.getRow(i + 1);
                    if (row == null) {
                        row = sheet.createRow(i + 1);
                    }
                    DataValidationHelper validationHelper = sheet.getDataValidationHelper();
                    CellRangeAddressList addressList = new CellRangeAddressList(i + 1, i + 1, 3, 3);
                    DataValidationConstraint validationConstraint =
                            validationHelper.createExplicitListConstraint(rule);
                    DataValidation dataValidation = validationHelper.createValidation(validationConstraint, addressList);
                    sheet.addValidationData(dataValidation);
                }
            }
            workbook.write(outputStream);
            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public ResponseEntity<Resource> generateYaml(FileData path,String tenantCode) throws IOException {
        List<Configuration> configData = readConfigXlsFile(path.getConfigFilePath());
        List<Check> rulesData = readRuleXlsFile(path.getRuleFilePath());
        Set<String> filenameSet = new HashSet<>();
        List<ClientRuleMappingResponse> clientRuleMappingResponseList = tenantRuleMappingService.getRuleMappingDetailsByTenantCode(tenantCode);
        filenameSet.addAll(formQualityCheck(rulesData, configData,clientRuleMappingResponseList));
        zipFiles(filenameSet, "config.zip");
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

    private List<Check> readRuleXlsFile(String path) {
        try (Workbook workbook = WorkbookFactory.create(new File(path))) {
            String[] PiiList = {"FIRST_NAME", "LAST_NAME", "FULL_NAME", "UAE_NATIONAL_ID", "MOBILE_NUMBER", "WHATSAPP_NUMBER", "EMAIL"};
            Set<String> PiiSet = new HashSet<>();
            PiiSet.addAll(Arrays.asList(PiiList));
            String[] SensitiveList = {"PASSPORT_NUMBER", "DATE_OF_BIRTH", "PLACE_OF_BIRTH", "RELIGION"};
            Set<String> SesitiveSet = new HashSet<>();
            SesitiveSet.addAll(Arrays.asList(SensitiveList));
            String[] NonPiiList = {"NATIONALITY", "CITY", "COUNTRY", "COUNTRY_CODE", "AREA", "MAIN_AREA", "EMIRATE", "LONGITUDE", "LATITUDE"};
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
                Check check = new Check();
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
                            check.setCheckRule(getCellValueAsString(cell));
                            dataList.add(check);
                        }
                    }
                }
            }


            for (int rowIndex = 1; rowIndex <= sheet1.getLastRowNum(); rowIndex++) {
                Row row = sheet1.getRow(rowIndex);
                Check check = new Check();
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
                            check.setCheckRule(getCellValueAsString(cell));
                            dataList.add(check);
                        }
                    }
                }
            }

            for (int rowIndex = 1; rowIndex <= sheet3.getLastRowNum(); rowIndex++) {
                Row row = sheet3.getRow(rowIndex);
                Check check = new Check();
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
                            check.setCheckRule(getCellValueAsString(cell));
                            dataList.add(check);
                        }
                    }
                }
            }

            return dataList;
        } catch (IOException e) {
            throw new RuntimeException("Error reading XLS file: " + e.getMessage());
        }
    }

    private Set<String> formQualityCheck(List<Check> rulesData, List<Configuration> configurationList, List<ClientRuleMappingResponse> clientRuleMappingResponseList) throws IOException {
        Set<String> fileNames = new HashSet<>();
        Set<String> tableRulenameSet = new HashSet<>();
        BufferedWriter writer = null;
        String previousTable = "";
        Map<String,ClientRuleMappingResponse> clinetResponsemap = formResponseMap(clientRuleMappingResponseList);
        for (Configuration configuration : configurationList) {
            for (Check rule : rulesData) {
                String tableRuleName = rule.getTablename() + "_" + rule.getDataSource();
                if (!tableRulenameSet.contains(tableRuleName)) {
                    if (!previousTable.equals("") && !previousTable.equals(rule.getTablename() + "_" + rule.getDataSource())) {
                        writer.append("validator_" + previousTable.split("_")[0].toLowerCase() + ".save_expectation_suite(None,False)" + "\n");
                        writer.append("checkpoint = context.add_or_update_checkpoint(name=\"my_quickstart_checkpoint\", validator=" + "validator_" + previousTable.split("_")[0].toLowerCase() + ",)" + "\n");
                        writer.append("checkpoint_result = checkpoint.run()" + "\n");
                        writer.append("json_string = context.get_validation_result(" + "\"" + "expectation_suite_" + previousTable.split("_")[0].toLowerCase() + "\"" + ").to_json_dict()" + "\n");
                        writer.append("response = requests.post(url, json=json_string)" + "\n");
                        writer.append("print(response.text)" + "\n");
                        writer.flush();
                        fileNames.add(previousTable + "_quality.py");
                        writer.close();
                    }
                    tableRulenameSet.add(rule.getTablename() + "_" + rule.getDataSource());
                    writer = new BufferedWriter(new FileWriter(rule.getTablename() + "_" + rule.getDataSource() + "_quality.py", true));
                    writer.append("import great_expectations as gx" + "\n");
                    writer.append("import requests" + "\n");
                    writer.append("context = gx.get_context()" + "\n");
                    if (configuration.getType().equalsIgnoreCase("mysql")) {
                        writer.append("dataPlatform=\"mysql\"" + "\n");
                        writer.append("url = f'http://localhost:8084/api/quality/processValidationResult/{dataPlatform}'" + "\n");
                        writer.append("datasource = context.sources.add_sql(name=" +
                                "\"" + configuration.getDataSource() + "\""
                                + ", connection_string=" +
                                "\"mysql+pymysql://" + configuration.getUsername() + ":" + configuration.getPassword() + "@" + configuration.getHost() + ":" + configuration.getPort() + "/" + configuration.getDataSource() + "\"" +
                                ")" + "\n");
                    }
                    writer.append("table_asset_" + rule.getTablename().toLowerCase() + " = " +
                            "datasource.add_table_asset(name=" + "\"" + rule.getTablename().toLowerCase() + "_asset" + "\"" + ", table_name=" +
                            "\"" + rule.getTablename() + "\"" + ",schema_name=" + "\"" + configuration.getDataSource() + "\"" + ")" + "\n");
                    writer.append("data_asset_" + rule.getTablename().toLowerCase() + " = " +
                            "context.get_datasource(" + "\"" + configuration.getDataSource() + "\")" +
                            ".get_asset(" + "\"" + rule.getTablename().toLowerCase() + "_asset" + "\"" + ")" + "\n");
                    writer.append("batch_request_" + rule.getTablename().toLowerCase() + " = " +
                            "data_asset_" + rule.getTablename().toLowerCase() + ".build_batch_request()" + "\n");
                    writer.append("context.add_or_update_expectation_suite(" + "\"" + "expectation_suite_" + rule.getTablename().toLowerCase() + "\"" + ")" + "\n");
                    writer.append("validator_" + rule.getTablename().toLowerCase() + " = context.get_validator(batch_request=" +
                            "batch_request_" + rule.getTablename().toLowerCase() + ",expectation_suite_name=" +
                            "\"" + "expectation_suite_" + rule.getTablename().toLowerCase() + "\"" + ",)" + "\n");
                }
                if (tableRuleName.equals(rule.getTablename() + "_" + rule.getDataSource())) {
                    previousTable = rule.getTablename() + "_" + rule.getDataSource();
                    if(clinetResponsemap.containsKey(rule.getCheckRule())){
                        ClientRuleMappingResponse clientRuleMappingResponse = clinetResponsemap.get(rule.getCheckRule());
                        if(clientRuleMappingResponse.getNullCheck()){
                            writer.append("validator_" + rule.getTablename().toLowerCase() +
                                    ".expect_column_values_to_not_be_null(column=" +
                                    "\"" + rule.getFieldName().toLowerCase() + "\")" + "\n");
                        }
                        if(clientRuleMappingResponse.getMaxValue()>0){
                            writer.append("validator_" + rule.getTablename().toLowerCase() +
                                    ".expect_column_value_lengths_to_be_between(" +
                                    "\"" + rule.getFieldName().toLowerCase() + "\"" +
                                    ", min_value="+clientRuleMappingResponse.getMinValue()+", max_value="
                                    +clientRuleMappingResponse.getMaxValue()+")" +
                                    "\n");
                        }
                        if(Objects.nonNull(clientRuleMappingResponse.getRegex()) &&
                                !clientRuleMappingResponse.getRegex().equals("")){
                            writer.append("validator_" + rule.getTablename().toLowerCase() +
                                    ".expect_column_values_to_match_regex(" +
                                    "\"" + rule.getFieldName().toLowerCase() + "\"," +
                                    "\"" + clientRuleMappingResponse.getRegex() + "\"" +
                                    ")" +
                                    "\n");
                        }
                    }
                }
            }
        }
        if (!previousTable.equals("")) {
            writer.append("validator_" + previousTable.split("_")[0].toLowerCase() + ".save_expectation_suite(None,False)" + "\n");
            writer.append("checkpoint = context.add_or_update_checkpoint(name=\"my_quickstart_checkpoint\", validator=" + "validator_" + previousTable.split("_")[0].toLowerCase() + ",)" + "\n");
            writer.append("checkpoint_result = checkpoint.run()" + "\n");
            writer.append("json_string = context.get_validation_result(" + "\"" + "expectation_suite_" + previousTable.split("_")[0].toLowerCase() + "\"" + ").to_json_dict()" + "\n");
            writer.append("response = requests.post(url, json=json_string)" + "\n");
            writer.append("print(response.text)" + "\n");
            writer.flush();
            fileNames.add(previousTable + "_quality.py");
            writer.close();
        }
        return fileNames;
    }

    private Map<String, ClientRuleMappingResponse> formResponseMap(List<ClientRuleMappingResponse> clientRuleMappingResponseList) {
        Map<String,ClientRuleMappingResponse> clientRuleMappingResponseMap = new HashMap<>();
        for(ClientRuleMappingResponse clientRuleMappingResponse : clientRuleMappingResponseList){
            clientRuleMappingResponseMap.put(clientRuleMappingResponse.getRuleName(),clientRuleMappingResponse);
        }
        return clientRuleMappingResponseMap;
    }
}
