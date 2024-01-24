package com.zy.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;

import java.util.*;

/**
 * easyExcel 监听器
 */
public abstract class ExcelListener extends AnalysisEventListener<Map<Integer, String>> {
    /**
     * 每隔1000条存储数据，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 2;
    /**
     * 缓存的Excel数据
     */
    protected List<List<String>> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    /**
     * 表头信息
     */
    protected final List<String> headTitle = new LinkedList<>();
    /**
     * 该数据表是否拥有表头
     */
    private boolean hasHead;
    /**
     * 预定义列类型解析精度(要解析多少行)
     */
    private static final int PARSE_COUNT = 500;
    /**
     * 预定义列类型解析
     */
    protected List<Map<String, String>> cellTypes = new LinkedList<>();
    /**
     * 遍历行计数器
     */
    private Integer currentLineNum = 0;

    public ExcelListener(boolean hasHead) {
        this.hasHead = hasHead;
    }

    /**
     * 单行数据解析完成后都将会回调该函数<br/>
     * 1、存储数据至缓存(cachedDataList)<br/>
     * 2、数据达到BATCH_COUNT,持久化一次防止OOM<br/>
     * 3、持久化完成后清理缓存(cachedDataList)
     * @param data  行数据<br/>
     */
    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        currentLineNum++;
        cachedDataList.add(converter(data));
        if (currentLineNum == PARSE_COUNT){
            parseCellTypes();
        }
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData(false);
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    /**
     * 当整个excel解析完成后,会回调该函数<br/>
     * 这里也要保存数据，确保最后遗留的数据也存储到数据库
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (cellTypes.size() <= 0){
            parseCellTypes();
        }
        saveData(true);
        cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        System.out.println("所有数据解析完成！");
    }

    /**
     * 获取表格第一行<br/>
     * 1、若该excel无表头则先将第一行数据写入缓存中<br/>
     * 2、否则将表头存入成员中<br/>
     * @param headMap 表格第一行数据
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        if (!hasHead){
            cachedDataList.add(converter(headMap));
        } else {
            converter(headMap, headTitle);
        }
    }

    /**
     * 数据持久化
     */
    protected abstract void saveData(boolean isEnd);

    /**
     * 列类型计算
     */
    protected abstract void parseCellTypes();

    /**
     * Map存list
     */
    private void converter(Map<Integer, String> source, List<String> target){
        for (Integer index : source.keySet()) {
            target.add(source.get(index));
        }
    }

    /**
     * Map存list
     */
    private List<String> converter(Map<Integer, String> source){
        ArrayList<String> target = new ArrayList<>();
        for (Integer index : source.keySet()) {
            target.add(source.get(index));
        }
        return target;
    }

    public void setHasHead(boolean hasHead) {
        this.hasHead = hasHead;
    }

    public List<String> getHeadTitle() {
        return headTitle;
    }
}