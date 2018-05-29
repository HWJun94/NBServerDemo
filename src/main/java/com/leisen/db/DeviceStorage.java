package com.leisen.db;

import com.leisen.util.JDBCUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DeviceStorage {
    private static Map deviceMap = new HashMap();

    public DeviceStorage() {
//        deviceMap = new HashMap();
    }

    public void loadFromDB() throws SQLException {
        Connection conn = JDBCUtil.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        String sql = "select * from deviceinfo";
        try {
            deviceMap = queryRunner.query(conn, sql, new DeviceMapHandler());
        } finally {
            DbUtils.close(conn);
        }
    }

    public Map getDeviceMap() {
        return deviceMap;
    }
}
