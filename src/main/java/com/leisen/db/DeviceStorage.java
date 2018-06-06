package com.leisen.db;

import com.leisen.util.JDBCUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DeviceStorage {
    private static Map<String, String> deviceIMEIMap = new HashMap();
    private static Map<String, String> deviceIdMap = new HashMap<>();

    public DeviceStorage() {
//        deviceIMEIMap = new HashMap();
    }

    /**
     * 从数据库中将deviceId和IMEI加载到map中
     * @throws SQLException
     */
    public void loadFromDB() throws SQLException {
        Connection conn = JDBCUtil.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        String sql = "select * from deviceinfo";
        try {
            deviceIMEIMap = queryRunner.query(conn, sql, new DeviceIMEIMapHandler());
            deviceIdMap = queryRunner.query(conn, sql, new DeviceIdMapHandler());
        } finally {
            DbUtils.close(conn);
        }
    }

    public void reloadFromDB() throws SQLException {
        this.loadFromDB();
    }
    /**
     * 插入一条数据
     * @param IMEI
     * @param deviceId
     * @throws SQLException
     */
    public void writeToDB(String IMEI, String deviceId) throws SQLException{
        Connection conn = JDBCUtil.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        String sql = "insert into deviceinfo (IMEI,deviceId) values (?,?)";
        try {
          queryRunner.update(conn, sql, IMEI, deviceId);
        } finally {
            DbUtils.close(conn);
            deviceIMEIMap.put(IMEI, deviceId);//直接在map中添加新设备
            deviceIdMap.put(deviceId, IMEI);
        }
    }

    /**
     * 批量插入
     * @param paras
     * @throws SQLException
     */
    public void writeToDBBatched(Object[][] paras) throws SQLException{
        Connection conn = JDBCUtil.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        String sql = "insert into deviceinfo values(?,?)";
        try {
            queryRunner.batch(conn, sql, paras);
        } finally {
            DbUtils.close(conn);
            this.reloadFromDB(); //批量插入结束后，重新加载缓存中的表
        }
    }

    public Map<String, String> getDeviceIMEIMap() {
        return deviceIMEIMap;
    }

    public Map<String, String> getDeviceIdMap() {
        return deviceIdMap;
    }
}
