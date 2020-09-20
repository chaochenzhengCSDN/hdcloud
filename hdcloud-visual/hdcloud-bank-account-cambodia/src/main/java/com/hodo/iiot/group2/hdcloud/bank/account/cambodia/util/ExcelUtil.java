package com.hodo.iiot.group2.hdcloud.bank.account.cambodia.util;


import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.export.ExcelExportServer;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/*import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.poi.excel.export.ExcelExportServer;*/

public class ExcelUtil<T> {
    public void print(String filename, Class<?> clazz, String title, String secondTitle, String sheetName, List<T> olist, HttpServletRequest request,
                      HttpServletResponse response) throws Exception {
        ModelMap model = new ModelMap();

        model.put(NormalExcelConstants.FILE_NAME, filename);
        model.put(NormalExcelConstants.CLASS, clazz);
        model.put(NormalExcelConstants.PARAMS, new ExportParams(title, secondTitle, sheetName));
        model.put(NormalExcelConstants.DATA_LIST, olist);

        String codedFileName = "临时文件";
        Workbook workbook = null;
        if (model.containsKey(NormalExcelConstants.MAP_LIST)) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) model
                    .get(NormalExcelConstants.MAP_LIST);
            if (list.size() == 0) {
                throw new RuntimeException("MAP_LIST IS NULL");
            }
            workbook = ExcelExportUtil.exportExcel(
                    (ExportParams) list.get(0).get(NormalExcelConstants.PARAMS), (Class<?>) list.get(0)
                            .get(NormalExcelConstants.CLASS),
                    (Collection<?>) list.get(0).get(NormalExcelConstants.DATA_LIST));
            for (int i = 1; i < list.size(); i++) {
                new ExcelExportServer().createSheet(workbook,
                        (ExportParams) list.get(i).get(NormalExcelConstants.PARAMS), (Class<?>) list
                                .get(i).get(NormalExcelConstants.CLASS),
                        (Collection<?>) list.get(i).get(NormalExcelConstants.DATA_LIST));
            }
        } else {
            workbook = ExcelExportUtil.exportExcel(
                    (ExportParams) model.get(NormalExcelConstants.PARAMS),
                    (Class<?>) model.get(NormalExcelConstants.CLASS),
                    (Collection<?>) model.get(NormalExcelConstants.DATA_LIST));
        }
        if (model.containsKey(NormalExcelConstants.FILE_NAME)) {
            codedFileName = (String) model.get(NormalExcelConstants.FILE_NAME);
        }
        String dataStr = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dataStr = sdf.format(new Date());
        if (workbook instanceof HSSFWorkbook) {
            codedFileName += dataStr + ".xls";
        } else {
            codedFileName += dataStr + ".xlsx";
        }
        response.setHeader("content-disposition", "attachment;filename*=UTF-8''" + URLEncoder.encode(codedFileName, "UTF-8"));
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("UTF-8");
        //response.reset();
        //response.setContentType("application/vnd.ms-excel;charset=UTF-8");//下面三行是关键代码，处理乱码问题
        //response.setContentType("application/x-download");

        //response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
    }

    private boolean isIE(HttpServletRequest request) {
        return (request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0 || request
                .getHeader("USER-AGENT").toLowerCase().indexOf("rv:11.0") > 0) ? true : false;
    }

    public List<T> importXls(MultipartFile file, Class<T> clazz) throws Exception{
        ImportParams params = new ImportParams();
        params.setTitleRows(2);
        params.setHeadRows(1);
        params.setNeedSave(false);
        List<T> list = null;
        list = ExcelImportUtil.importExcel(file.getInputStream(), clazz, params);
        return list;
    }

    private static void writePicures(List<HSSFPictureData> pics, String picFold) {

    }


}
