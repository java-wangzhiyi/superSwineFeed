package com.zy.excel.constant;

import com.alibaba.excel.util.StringUtils;

/**
 * 数据导出、导出支持的excel文件类型
 */
public enum ExcelType {

    EXPORT_XLSX("xlsx", "export"),
    EXPORT_CSV("csv", "export"),
    IMPORT_XLSX("xlsx", "import"),
    IMPORT_XLS("xls", "import"),
    IMPORT_CSV("csv", "import");

    String name;
    String IOType;

    ExcelType(String name, String IOType) {
        this.name = name;
        this.IOType = IOType;
    }

    /**
     * 文件类型是否支持
     * @param fileName filename
     * @param IOType export/import
     */
    public static boolean equalsTo(String fileName, String IOType) {
        if (!StringUtils.isNotBlank(fileName) || !StringUtils.isNotBlank(IOType)) return false;
        for (ExcelType value : ExcelType.values()) {
            if (IOType.equals(value.IOType) && fileName.endsWith(value.name)) {
                return true;
            }
        }
        //TODO LOG ERROR
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIOType() {
        return IOType;
    }

    public void setIOType(String IOType) {
        this.IOType = IOType;
    }
}