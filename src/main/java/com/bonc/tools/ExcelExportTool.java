package com.bonc.tools;

import java.io.IOException;
import java.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.charts.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;

public class ExcelExportTool {
    /**
     * 数据行标识
     */
    public final static String DATA_LINE = "datas";
    /**
     * 默认样式标识
     */
    public final static String DEFAULT_STYLE = "defaultStyles";
    /**
     * 行样式标识
     */
    public final static String STYLE = "styles";
    /**
     * 数据的初始化列数
     */
    private int initColIndex;
    /**
     * 数据的初始化行数
     */
    private int initRowIndex;
    /**
     * 当前列数
     */
    private int curColIndex;
    /**
     * 当前行数
     */
    private int curRowIndex;
    /**
     * 当前行对象
     */
    private XSSFRow curRow;
    /**
     * 最后一行的数据
     */
    private int lastRowIndex;
    /**
     * 默认样式
     */
    private CellStyle defaultStyle;
    /**
     * 默认行高
     */
    private float rowHeight;
    /**
     * 存储某一方所对于的样式
     */
    private Map<Integer, CellStyle> styles;


    private XSSFWorkbook wb = null;

    private XSSFSheet sheet = null;

    /**
     * @param wb
     * @param sheet
     */
    public ExcelExportTool(XSSFWorkbook wb, XSSFSheet sheet) {
        this.wb = wb;
        this.sheet = sheet;
    }

    /**
     * @param wb
     * @param sheet
     */
    public ExcelExportTool(String path) {
        try {
            //System.out.println(ExportUtil.class.getClassLoader().getResource(path).getPath());
            wb = new XSSFWorkbook(ExcelExportTool.class.getClassLoader().getResourceAsStream(path));
            initTemplate();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("读取模板不存在！请检查");
        }
    }

    private void initTemplate() {
        sheet = wb.getSheetAt(0);
        initConfigData();
        lastRowIndex = sheet.getLastRowNum();
        curRow = sheet.createRow(curRowIndex);
    }

    /**
     * 合并单元格后给合并后的单元格加边框
     *
     * @param region
     * @param cs
     */
    public void setRegionStyle(CellRangeAddress region, XSSFCellStyle cs) {

        int toprowNum = region.getFirstRow();
        for (int i = toprowNum; i <= region.getLastRow(); i++) {
            XSSFRow row = sheet.getRow(i);
            for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
                XSSFCell cell = row.getCell(j);// XSSFCellUtil.getCell(row,
                // (short) j);
                cell.setCellStyle(cs);
            }
        }
    }

    /**
     * 设置表头的单元格样式
     *
     * @return
     */
    public XSSFCellStyle getHeadStyle() {
        // 创建单元格样式
        XSSFCellStyle cellStyle = wb.createCellStyle();
        // 设置单元格的背景颜色为淡蓝色
        cellStyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        // 设置单元格居中对齐
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        // 设置单元格垂直居中对齐
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 创建单元格内容显示不下时自动换行
        cellStyle.setWrapText(true);
        // 设置单元格字体样式
        XSSFFont font = wb.createFont();
        // 设置字体加粗
        //font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short) 200);
        cellStyle.setFont(font);
        // 设置单元格边框为细线条
        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        return cellStyle;
    }

    /**
     * 设置表体的单元格样式
     *
     * @return
     */
    public XSSFCellStyle getBodyStyle() {
        // 创建单元格样式
        XSSFCellStyle cellStyle = wb.createCellStyle();
        // 设置单元格居中对齐
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        // 设置单元格垂直居中对齐
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 创建单元格内容显示不下时自动换行
        cellStyle.setWrapText(true);
        // 设置单元格字体样式
        XSSFFont font = wb.createFont();
        // 设置字体加粗
        //font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short) 200);
        cellStyle.setFont(font);
        // 设置单元格边框为细线条
        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        return cellStyle;
    }

    /**
     * 设置表体的单元格样式
     *
     * @return
     */
    public XSSFCellStyle getBodyStyleOfString() {
        // 创建单元格样式
        XSSFCellStyle cellStyle = wb.createCellStyle();
        // 设置单元格居中对齐
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);
        // 设置单元格垂直居中对齐
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 创建单元格内容显示不下时自动换行
        cellStyle.setWrapText(true);
        // 设置单元格字体样式
        XSSFFont font = wb.createFont();
        // 设置字体加粗
        //font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short) 200);
        cellStyle.setFont(font);
        // 设置单元格边框为细线条
        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        return cellStyle;
    }

    /**
     * 设置表体的单元格样式
     *
     * @return
     */
    public XSSFCellStyle getBodyStyleOfNumber() {
        // 创建单元格样式
        XSSFCellStyle cellStyle = wb.createCellStyle();
        // 设置单元格居中对齐
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
        // 设置单元格垂直居中对齐
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 创建单元格内容显示不下时自动换行
        //cellStyle.setWrapText(true);
        // 设置单元格字体样式
        XSSFFont font = wb.createFont();
        // 设置字体加粗
        //font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short) 200);
        cellStyle.setFont(font);
        // 设置单元格边框为细线条
        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);

        DataFormat format = wb.createDataFormat();
        cellStyle.setDataFormat(format.getFormat("#,##0.00"));
        return cellStyle;
    }

    /**
     * 创建新行，在使用时只要添加完一行，需要调用该方法创建
     */
    public void createNewRow() {
        if (lastRowIndex > curRowIndex && curRowIndex != initRowIndex) {
            sheet.shiftRows(curRowIndex, lastRowIndex, 1, true, true);
            lastRowIndex++;
        }
        curRow = sheet.createRow(curRowIndex);
        curRow.setHeightInPoints(rowHeight);
        curRowIndex++;
        curColIndex = initColIndex;
    }

    /**
     * 创建相应的元素
     */
    public XSSFCell createCell() {
        XSSFCell cell = curRow.createCell(curColIndex);
        setCellStyle(cell);
        curColIndex++;
        return cell;
    }

    /**
     * 设置某个元素的样式
     *
     * @param c
     */
    private void setCellStyle(Cell c) {
        if (styles.containsKey(curColIndex)) {
            c.setCellStyle(styles.get(curColIndex));
        } else {
            c.setCellStyle(defaultStyle);
        }
    }

    /**
     * 初始化数据信息
     */
    private void initConfigData() {
        boolean findData = false;
        for (Row row : sheet) {
            if (findData) break;
            for (Cell c : row) {
                if (c.getCellType() != Cell.CELL_TYPE_STRING) continue;
                String str = c.getStringCellValue().trim();
                if (str.equals(DATA_LINE)) {
                    initColIndex = c.getColumnIndex();
                    initRowIndex = row.getRowNum();
                    curColIndex = initColIndex;
                    curRowIndex = initRowIndex;
                    findData = true;
                    defaultStyle = c.getCellStyle();
                    rowHeight = row.getHeightInPoints();
                    initStyles();
                    break;
                }
            }
        }
        //System.out.println(curColIndex+","+curRowIndex);
    }

    /**
     * 初始化样式信息
     */
    private void initStyles() {
        styles = new HashMap<Integer, CellStyle>();
        for (Row row : sheet) {
            for (Cell c : row) {
                if (c.getCellType() != Cell.CELL_TYPE_STRING) continue;
                String str = c.getStringCellValue().trim();
                if (str.equals(DEFAULT_STYLE)) {
                    defaultStyle = c.getCellStyle();
                }
                if (str.equals(STYLE)) {
                    styles.put(c.getColumnIndex(), c.getCellStyle());
                }
            }
        }
    }

    /**
     * 将文件写到某个输出流中
     *
     * @param out
     */
    public void wirteToStream(ServletOutputStream out) {
        try {
            wb.write(out);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("写入流失败:" + e.getMessage());
        }
    }

    /**
     * 导出Excel 设置 文件名、sheet名、标题、数据即可导出
     *
     * @param response
     * @param fileName
     * @param sheetName
     * @param titles
     * @param data
     */
    public static void exportExcel(HttpServletResponse response, String fileName, String sheetName,
                                   String[] titles, List<String[]> data) {
        response.setContentType("application/binary;charset=ISO8859_1");
        ServletOutputStream out = null;
        try {
            fileName = new String(fileName.getBytes("gb2312"), "iso8859-1");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");// 组装附件名称和格式
            out = response.getOutputStream();

            // 创建一个workbook 对应一个excel应用文件
            XSSFWorkbook workBook = new XSSFWorkbook();
            // 在workbook中添加一个sheet,对应Excel文件中的sheet
            XSSFSheet sheet = workBook.createSheet(sheetName);

            ExcelExportTool exportUtil = new ExcelExportTool(workBook, sheet);
            XSSFCellStyle headStyle = exportUtil.getHeadStyle();
            XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
            // 构建表头
            XSSFRow headRow = sheet.createRow(0);
            XSSFCell cell = null;
            for (int i = 0; i < titles.length; i++) {
                cell = headRow.createCell(i);
                cell.setCellStyle(headStyle);
                cell.setCellValue(titles[i]);
            }

            // 构建表体数据
            if (data != null && data.size() > 0) {
                for (int j = 0; j < data.size(); j++) {
                    XSSFRow bodyRow = sheet.createRow(j + 1);
                    String[] obj = data.get(j);

                    for (int k = 0; k < obj.length; k++) {
                        cell = bodyRow.createCell(k);

                        cell.setCellStyle(bodyStyle);
                        cell.setCellValue(obj[k]);


                    }
                }
            }

            for (int i = 0; i < titles.length; i++) {
                sheet.autoSizeColumn((short) i); //调整列宽度
            }

            workBook.write(out);
            workBook.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 导出Excel 设置 文件名、sheet名、标题、数据即可导出,图标的参数
     *
     * @param response
     * @param fileName
     * @param sheetName
     * @param titles
     * @param data
     * @param chartParam
     */
    public static void exportExcelWithChart(HttpServletResponse response, String fileName, String sheetName,
                                            String[] titles, List<String[]> data, ExcelChartParam chartParam) {
        response.setContentType("application/binary;charset=ISO8859_1");
        ServletOutputStream out = null;
        try {
            fileName = new String(fileName.getBytes("gb2312"), "iso8859-1");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");// 组装附件名称和格式
            out = response.getOutputStream();

            // 创建一个workbook 对应一个excel应用文件
            XSSFWorkbook workBook = new XSSFWorkbook();
            // 在workbook中添加一个sheet,对应Excel文件中的sheet
            XSSFSheet sheet = workBook.createSheet(sheetName);

            ExcelExportTool exportUtil = new ExcelExportTool(workBook, sheet);
            XSSFCellStyle headStyle = exportUtil.getHeadStyle();
            XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
            // 构建表头
            XSSFRow headRow = sheet.createRow(0);
            XSSFCell cell = null;
            for (int i = 0; i < titles.length; i++) {
                cell = headRow.createCell(i);
                cell.setCellStyle(headStyle);
                cell.setCellValue(titles[i]);
            }

            // 构建表体数据
            if (data != null && data.size() > 0) {
                for (int j = 0; j < data.size(); j++) {
                    XSSFRow bodyRow = sheet.createRow(j + 1);
                    String[] obj = data.get(j);

                    for (int k = 0; k < obj.length; k++) {
                        cell = bodyRow.createCell(k);

                        cell.setCellStyle(bodyStyle);
                        cell.setCellValue(obj[k]);


                    }
                }
            }

            for (int i = 0; i < titles.length; i++) {
                sheet.autoSizeColumn((short) i); //调整列宽度
            }

            drawChart(workBook, sheet, chartParam);
            workBook.write(out);
            workBook.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 导出Excel 设置 文件名、sheet名、标题、数据即可导出
     *
     * @param response
     * @param fileName
     * @param sheetName
     * @param titles
     * @param data
     */
    public static void exportExcelV2(HttpServletResponse response, String fileName, String sheetName,
                                     String[] titles, List<Object[]> data) {
        response.setContentType("application/binary;charset=ISO8859_1");
        ServletOutputStream out = null;
        try {
            fileName = new String(fileName.getBytes("gb2312"), "iso8859-1");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");// 组装附件名称和格式
            out = response.getOutputStream();

            // 创建一个workbook 对应一个excel应用文件
            XSSFWorkbook workBook = new XSSFWorkbook();
            // 在workbook中添加一个sheet,对应Excel文件中的sheet
            XSSFSheet sheet = workBook.createSheet(sheetName);

            ExcelExportTool exportUtil = new ExcelExportTool(workBook, sheet);
            XSSFCellStyle headStyle = exportUtil.getHeadStyle();
            XSSFCellStyle bodyStyleOfString = exportUtil.getBodyStyleOfString();
            XSSFCellStyle bodyStyleOfNumber = exportUtil.getBodyStyleOfNumber();
            // 构建表头
            XSSFRow headRow = sheet.createRow(0);
            XSSFCell cell = null;
            for (int i = 0; i < titles.length; i++) {
                cell = headRow.createCell(i);
                cell.setCellStyle(headStyle);
                cell.setCellValue(titles[i]);
            }

            // 构建表体数据
            if (data != null && data.size() > 0) {
                for (int j = 0; j < data.size(); j++) {
                    XSSFRow bodyRow = sheet.createRow(j + 1);
                    Object[] obj = data.get(j);

                    for (int k = 0; k < obj.length; k++) {
                        cell = bodyRow.createCell(k);

                        //针对数字做右对齐
                        if (obj[k] instanceof Double) {
                            cell.setCellStyle(bodyStyleOfNumber);
                            cell.setCellValue(((Double) obj[k]).doubleValue());

                        } else {
                            cell.setCellStyle(bodyStyleOfString);
                            cell.setCellValue(obj[k].toString());
                        }


                    }
                }
            }

            for (int i = 0; i < titles.length; i++) {
                sheet.autoSizeColumn((short) i); //调整列宽度
            }

            workBook.write(out);
            workBook.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public interface Transfer {
        public Object transfer(Object object, Object... params);
    }

    public static List<String[]> transData(List<Object> datas, String[] titles, String[] fields, Transfer transfer, Object... params) {
        List<String[]> res = new ArrayList<String[]>();
        for (Object obj : datas) {
            if (transfer != null) {
                obj = transfer.transfer(obj, params);
            }

            Map<?, ?> data = (Map<?, ?>) obj;
            if (data != null) {
                List<String> row = new ArrayList<String>();
                for (String field : fields) {
                    row.add(data.get(field) == null ? "" : data.get(field).toString());
                }
                res.add(row.toArray(new String[]{}));
            }
        }
        return res;
    }

    public static String[] transFooter(String[] fields, String[] params) {
        List<String> row = new ArrayList<String>();
        List<String> transfers = Arrays.asList(params);
        for (String field : fields) {
            if (transfers.contains(field)) {
                row.add(transfers.get(transfers.indexOf(field) + 1));
            } else {
                row.add("");
            }
        }
        return row.toArray(new String[]{});
    }


    /*
     * @param params params.get(0) 为图表位置 0123为起始横坐标，起始纵坐标，终点横，终点纵
     *
     * @param params params.get(1) 为x轴取值范围，其他为折线取值范围。起始行号，终止行号， 起始列号，终止列号（折线名称坐标
     * ）。
     */
    private static void drawLineChart(XSSFSheet sheet, List<int[]> params) {

        Drawing drawing = sheet.createDrawingPatriarch();
        // 设置位置 起始横坐标，起始纵坐标，终点横，终点纵
        ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, params.get(0)[0], params.get(0)[1], params.get(0)[2],
                params.get(0)[3]);
        Chart chart = drawing.createChart(anchor);

        // 创建标题

        // 创建图形注释的位置
        ChartLegend legend = chart.getOrCreateLegend();
        legend.setPosition(LegendPosition.BOTTOM);
        // 创建绘图的类型 LineCahrtData 折线图
        LineChartData chartData = chart.getChartDataFactory().createLineChartData();
        // 设置横坐标
        ChartAxis bottomAxis = chart.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);
        bottomAxis.setCrosses(AxisCrosses.AUTO_ZERO);
        // 设置纵坐标
        ValueAxis leftAxis = chart.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);

        leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
        // 从Excel中获取数据.CellRangeAddress-->params: 起始行号，终止行号， 起始列号，终止列号
        // 数据类别（x轴）
        ChartDataSource xAxis = DataSources.fromStringCellRange(sheet,
                new CellRangeAddress(params.get(1)[0], params.get(1)[1], params.get(1)[2], params.get(1)[3]));
        for (int i = 2, len = params.size(); i < len; i++) {
            // 数据源
            ChartDataSource dataAxis = DataSources.fromNumericCellRange(sheet,
                    new CellRangeAddress(params.get(i)[0], params.get(i)[1], params.get(i)[2], params.get(i)[3]));

            LineChartSeries chartSeries = chartData.addSeries(xAxis, dataAxis);
            // 给每条折线创建名字
            chartSeries.setTitle(sheet.getRow(params.get(i)[4]).getCell(params.get(i)[5]).getStringCellValue());
        }

        setChartTitle((XSSFChart) chart, sheet.getSheetName());
        // 开始绘制折线图
        chart.plot(chartData, bottomAxis, leftAxis);
    }

    private static void drawCTLineChart(XSSFSheet sheet, int[] position, List<String> xString, Set<String> serTxName,
                                        List<String> dataRef) {

        Drawing drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, position[0], position[1], position[2], position[3]);

        Chart chart = drawing.createChart(anchor);

        CTChart ctChart = ((XSSFChart) chart).getCTChart();
        CTPlotArea ctPlotArea = ctChart.getPlotArea();
        CTLineChart ctLineChart = ctPlotArea.addNewLineChart();

        ctLineChart.addNewVaryColors().setVal(true);

        // telling the Chart that it has axis and giving them Ids
        setAxIds(ctLineChart);
        // set cat axis
        setCatAx(ctPlotArea);
        // set val axis
        setValAx(ctPlotArea);
        // legend
        setLegend(ctChart);
        // set data lable
        setDataLabel(ctLineChart);
        // set chart title
        setChartTitle((XSSFChart) chart, sheet.getSheetName());

        paddingData(ctLineChart, xString, serTxName, dataRef);
    }

    private static void drawChart(XSSFWorkbook wb, XSSFSheet sheet, ExcelChartParam param) {

        if (!param.isIntCellPositionIsEmpty()) {
            List<Map<Integer, Integer>> pos = param.getIntCellPosition();
            for (Map<Integer, Integer> xy : pos) {
                Set<Integer> xI = xy.keySet();
                Iterator<Integer> iterator = xI.iterator();
                Integer x = 0;
                while (iterator.hasNext()) x = iterator.next();
                Integer y = xy.get(x);
                Cell cell = sheet.getRow(y).getCell(x);

                double val = -1;
                String str = "";
                if (cell != null) {

                    if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        str = cell.getStringCellValue();
                    } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        val = cell.getNumericCellValue();
                    }
                    if (str.contains("%")) {
                        cell.setCellStyle(getBodyStyleOfPercent(wb));
                        cell.setCellValue(Double.parseDouble(str.substring(0,str.indexOf("%")))/100);
                    } else {
                        cell.setCellStyle(getBodyStyleOfDouble(wb));
                        cell.setCellValue(Double.parseDouble(str));
                    }
                }
            }
        }


        if (param.getFunctionName().equals(ExcelChartParam.ExcelChartParamFactory.BarChart))
//            drawBarChart(sheet, param.getPosition(), param.getxString(), param.getSerTxName(), param.getDataRef());
            drawBarChartWithChartParam(sheet,param);
        else if (param.getFunctionName().equals(ExcelChartParam.ExcelChartParamFactory.LineChart)) {
            drawBarChart(sheet, param.getPosition(), param.getxString(), param.getSerTxName(), param.getDataRef());
        }
    }

    public static XSSFCellStyle getBodyStyleOfDouble(XSSFWorkbook wb) {
        // 创建单元格样式
        XSSFCellStyle cellStyle = wb.createCellStyle();
        // 设置单元格居中对齐
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
        // 设置单元格垂直居中对齐
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 创建单元格内容显示不下时自动换行
        //cellStyle.setWrapText(true);
        // 设置单元格字体样式
        XSSFFont font = wb.createFont();
        // 设置字体加粗
        //font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short) 200);
        cellStyle.setFont(font);
        // 设置单元格边框为细线条
        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);

        DataFormat format = wb.createDataFormat();
        cellStyle.setDataFormat(format.getFormat("#,##0.00"));
        return cellStyle;
    }

    public static XSSFCellStyle getBodyStyleOfPercent(XSSFWorkbook wb) {
        // 创建单元格样式
        XSSFCellStyle cellStyle = wb.createCellStyle();
        // 设置单元格居中对齐
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
        // 设置单元格垂直居中对齐
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 创建单元格内容显示不下时自动换行
        // cellStyle.setWrapText(true);
        // 设置单元格字体样式
        XSSFFont font = wb.createFont();
        // 设置字体加粗
        // font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short) 200);
        cellStyle.setFont(font);
        // 设置单元格边框为细线条
        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);

        DataFormat format = wb.createDataFormat();
        cellStyle.setDataFormat(format.getFormat("#"));

        cellStyle.setDataFormat(format.getFormat("0.00%"));
        return cellStyle;
    }

    /*
     * @param position 图表坐标 起始行，起始列，终点行，重点列
     *
     * @param xString 横坐标
     *
     * @param serTxName 图形示例
     *
     * @param dataRef 柱状图数据范围 ： sheetName!$A$1:$A$12
     */

    private static void drawBarChartWithChartParam(XSSFSheet sheet,ExcelChartParam param){

        Drawing drawing = sheet.createDrawingPatriarch();
        int[] position = param.getPosition();
        ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, position[0], position[1], position[2], position[3]);

        Chart chart = drawing.createChart(anchor);

        CTChart ctChart = ((XSSFChart) chart).getCTChart();
        CTPlotArea ctPlotArea = ctChart.getPlotArea();
        CTBarChart ctBarChart = ctPlotArea.addNewBarChart();

        ctBarChart.addNewVaryColors().setVal(false);
        ctBarChart.addNewBarDir().setVal(STBarDir.COL);

        // telling the Chart that it has axis and giving them Ids
        setAxIds(ctBarChart);
        // set cat axis
        setCatAx(ctPlotArea);
        // set val axis
        setValAx(ctPlotArea);
        // add legend and set legend position
        setLegend(ctChart);
        // set data table is show
        if(param.isShowDataTable()){
            ctPlotArea.addNewDTable();
        }
        // set data lable
        setDataLabel(ctBarChart);
        // set chart title
        setChartTitle((XSSFChart) chart, sheet.getSheetName());
        // padding data to chart
        paddingData(ctBarChart, param.getxString(), param.getSerTxName(), param.getDataRef());

    }
    private static void drawBarChart(XSSFSheet sheet, int[] position, List<String> xString, Set<String> serTxName,
                                     List<String> dataRef) {

        Drawing drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, position[0], position[1], position[2], position[3]);

        Chart chart = drawing.createChart(anchor);

        CTChart ctChart = ((XSSFChart) chart).getCTChart();
        CTPlotArea ctPlotArea = ctChart.getPlotArea();
        CTBarChart ctBarChart = ctPlotArea.addNewBarChart();

        ctBarChart.addNewVaryColors().setVal(false);
        ctBarChart.addNewBarDir().setVal(STBarDir.COL);

        // telling the Chart that it has axis and giving them Ids
        setAxIds(ctBarChart);
        // set cat axis
        setCatAx(ctPlotArea);
        // set val axis
        setValAx(ctPlotArea);
        // add legend and set legend position
        setLegend(ctChart);
        // set data lable
        setDataLabel(ctBarChart);
        // set chart title
        setChartTitle((XSSFChart) chart, sheet.getSheetName());
        // padding data to chart
        paddingData(ctBarChart, xString, serTxName, dataRef);
        // set data table is show


    }

    private static void paddingData(CTBarChart ctBarChart, List<String> xString, Set<String> serTxName,
                                    List<String> dataRef) {
        Iterator<String> iterator = serTxName.iterator();
        for (int r = 0, len = dataRef.size(); r < len && iterator.hasNext(); r++) {
            CTBarSer ctBarSer = ctBarChart.addNewSer();

            ctBarSer.addNewIdx().setVal(r);
            // set legend value
            setLegend(iterator.next(), ctBarSer.addNewTx());
            // cat ax value
            setChartCatAxLabel(ctBarSer.addNewCat(), xString);
            // value range
            ctBarSer.addNewVal().addNewNumRef().setF(dataRef.get(r));
            // add border to chart
            ctBarSer.addNewSpPr().addNewLn().addNewSolidFill().addNewSrgbClr().setVal(new byte[]{0, 0, 0});
        }
    }

    private static void setLegend(String str, CTSerTx ctSerTx) {
        if (str.contains("$"))
            // set legend by str ref
            ctSerTx.addNewStrRef().setF(str);
        else
            // set legend by str
            ctSerTx.setV(str);
    }

    private static void paddingData(CTLineChart ctLineChart, List<String> xString, Set<String> serTxName,
                                    List<String> dataRef) {
        Iterator<String> iterator = serTxName.iterator();
        for (int r = 0, len = dataRef.size(); r < len && iterator.hasNext(); r++) {
            CTLineSer ctLineSer = ctLineChart.addNewSer();
            ctLineSer.addNewIdx().setVal(r);
            setLegend(iterator.next(), ctLineSer.addNewTx());
            setChartCatAxLabel(ctLineSer.addNewCat(), xString);
            ctLineSer.addNewVal().addNewNumRef().setF(dataRef.get(r));
            ctLineSer.addNewSpPr().addNewLn().addNewSolidFill().addNewSrgbClr().setVal(new byte[]{0, 0, 0});
        }
    }

    private static void setChartCatAxLabel(CTAxDataSource cttAxDataSource, List<String> xString) {
        if (xString.size() == 1) {
            cttAxDataSource.addNewStrRef().setF(xString.get(0));
        } else {
            CTStrData ctStrData = cttAxDataSource.addNewStrLit();
            for (int m = 0, xlen = xString.size(); m < xlen; m++) {
                CTStrVal ctStrVal = ctStrData.addNewPt();
                ctStrVal.setIdx((long) m);
                ctStrVal.setV(xString.get(m));
            }
        }
    }

    private static void setDataLabel(CTBarChart ctBarChart) {
        setDLShowOpts(ctBarChart.addNewDLbls());
    }

    private static void setDataLabel(CTLineChart ctLineChart) {
        CTDLbls dlbls = ctLineChart.addNewDLbls();
        setDLShowOpts(dlbls);
        setDLPosition(dlbls, null);
    }

    private static void setDLPosition(CTDLbls dlbls, STDLblPos.Enum e) {
        if (e == null)
            dlbls.addNewDLblPos().setVal(STDLblPos.T);
        else
            dlbls.addNewDLblPos().setVal(e);
    }

    private static void setDLShowOpts(CTDLbls dlbls) {
        // 添加图形示例的字符串
        dlbls.addNewShowSerName().setVal(false);
        // 添加x轴的坐标字符串
        dlbls.addNewShowCatName().setVal(false);
        // 添加图形示例的图片
        dlbls.addNewShowLegendKey().setVal(false);
        // 添加x对应y的值---全设置成false 就没什么用处了
        // dlbls.addNewShowVal().setVal(false);
    }

    private static void setAxIds(CTBarChart ctBarChart) {
        ctBarChart.addNewAxId().setVal(123456);
        ctBarChart.addNewAxId().setVal(123457);
    }

    private static void setAxIds(CTLineChart ctLineChart) {
        ctLineChart.addNewAxId().setVal(123456);
        ctLineChart.addNewAxId().setVal(123457);
    }

    private static void setLegend(CTChart ctChart) {
        CTLegend ctLegend = ctChart.addNewLegend();
        ctLegend.addNewLegendPos().setVal(STLegendPos.B);
        ctLegend.addNewOverlay().setVal(false);
    }

    private static void setCatAx(CTPlotArea ctPlotArea) {
        CTCatAx ctCatAx = ctPlotArea.addNewCatAx();
        ctCatAx.addNewAxId().setVal(123456); // id of the cat axis
        CTScaling ctScaling = ctCatAx.addNewScaling();
        ctScaling.addNewOrientation().setVal(STOrientation.MIN_MAX);
        ctCatAx.addNewDelete().setVal(false);
        ctCatAx.addNewAxPos().setVal(STAxPos.B);
        ctCatAx.addNewCrossAx().setVal(123457); // id of the val axis
        ctCatAx.addNewTickLblPos().setVal(STTickLblPos.NEXT_TO);

    }

    // 一点结项目要求不要y轴的标签，或者y轴尽可能的窄一些
    private static void setValAx(CTPlotArea ctPlotArea) {
        CTValAx ctValAx = ctPlotArea.addNewValAx();
        ctValAx.addNewAxId().setVal(123457); // id of the val axis
        CTScaling ctScaling = ctValAx.addNewScaling();
        ctScaling.addNewOrientation().setVal(STOrientation.MIN_MAX);
        // 不现实y轴-->该为false之后变为实现y轴
        ctValAx.addNewDelete().setVal(false);
        ctValAx.addNewAxPos().setVal(STAxPos.L);
        ctValAx.addNewCrossAx().setVal(123456); // id of the cat axis
        ctValAx.addNewTickLblPos().setVal(STTickLblPos.NEXT_TO);
    }

    // 图标标题
    private static void setChartTitle(XSSFChart xchart, String titleStr) {
        CTChart ctChart = xchart.getCTChart();

        CTTitle title = CTTitle.Factory.newInstance();
        CTTx cttx = title.addNewTx();
        CTStrData sd = CTStrData.Factory.newInstance();
        CTStrVal str = sd.addNewPt();
        str.setIdx(123459);
        str.setV(titleStr);
        cttx.addNewStrRef().setStrCache(sd);

        ctChart.setTitle(title);
    }

}
