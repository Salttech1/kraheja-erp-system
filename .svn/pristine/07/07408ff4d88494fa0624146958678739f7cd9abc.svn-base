package kraheja.commons.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ExcelUtils {
	
	INSTANCE;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	public File export(String sqlQuery, String dbURl, String dbUsername, String dbPassword) {
		try (Connection connection = DriverManager.getConnection(dbURl, dbUsername, dbPassword)) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sqlQuery);

//			if(!result.isBeforeFirst()) 
//				return null;
			
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Sheet 1");

			writeHeaderLine(result, sheet);

			writeDataLines(result, workbook, sheet);

			File excelFile = File.createTempFile("KRAHJEA", CommonConstraints.INSTANCE.XLS_EXTENSION);
			LOGGER.info("TempFile :: {}", excelFile);
			
			FileOutputStream outputStream = new FileOutputStream(excelFile);
			workbook.write(outputStream);
			workbook.close();
			statement.close();
			
			return excelFile;
			
		} catch (SQLException e) {
			System.out.println("Datababse error:");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("File IO error:");
			e.printStackTrace();
		}
		return null;
	}

	private void writeHeaderLine(ResultSet result, HSSFSheet sheet) throws SQLException {
		// write header line containing column names
		ResultSetMetaData metaData = result.getMetaData();
		int numberOfColumns = metaData.getColumnCount();

		Row headerRow = sheet.createRow(0);

		// exclude the first column which is the ID field
		for (int i = 1; i <= numberOfColumns; i++) {
			String columnName = metaData.getColumnName(i);
			System.out.println(columnName);
			Cell headerCell = headerRow.createCell(i - 1);
			headerCell.setCellValue(columnName);
		}
	}

	private void writeDataLines(ResultSet result, HSSFWorkbook workbook, HSSFSheet sheet) throws SQLException {
		ResultSetMetaData metaData = result.getMetaData();
		int numberOfColumns = metaData.getColumnCount();
		CellStyle style = workbook.createCellStyle();
		DataFormat dataFormat = workbook.createDataFormat();
		int rowCount = 1;

		while (result.next()) {
			Row row = sheet.createRow(rowCount++);

			for (int i = 1; i <= numberOfColumns; i++) {
				Object valueObject = result.getObject(i);

				Cell cell = row.createCell(i - 1);

				if (valueObject instanceof Boolean)
					cell.setCellValue((Boolean) valueObject);
				else if (valueObject instanceof Double)
					cell.setCellValue((double) valueObject);
				else if (valueObject instanceof Float)
					cell.setCellValue((float) valueObject);
				else if (valueObject instanceof BigDecimal)
					cell.setCellValue(((BigDecimal) valueObject).doubleValue());
				else if (valueObject instanceof Date) {
					cell.setCellValue((Date) valueObject);
					formatDateCell(workbook, cell);
				} else {
					// Setting the cell type to be a text cell
					/*
					 * The code style.setDataFormat(dataFormat.getFormat("@")); sets the data format of the cell style to "@".
					 * In Excel, the "@" symbol is used as a placeholder for text. When a cell is formatted with "@", 
					 * any value entered into that cell will be treated as text, even if it looks like a number or a date.
					 */
					style.setDataFormat(dataFormat.getFormat("@"));
					cell.setCellStyle(style);
					cell.setCellValue((String) valueObject);
				}
			}

		}
	}

	private void formatDateCell(HSSFWorkbook workbook, Cell cell) {
		CellStyle cellStyle = workbook.createCellStyle();
		CreationHelper creationHelper = workbook.getCreationHelper();
		cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));
		cell.setCellStyle(cellStyle);
	}
}
