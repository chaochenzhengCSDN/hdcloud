package com.hodo.iiot.group2.hdcloudbankaccountshares.util;
/**
 * Copyright 2013-2015 JueYue (qrb.jueyue@gmail.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.export.base.ExcelExportBase;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;
import cn.afterturn.easypoi.exception.excel.ExcelExportException;
import cn.afterturn.easypoi.exception.excel.enums.ExcelExportEnum;
import cn.afterturn.easypoi.util.PoiPublicUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;
import java.util.*;

import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_CENTER;
import static org.apache.poi.ss.usermodel.CellStyle.VERTICAL_CENTER;

/**
 * Excel导出服务
 *
 * @author JueYue
 *  2014年6月17日 下午5:30:54
 */
public class ExcelExportServer extends ExcelExportBase {

    // 最大行数,超过自动多Sheet
    private int MAX_NUM = 60000;

    protected int createHeaderAndTitle(ExportParams entity, Sheet sheet, Workbook workbook,
                                       List<ExcelExportEntity> excelParams) {
        int rows = 0, feildWidth = getFieldLength(excelParams);
        if (entity.getTitle() != null) {
            rows += createHeaderRow(entity, sheet, workbook, feildWidth);
        }
        //rows += createTitleRow(entity, sheet, workbook, rows, excelParams);
        rows += createTitleRow1(entity, sheet, workbook, rows, excelParams);
        sheet.createFreezePane(0, rows, 0, rows);
        return rows;
    }

    /**
     * 创建 表头改变
     *
     * @param entity
     * @param sheet
     * @param workbook
     * @param feildWidth
     */
    public int createHeaderRow(ExportParams entity, Sheet sheet, Workbook workbook,
                               int feildWidth) {

        Row row = sheet.createRow(0);
        row.setHeight(entity.getTitleHeight());
        createStringCell(row, 0, entity.getTitle(),
                getExcelExportStyler().getHeaderStyle(entity.getHeaderColor()), null);
        for (int i = 1; i <= feildWidth; i++) {
            createStringCell(row, i, "",
                    getExcelExportStyler().getHeaderStyle(entity.getHeaderColor()), null);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, feildWidth));
        if (entity.getSecondTitle() != null) {
            row = sheet.createRow(1);
            row.setHeight(entity.getSecondTitleHeight());
            CellStyle style = workbook.createCellStyle();
            style.setAlignment(CellStyle.ALIGN_RIGHT);
            createStringCell(row, 0, entity.getSecondTitle(), style, null);
            for (int i = 1; i <= feildWidth; i++) {
                createStringCell(row, i, "",
                        getExcelExportStyler().getHeaderStyle(entity.getHeaderColor()), null);
            }
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, feildWidth));
            return 2;
        }
        return 1;
    }

    public void createSheet(Workbook workbook, ExportParams entity, Class<?> pojoClass,
                            Collection<?> dataSet) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Excel export start ,class is {}", pojoClass);
            LOGGER.debug("Excel version is {}",
                    entity.getType().equals(ExcelType.HSSF) ? "03" : "07");
        }
        if (workbook == null || entity == null || pojoClass == null || dataSet == null) {
            throw new ExcelExportException(ExcelExportEnum.PARAMETER_ERROR);
        }
        try {
            List<ExcelExportEntity> excelParams = new ArrayList<ExcelExportEntity>();
            // 得到所有字段
            Field fileds[] = PoiPublicUtil.getClassFields(pojoClass);
            ExcelTarget etarget = pojoClass.getAnnotation(ExcelTarget.class);
            String targetId = etarget == null ? null : etarget.value();
            getAllExcelField(entity.getExclusions(), targetId, fileds, excelParams, pojoClass,
                    null);
            //获取所有参数后,后面的逻辑判断就一致了
            createSheetForMap(workbook, entity, excelParams, dataSet);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ExcelExportException(ExcelExportEnum.EXPORT_ERROR, e.getCause());
        }
    }

    public void createSheetForMap(Workbook workbook, ExportParams entity,
                                  List<ExcelExportEntity> entityList, Collection<?> dataSet) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Excel version is {}",
                    entity.getType().equals(ExcelType.HSSF) ? "03" : "07");
        }
        if (workbook == null || entity == null || entityList == null || dataSet == null) {
            throw new ExcelExportException(ExcelExportEnum.PARAMETER_ERROR);
        }
        super.type = entity.getType();
        if (type.equals(ExcelType.XSSF)) {
            MAX_NUM = 1000000;
        }
        if (entity.getMaxNum() > 0) {
            MAX_NUM = entity.getMaxNum();
        }
        Sheet sheet = null;
        try {
            sheet = workbook.createSheet(entity.getSheetName());
        } catch (Exception e) {
            // 重复遍历,出现了重名现象,创建非指定的名称Sheet
            sheet = workbook.createSheet();
        }
        insertDataToSheet(workbook, entity, entityList, dataSet, sheet);
    }

    protected void insertDataToSheet(Workbook workbook, ExportParams entity,
                                     List<ExcelExportEntity> entityList, Collection<?> dataSet,
                                     Sheet sheet) {
        try {
            dataHanlder = entity.getDataHanlder();
            if (dataHanlder != null && dataHanlder.getNeedHandlerFields() != null) {
                needHanlderList = Arrays.asList(dataHanlder.getNeedHandlerFields());
            }
            // 创建表格样式
            setExcelExportStyler((IExcelExportStyler) entity.getStyle()
                    .getConstructor(Workbook.class).newInstance(workbook));
            Drawing patriarch = sheet.createDrawingPatriarch();
            List<ExcelExportEntity> excelParams = new ArrayList<ExcelExportEntity>();
            if (entity.isAddIndex()) {
                excelParams.add(indexExcelEntity(entity));
            }
            excelParams.addAll(entityList);
            sortAllParams(excelParams);
            int index = entity.isCreateHeadRows()
                    ? createHeaderAndTitle(entity, sheet, workbook, excelParams) : 0;
            int titleHeight = index;
            setCellWith(excelParams, sheet);
            short rowHeight = getRowHeight(excelParams);
            setCurrentIndex(1);
            Iterator<?> its = dataSet.iterator();
            List<Object> tempList = new ArrayList<Object>();
            while (its.hasNext()) {
                Object t = its.next();
                index += createCells(patriarch, index, t, excelParams, sheet, workbook, rowHeight);
                tempList.add(t);
                if (index >= MAX_NUM)
                    break;
            }
            if (entity.getFreezeCol() != 0) {
                sheet.createFreezePane(entity.getFreezeCol(), 0, entity.getFreezeCol(), 0);
            }

            mergeCells(sheet, excelParams, titleHeight);

            its = dataSet.iterator();
            for (int i = 0, le = tempList.size(); i < le; i++) {
                its.next();
                its.remove();
            }
            // 发现还有剩余list 继续循环创建Sheet
            if (dataSet.size() > 0) {
                createSheetForMap(workbook, entity, entityList, dataSet);
            } else {
                // 创建合计信息
                addStatisticsRow(getExcelExportStyler().getStyles(true, null), sheet);
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ExcelExportException(ExcelExportEnum.EXPORT_ERROR, e.getCause());
        }
    }

    /**
     * 创建表头
     *
     * @param title
     * @param index
     */
    private int createTitleRow(ExportParams title, Sheet sheet, Workbook workbook, int index,
                               List<ExcelExportEntity> excelParams) {
        Row row = sheet.createRow(index);
        int rows = getRowNums(excelParams);
        row.setHeight((short) 450);
        Row listRow = null;
        if (rows == 2) {
            listRow = sheet.createRow(index + 1);
            listRow.setHeight((short) 450);
        }
        int cellIndex = 0;
        CellStyle titleStyle = getExcelExportStyler().getTitleStyle(title.getColor());
        for (int i = 0, exportFieldTitleSize = excelParams.size(); i < exportFieldTitleSize; i++) {
            ExcelExportEntity entity = excelParams.get(i);
            if (StringUtils.isNotBlank(entity.getName())) {
                createStringCell(row, cellIndex, entity.getName(), titleStyle, entity);
            }
            if (entity.getList() != null) {
                List<ExcelExportEntity> sTitel = entity.getList();
                if (StringUtils.isNotBlank(entity.getName())) {
                    sheet.addMergedRegion(new CellRangeAddress(index, index, cellIndex,
                            cellIndex + sTitel.size() - 1));
                }
                for (int j = 0, size = sTitel.size(); j < size; j++) {
                    createStringCell(rows == 2 ? listRow : row, cellIndex, sTitel.get(j).getName(),
                            titleStyle, entity);
                    cellIndex++;
                }
                cellIndex--;
            } else if (rows == 2) {
                createStringCell(listRow, cellIndex, "", titleStyle, entity);
                sheet.addMergedRegion(new CellRangeAddress(index, index + 1, cellIndex, cellIndex));
            }
            cellIndex++;
        }
        return rows;

    }

    private int createTitleRow1(ExportParams title, Sheet sheet, Workbook workbook, int index,
                                List<ExcelExportEntity> excelParams) {
        String[] head0 = {"银行账户","授信银行","已批复额度","已使用授信额度", "可使用授信额度", "拟新增授信额度", "批复授信期限",
                "贷款明细","贷款明细","贷款明细","贷款明细","贷款明细"
        };
        String[] head1 = {"金额","贷款日(1999-01-01)", "到账日(1999-01-01)",
                "利率(%)","借账方式"};

        String[] headnum0 = {"2,3,0,0", "2,3,1,1", "2,3,2,2","2,3,3,3","2,3,4,4","2,3,5,5","2,3,6,6","2,2,7,11"};
        Row row = sheet.createRow(2);
        Cell cell = null;
        for (int i = 0; i < head0.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(head0[i]);
            cell.getCellStyle().setWrapText(true);
            cell.getCellStyle().setAlignment(ALIGN_CENTER);
            cell.getCellStyle().setVerticalAlignment(VERTICAL_CENTER);
        }
        for (int i = 0; i < headnum0.length; i++) {
            String[] temp = headnum0[i].split(",");
            Integer startrow = Integer.parseInt(temp[0]);
            Integer overrow = Integer.parseInt(temp[1]);
            Integer startcol = Integer.parseInt(temp[2]);
            Integer overcol = Integer.parseInt(temp[3]);
            sheet.addMergedRegion(new CellRangeAddress(startrow, overrow,
                    startcol, overcol));
        }
        row = sheet.createRow(3);
        for (int i = 0; i < head0.length; i++) {
            cell = row.createCell(i);
            if (i > 6) {
                for (int j = 0; j < head1.length; j++) {
                    cell = row.createCell(j + 7);
                    cell.setCellValue(head1[j]);
                    cell.getCellStyle().setWrapText(true);
                    cell.getCellStyle().setAlignment(ALIGN_CENTER);
                    cell.getCellStyle().setVerticalAlignment(VERTICAL_CENTER);
                }
            }
        }
//        for (int t = 0; t < headnum1.length; t++) {
//            String[] temp = headnum1[t].split(",");
//            Integer startrow = Integer.parseInt(temp[0]);
//            Integer overrow = Integer.parseInt(temp[1]);
//            Integer startcol = Integer.parseInt(temp[2]);
//            Integer overcol = Integer.parseInt(temp[3]);
//            sheet.addMergedRegion(new CellRangeAddress(startrow, overrow,
//                    startcol, overcol));
//        }
        return 2;
    }


}

