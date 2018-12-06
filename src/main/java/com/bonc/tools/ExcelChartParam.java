package com.bonc.tools;

import java.util.*;

public class ExcelChartParam {
    private String functionName;

    public List<Map<Integer, Integer>> getIntCellPosition() {
        return intCellPosition;
    }

    private List<Map<Integer, Integer>> intCellPosition = new ArrayList<Map<Integer, Integer>>();
    public ExcelChartParam(String functionName){
        this.functionName=functionName;
        xString = new ArrayList<String>();
        serTxName = new LinkedHashSet<String>();
        dataRef = new ArrayList<String>();
    }
    public boolean isIntCellPositionIsEmpty(){
        return intCellPosition.isEmpty();
    }
    public void addIntPosition(int row,int col){
        Map map = new HashMap();
        map.put(row,col);
        intCellPosition.add(map);
    }
    public String getFunctionName() {
        return functionName;
    }

    private int[] position;
    private List<String> xString  = null;
    private Set<String> serTxName = null;
    private List<String> dataRef  = null;
    private boolean isShowDataTable = false;

    public boolean isShowDataTable() {
        return isShowDataTable;
    }

    public void setShowDataTable(boolean showDataTable) {
        isShowDataTable = showDataTable;
    }

    public void addDataRef(String ref){
        dataRef.add(ref);
    }
    public void addSerTxName(String txName){
        serTxName.add(txName);
    }
    public void addXString(String xStr){
        xString.add(xStr);
    }
    public void setPosition(int startRow,int startCol,int endRow,int endCol){
        position = new int[]{startRow, startCol, endRow, endCol};
    }

    public int[] getPosition() {
        return position;
    }

    public List<String> getxString() {
        return xString;
    }

    public Set<String> getSerTxName() {
        return serTxName;
    }

    public List<String> getDataRef() {
        return dataRef;
    }
    public static class ExcelChartParamFactory{
        public static ExcelChartParam createBarChart(){
            return createChart(BarChart);
        }
        public static ExcelChartParam createLineChart(){
            return createChart(LineChart);
        }
        private static ExcelChartParam createChart(String chart){
            return new ExcelChartParam(chart);
        }
        public static String BarChart = "BarChart";
        public static String LineChart = "LineChart";
    }

}
