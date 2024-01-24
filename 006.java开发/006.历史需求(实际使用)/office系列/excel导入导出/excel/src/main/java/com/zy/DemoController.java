package com.zy;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson2.JSON;
import com.zy.excel.util.EasyExcelUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DemoController {
    public static void main(String[] args) {
        String fileName = "E:\\demand\\excel\\hello.xlsx";
        String fileName2 = "E:\\demand\\excel\\hello.txt";
        String fileName3 = "E:\\demand\\excel\\hello2.csv";


//        System.out.println(JSON.toJSONString(EasyExcelUtil.ReadExcel(fileName, fileName2, 0, false, true)));
//        System.out.println(EasyExcelUtil.getSheetNum(fileName));
        final EasyExcelUtil easyExcelUtil = new EasyExcelUtil(4);
        easyExcelUtil.writeExcel("aabbcc", fileName2, fileName3, head());

    }

    private static List<List<String>> head() {
        List<List<String>> list = ListUtils.newArrayList();
        List<String> head0 = ListUtils.newArrayList();
        head0.add("字符串" + System.currentTimeMillis());
        List<String> head1 = ListUtils.newArrayList();
        head1.add("数字" + System.currentTimeMillis());
        List<String> head2 = ListUtils.newArrayList();
        head2.add("日期" + System.currentTimeMillis());
        list.add(head0);
        list.add(head1);
        list.add(head2);
        return list;
    }

    private static List<List<Object>> dataList() {
        List<List<Object>> list = ListUtils.newArrayList();
        for (int i = 0; i < 10; i++) {
            List<Object> data = ListUtils.newArrayList();
            data.add("字符串" + i);
            data.add(0.56);
            data.add(new Date());
            list.add(data);
        }
        return list;
    }
}