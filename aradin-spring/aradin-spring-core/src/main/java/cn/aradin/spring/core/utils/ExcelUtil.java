package cn.aradin.spring.core.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 * @author liudaac
 * 
 */
public class ExcelUtil {

	public static List<List<String>> parseSheet(Integer sheetindex, Integer startrow, Integer startcel,
			String filename) {
		List<List<String>> content = new ArrayList<List<String>>();
		try {
			@SuppressWarnings("resource")
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(filename);
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(sheetindex);
			if (xssfSheet == null) {
				return null;
			}
			for (int rowNum = startrow; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow == null) {
					continue;
				}
				// 循环列Cell
				List<String> cellcontent = new ArrayList<String>();
				for (int cellNum = startcel; cellNum <= xssfRow.getLastCellNum(); cellNum++) {
					XSSFCell xssfCell = xssfRow.getCell(cellNum);
					if (xssfCell == null) {
						cellcontent.add("");
						continue;
					}
					cellcontent.add(getValue(xssfCell));
					System.out.print("   " + getValue(xssfCell));
				}
				content.add(cellcontent);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return content;
	}

	public static List<List<String>> parseSheet(Integer sheetindex, Integer startrow, String filename) {
		List<List<String>> content = new ArrayList<List<String>>();
		try {
			@SuppressWarnings("resource")
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(filename);
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(sheetindex);
			if (xssfSheet == null) {
				return null;
			}
			for (int rowNum = startrow; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow == null) {
					continue;
				}
				// 循环列Cell
				List<String> cellcontent = new ArrayList<String>();
				for (int cellNum = 0; cellNum <= xssfRow.getLastCellNum(); cellNum++) {
					XSSFCell xssfCell = xssfRow.getCell(cellNum);
					if (xssfCell == null) {
						cellcontent.add("");
						continue;
					}
					cellcontent.add(getValue(xssfCell));
					System.out.print("   " + getValue(xssfCell));
				}
				content.add(cellcontent);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return content;
	}

	public static List<List<String>> parseSheet(Integer sheetindex, Integer startrow, InputStream ins) {
		List<List<String>> content = new ArrayList<List<String>>();
		try {
			@SuppressWarnings("resource")
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(ins);
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(sheetindex);
			if (xssfSheet == null) {
				return null;
			}
			for (int rowNum = startrow; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow == null) {
					continue;
				}
				// 循环列Cell
				List<String> cellcontent = new ArrayList<String>();
				for (int cellNum = 0; cellNum <= xssfRow.getLastCellNum(); cellNum++) {
					XSSFCell xssfCell = xssfRow.getCell(cellNum);
					if (xssfCell == null) {
						cellcontent.add("");
						continue;
					}
					cellcontent.add(getValue(xssfCell));
					System.out.print("   " + getValue(xssfCell));
				}
				content.add(cellcontent);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return content;
	}

	@SuppressWarnings("static-access")
	private static String getValue(XSSFCell xssfCell) {
		if (xssfCell.getCellType() == xssfCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(xssfCell.getBooleanCellValue());
		} else if (xssfCell.getCellType() == xssfCell.CELL_TYPE_NUMERIC) {
			return String.valueOf((long) (xssfCell.getNumericCellValue()));
		} else {
			return String.valueOf(xssfCell.getStringCellValue());
		}
	}

	/**
	 * 创建sheet到file中
	 * 
	 * @param filepath
	 * @param sheetIndex
	 * @param cellnames
	 * @param arrRows
	 * @throws Exception
	 */
	public static void createSheet(String filepath, String sheetname, String[] cellnames, List<List<Object>> arrRows)
			throws Exception {
		FileOutputStream fout = null;
		try {
			File file = new File(filepath);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			fout = new FileOutputStream(file);
			createSheet(fout, sheetname, cellnames, arrRows);
		} catch (Exception e) {
			throw new Exception("保存excel失败");
		} finally {
			if (fout != null) {
				try {
					fout.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("resource")
	public static void createSheet(OutputStream outputStream, String sheetname, String[] cellnames,
			List<List<Object>> arrRows) throws Exception {
		// 第一步，创建一个webbook，对应一个Excel文件
		XSSFWorkbook xb = new XSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		XSSFSheet sheet = xb.createSheet(sheetname);
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		XSSFRow row = sheet.createRow(0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		XSSFCellStyle style = xb.createCellStyle();
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);

		if (cellnames == null || cellnames.length <= 0) {
			throw new Exception("表头信息为空");
		}
		for (int i = 0; i < cellnames.length; i++) {
			XSSFCell cell = row.createCell(i);
			cell.setCellValue(cellnames[i]);
			cell.setCellStyle(style);
		}
		// 第五步，写入实体数据
		if (arrRows != null && arrRows.size() > 0) {
			for (int i = 0; i < arrRows.size(); i++) {
				List<Object> rowdata = arrRows.get(i);
				row = sheet.createRow(i + 1);
				for (int j = 0; j < rowdata.size(); j++) {
					Object value = rowdata.get(j);
					if (value instanceof Double) {
						row.createCell(j).setCellValue((Double)rowdata.get(j));
					}else {
						row.createCell(j).setCellValue((String)rowdata.get(j));
					}
				}
			}
		}
		try {
			xb.write(outputStream);
			outputStream.flush();
		} catch (Exception e) {
			throw new Exception("保存excel失败");
		}
	}
}
