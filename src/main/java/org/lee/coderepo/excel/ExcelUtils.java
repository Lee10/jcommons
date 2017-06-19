package org.lee.coderepo.excel;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lee on 2017/2/13.
 * excel 操作类，依赖poi
 */
public class ExcelUtils {

	private final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0");
	private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 读取excel文件数据，并转换为List集合
	 * @param filepath 文件路径
	 * @return
	 * @throws ExcelException
	 */
	public final static List<List<Object>> readToList(String filepath) throws ExcelException {

		Workbook workbook = buildWordbook(filepath);

		if (workbook != null) {
			Sheet sheet = workbook.getSheetAt(0);

			List<List<Object>> rowList = new ArrayList<List<Object>>();
			int rowNum = sheet.getLastRowNum();
			for (int i = 0; i <= rowNum; i++) {
				Row row = sheet.getRow(i);
				if (row != null && row.getLastCellNum() > 0) {
					List<Object> cellList = new ArrayList<Object>();
					for (int j = 0; j < row.getLastCellNum(); j++) {
						cellList.add(getCellValue(row.getCell(j)));
					}
					rowList.add(cellList);
				}
			}
			try {
				workbook.close();
			} catch (IOException e) {
				throw new ExcelException(e.getMessage());
			}
			return rowList;
		}
		return Collections.emptyList();
	}

	/**
	 * 读取excel文件数据，并转换为map集合
	 * @param filepath 文件路径
	 * @return
	 * @throws ExcelException
	 */
	public final static List<Map<String, Object>> readToMap(String filepath) throws ExcelException {

		Workbook workbook = buildWordbook(filepath);

		if (workbook != null) {
			Sheet sheet = workbook.getSheetAt(0);

			List<Map<String, Object>> rowList = new ArrayList<Map<String, Object>>();
			int rowNum = sheet.getLastRowNum();
			if (rowNum > 0) {

				List<String> titleList = new ArrayList<String>();
				Row titleRow = sheet.getRow(0);
				for (int i = 0; i < titleRow.getLastCellNum(); i++) {
					titleList.add(titleRow.getCell(i).getStringCellValue());
				}

				for (int i = 1; i <= rowNum; i++) {
					Row row = sheet.getRow(i);
					int cellNum = row.getLastCellNum();
					if (row != null && cellNum > 0) {
						Map<String, Object> cellMap = new LinkedHashMap<String, Object>();
						for (int j = 0; j < cellNum; j++) {
							cellMap.put(titleList.get(j), getCellValue(row.getCell(j)));
						}
						rowList.add(cellMap);
					}
				}
			}
			try {
				workbook.close();
			} catch (IOException e) {
				throw new ExcelException(e.getMessage());
			}
			return rowList;
		}
		return Collections.emptyList();
	}

	/**
	 * 将map集合导出excel表格
	 * @param filepath 导出路径
	 * @param dataList 数据集合
	 * @return
	 */
	public final static boolean write(String filepath, List<Map<String, Object>> dataList) {

		Workbook workbook = null;
		if(StringUtils.endsWith(filepath, ".xls")) workbook = new HSSFWorkbook();
		else if(StringUtils.endsWith(filepath, ".xlsx")) workbook = new SXSSFWorkbook();

		if(workbook != null) {

			Sheet sheet = workbook.createSheet();

			if (CollectionUtils.isNotEmpty(dataList)) {

				//创建表头
				Row row = sheet.createRow(0);
				Cell cell = null;
				Map<String, Object> tmpMap = dataList.get(0);
				int titleSize = tmpMap.size();
				List<String> titleList = new ArrayList<>(tmpMap.keySet());
				for (int i = 0; i < titleSize; i++) {
					cell = row.createCell(i);
					cell.setCellValue(titleList.get(i));
				}

				//填充数据
				int dataSize = dataList.size();
				for (int i = 0; i < dataSize; i++) {
					row = sheet.createRow(i + 1);
					tmpMap = dataList.get(i);
					for (int j = 0; j < tmpMap.size(); j++) {
						cell = row.createCell(j);
						cell.setCellValue(String.valueOf(tmpMap.get(titleList.get(j))));
					}
				}
			}

			if (!new File(filepath).exists()) new File(filepath).getParentFile().mkdirs();

			//导出excel
			try {
				FileOutputStream outputStream = new FileOutputStream(filepath);
				workbook.write(outputStream);
				outputStream.close();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 将对象<T>集合导出为excel表格
	 * @param filepath 导出路径
	 * @param titleMap 标题字典，key：对象属性名称；value：对象属性编码
	 * @param dataList 对象数据集合
	 * @param <T> 对象类型
	 * @return boolean
	 */
	public final static <T> boolean write(String filepath, LinkedHashMap<String, String> titleMap, List<T> dataList) throws ExcelException {

		Workbook workbook = null;
		if(StringUtils.endsWith(filepath, ".xls")) workbook = new HSSFWorkbook();
		else if(StringUtils.endsWith(filepath, ".xlsx")) workbook = new SXSSFWorkbook();

		if(workbook != null) {

			Sheet sheet = workbook.createSheet();

			if (MapUtils.isNotEmpty(titleMap) && CollectionUtils.isNotEmpty(dataList)) {

				//创建表头
				Row row = sheet.createRow(0);
				Cell cell = null;
				int index = 0;
				for (Object key : titleMap.keySet()) {
					cell = row.createCell(index);
					cell.setCellValue(String.valueOf(key));
					index++;
				}

				//填充数据
				index = 0;
				try {
					for (T t : dataList) {
						row = sheet.createRow(index + 1);
						Field field = null;
						int j = 0;
						for (Object key : titleMap.keySet()) {
							field = t.getClass().getDeclaredField(String.valueOf(titleMap.get(key)));
							field.setAccessible(true);
							cell = row.createCell(j);
							if (field.getType().getCanonicalName() == "java.util.Date")
								cell.setCellValue(DATE_FORMAT.format(field.get(t)));
							else cell.setCellValue(String.valueOf(field.get(t)));
							j++;
						}
						index++;
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new ExcelException("input data error");
				}
			}

			if (!new File(filepath).exists()) new File(filepath).getParentFile().mkdirs();

			//导出excel
			try {
				FileOutputStream outputStream = new FileOutputStream(filepath);
				workbook.write(outputStream);
				outputStream.close();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 读取excel文件，并构建Wordbook
	 * @param filepath 文件路径
	 * @return
	 * @throws ExcelException
	 */
	private final static Workbook buildWordbook(String filepath) throws ExcelException {
		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(new FileInputStream(new File(filepath)));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExcelException("Wordbook build error");
		}
		return workbook;
	}

	/**
	 * 根据cell数据类型获取对应类型的值
	 * @param cell
	 * @return
	 */
	private final static Object getCellValue(Cell cell) {

		if (Cell.CELL_TYPE_STRING == cell.getCellType()) return cell.getStringCellValue();
		else if (Cell.CELL_TYPE_BOOLEAN == cell.getCellType()) return cell.getBooleanCellValue();
		else if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
			return DECIMAL_FORMAT.format(cell.getNumericCellValue());
		} else {
			cell.setCellType(Cell.CELL_TYPE_STRING);
			return cell.getStringCellValue();
		}
	}

}
