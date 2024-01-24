package com.zy;

import com.spire.xls.*;

public class ToPDF {


    public static void main(String[] args) {
        //加载Excel文档
        Workbook wb = new Workbook();
        wb.loadFromFile("src/main/resources/案件办理情况汇总表.xlsx");

        ConverterSetting converterSetting = new ConverterSetting();
        converterSetting.setSheetFitToPage(true);
        wb.setConverterSetting(converterSetting);//关键 自适应pdf
        //调用方法保存为PDF格式
        wb.saveToFile("src/main/resources/ToPDF.pdf", FileFormat.PDF);

    }
}
