package com.hodo.iiot.group2.data.hdcloud.bank.account.bill.controller;

import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.util.DbUtil;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.util.jjUtil;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 获取九恒星数据
 */
@RestController
@AllArgsConstructor
@RequestMapping("/syncData")
@Api(value = "SyncDataController", tags = "九恒星数据同步")
public class SyncDataController {

    @RequestMapping(value = "/syncData", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("@pms.hasPermission('syncdata')")
    public List<List> syncDataPost(@RequestBody() Map<String, Object> params) throws UnsupportedEncodingException {

        String sql = jjUtil.handleParams(params, "sql");
//        sql =  URLDecoder.decode(sql,"utf-8");
        String query_begin = jjUtil.handleParams(params, "query_begin");
        String query_end = jjUtil.handleParams(params, "query_end");
//        System.out.println("----sql----"+sql);
//        System.out.println("----query_begin----"+query_begin);
//        System.out.println("----query_end----"+query_end);

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rsn = null;
        List<List> list = new ArrayList();
        try {
            conn = DbUtil.getConnection();
            //查询所有内部账单号然后拼接查询条件
            ps = conn.prepareStatement(sql);
            ps.setString(1, query_begin);
            ps.setString(2, query_end);
            rsn = ps.executeQuery();
            ResultSetMetaData md = rsn.getMetaData(); //获得结果集结构信息,元数据
            int columnCount = md.getColumnCount();   //获得列数

            while (rsn.next()) {
                List rowData = new ArrayList();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.add(rsn.getObject(i));
                    System.out.println(rsn.getObject(i));
                }
                list.add(rowData);

            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rsn != null) {
                try {
                    rsn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null)
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return list;
    }

    ;
}
