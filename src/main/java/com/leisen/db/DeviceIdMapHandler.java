package com.leisen.db;

import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DeviceIdMapHandler implements ResultSetHandler<Map<String, String>> {

    @Override
    public Map<String, String> handle(ResultSet resultSet) throws SQLException {
        Map<String, String> deviceIdMap = new HashMap<>();
        while(resultSet.next()) {
            String IMEI = resultSet.getString("IMEI");
            String deviceId = resultSet.getString("deviceId");
            deviceIdMap.put(deviceId, IMEI);
        }
        return deviceIdMap;
    }
}
