package com.zy.excel.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.zy.excel.constant.ExcelType;
import com.zy.excel.listener.ExcelListener;
import lombok.SneakyThrows;

import java.io.*;
import java.util.*;

import com.zy.excel.constant.DataType;

/**
 * easyExcel文件生成、解析通用工具类
 */
public class EasyExcelUtil {
    /**
     * 从文件load数据至excel,文件列默认分隔符
     */
    private static final String EXPORT_DEFAULT_TXT2EXCEL_SPLIT = "\\&\\$\\%";
    /**
     * excel单元格内容输出至txt文件后的列分隔符
     */
    private static final String EXPORT_DEFAULT_EXCEL2TXT_SPLIT = "&$%";
    /**
     * 数据导出excel单sheet最大行数
     */
    private static Integer MAXIMUM_SIZE_OF_A_SINGLE_SHEET = null;
    /**
     * 正则,用于数据类型解析
     */
    private static final HashMap<String, String> dataPatterns = new HashMap<>();

    static {
        dataPatterns.put(DataType.BIGINT, "\\d+");
        dataPatterns.put(DataType.DECIMAL, "\\d+(\\.\\d+)?");
    }

    public EasyExcelUtil(Integer xlsxMaxSizeOfASheet) {
        MAXIMUM_SIZE_OF_A_SINGLE_SHEET = xlsxMaxSizeOfASheet <= 500000 ? xlsxMaxSizeOfASheet : 500000;
    }

    /**
     * 解析sheet内容并持久化
     *
     * @param source         文件路径
     * @param target         持久化文件路径
     * @param sheetIndex     解析当前表格的sheet页码(begin=0)
     * @param ignoreEmptyRow 是否忽略表格内的空行
     * @param hasHead        当前sheet内是否拥有表头
     * @return List<Map < String, Map < String, String>>> 返回表头信息,无表头则返回空 {field:{filedType:length}...}
     */
    public List<Map<String, Map<String, String>>> ReadExcel(String source, String target, int sheetIndex, boolean ignoreEmptyRow, boolean hasHead) {
        List<Map<String, Map<String, String>>> result = new LinkedList<>();
        List<String> headTitleList = new LinkedList<>();
        List<Map<String, String>> ctsList = new LinkedList<>();
        try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(target))) {
            ExcelListener excelListener = getExecListener(hasHead, headTitleList, ctsList, osw);
            EasyExcel.read(source, excelListener).ignoreEmptyRow(ignoreEmptyRow).sheet(sheetIndex).doRead();
            if (hasHead) {
                for (int i = 0; i < headTitleList.size(); i++) {
                    LinkedHashMap<String, Map<String, String>> fieldMap = new LinkedHashMap<>();
                    if (i < ctsList.size()) {
                        fieldMap.put(headTitleList.get(i), ctsList.get(i));
                    } else {
                        HashMap<String, String> bacMap = new HashMap<>();
                        bacMap.put("STRING", "8byte");
                        fieldMap.put("", bacMap);
                    }
                    result.add(fieldMap);
                }
            } else {
                for (Map<String, String> stringStringMap : ctsList) {
                    LinkedHashMap<String, Map<String, String>> fieldMap = new LinkedHashMap<>();
                    fieldMap.put("", stringStringMap);
                    result.add(fieldMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //TODO LOGERROR
        }
        return result;
    }


    /**
     * 获取sheet个数
     */
    public int getSheetNum(String fileName) {
        try {
            return EasyExcel.read(fileName).build().excelExecutor().sheetList().size();
        } catch (Exception e) {
            return 0;
        }

    }


    /**
     * 生成excel文件 仅支持 xlsx、csv
     *
     * @param tableName 表名称
     * @param source    数据文件路径
     * @param target    生成的文件路径
     * @param head      表头集合信息
     */
    public boolean writeExcel(String tableName, String source, String target, List<List<String>> head) {
        if (!ExcelType.equalsTo(target, ExcelType.EXPORT_XLSX.getIOType())) return false;

        ExcelWriter excelWriter = null;
        WriteSheet writeSheet = null;
        List<List<Object>> dataLists = null;
        int currentDataRow = 0, currentSheetNum = 1;
        String str = null;

        try (BufferedReader br = new BufferedReader(new FileReader(source))) {
            dataLists = ListUtils.newArrayList();
            excelWriter = EasyExcel.write(target).build();
            while ((str = br.readLine()) != null) {
                if (currentDataRow % MAXIMUM_SIZE_OF_A_SINGLE_SHEET == 0 && currentDataRow != 0 && !target.endsWith(ExcelType.EXPORT_CSV.getName())) {
                    if (currentSheetNum == 1) {
                        writeSheet = EasyExcel.writerSheet(0, tableName + currentSheetNum++).head(head).build();
                    } else {
                        writeSheet = EasyExcel.writerSheet(currentSheetNum - 1, tableName + currentSheetNum++).build();
                    }
                    excelWriter.write(dataLists, writeSheet);
                    dataLists.clear();
                }
                List<Object> lineFields = Arrays.asList(str.split(EXPORT_DEFAULT_TXT2EXCEL_SPLIT));
                dataLists.add(lineFields);
                currentDataRow++;
            }
            if (dataLists.size() > 0) {
                if (currentSheetNum == 1) {
                    writeSheet = EasyExcel.writerSheet(0, tableName + currentSheetNum).head(head).build();
                } else {
                    writeSheet = EasyExcel.writerSheet(currentSheetNum - 1, tableName + currentSheetNum).build();
                }
                excelWriter.write(dataLists, writeSheet);
                dataLists.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
            //TODO LOGGER ERROR
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
        return true;
    }


    /**
     * 通过监听器对象完成 数据持久化 和 数据类型自动识别 保存至集合
     *
     * @param hasHead       文件内是否拥有表头
     * @param headTitleList 表头集合[field,field, ... field]
     * @param ctsList       根据默认行数据识别数据类型 [{field:{filedType:length}}, ...]
     * @param osw           持久化文件流
     * @return 监听器对象
     */
    private ExcelListener getExecListener(boolean hasHead, List<String> headTitleList, List<Map<String, String>> ctsList, OutputStreamWriter osw) {
        return new ExcelListener(hasHead) {
            //简单的类型解析
            @Override
            protected void parseCellTypes() {
                for (final List<String> line : cachedDataList) {
                    for (int j = 0; j < line.size(); j++) {
                        final String cell = line.get(j);
                        Map<String, String> cellMap;
                        if (cellTypes.size() > j) {
                            cellMap = cellTypes.get(j);
                        } else {
                            cellMap = new HashMap<>();
                            cellTypes.add(cellMap);
                        }

                        if (cell == null) {
                            if (cellMap.size() <= 0) {
                                cellMap.put(DataType.NULL, "NULL");
                            }
                        } else if (cell.matches(dataPatterns.get(DataType.BIGINT))) {
                            if (cellMap.size() > 0) {
                                for (String s : cellMap.keySet()) {
                                    switch (s) {
                                        case DataType.STRING:
                                        case DataType.BOOLEAN:
                                            cellMap.clear();
                                            cellMap.put(DataType.STRING, "2g");
                                            break;
                                        case DataType.DECIMAL:
                                            cellMap.clear();
                                            cellMap.put(DataType.DECIMAL, "20, 10");
                                            break;
                                        case DataType.NULL:
                                            cellMap.clear();
                                            cellMap.put(DataType.BIGINT, "8byte");
                                            break;
                                    }
                                }
                            } else {
                                cellMap.put(DataType.BIGINT, "8byte");
                            }
                        } else if (cell.matches(dataPatterns.get(DataType.DECIMAL))) {
                            if (cellMap.size() > 0) {
                                for (String s : cellMap.keySet()) {
                                    switch (s) {
                                        case DataType.STRING:
                                        case DataType.BOOLEAN:
                                            cellMap.clear();
                                            cellMap.put(DataType.STRING, "2g");
                                            break;
                                        case DataType.BIGINT:
                                        case DataType.NULL:
                                            cellMap.clear();
                                            cellMap.put(DataType.DECIMAL, "20, 10");
                                            break;
                                    }
                                }
                            } else {
                                cellMap.put(DataType.DECIMAL, "20, 10");
                            }
                        } else if (cell.equals("false") || cell.equals("true")) {
                            if (cellMap.size() > 0) {
                                for (String s : cellMap.keySet()) {
                                    if (s.equals(DataType.STRING) || s.equals(DataType.BIGINT) || s.equals(DataType.DECIMAL)) {
                                        cellMap.clear();
                                        cellMap.put(DataType.STRING, "2g");
                                    } else if (s.equals(DataType.NULL)) {
                                        cellMap.clear();
                                        cellMap.put(DataType.BOOLEAN, "1byte");
                                    }
                                }
                            } else {
                                cellMap.put(DataType.BOOLEAN, "1byte");
                            }
                        } else {
                            cellMap.clear();
                            cellMap.put(DataType.STRING, "2g");
                        }
                    }
                }
                for (Map<String, String> cellType : cellTypes) {
                    if (cellType.get(DataType.NULL) != null) {
                        cellType.put(DataType.STRING, "8byte");
                    }
                }
                ctsList.addAll(cellTypes);
            }

            //数据持久化
            @SneakyThrows
            @Override
            protected void saveData(boolean isEnd) {
                if (hasHead) {
                    if (headTitleList.size() <= 0) {
                        headTitleList.addAll(headTitle);
                    }
                }
                final Iterator<List<String>> cachedDataIter = cachedDataList.iterator();
                while (cachedDataIter.hasNext()) {
                    outPutStream(cachedDataIter.next().iterator(), osw, isEnd && !cachedDataIter.hasNext());
                }
            }
        };
    }


    /**
     * 将集合内容输出至指定文件
     */
    private void outPutStream(Iterator<String> iterator, OutputStreamWriter osw, boolean isEnd) throws Exception {
        while (iterator.hasNext()) {
            final String next = iterator.next();
            osw.write(next != null ? next : "");
            if (iterator.hasNext()) {
                osw.write(EXPORT_DEFAULT_EXCEL2TXT_SPLIT);
            }
        }
        if (!isEnd) {
            osw.write("\r\n");
        }
    }
}

