package com.leisen.db;

import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DeviceMapHandler implements ResultSetHandler<Map> {

    @Override
    public Map handle(ResultSet resultSet) throws SQLException {
        Map<String, String> deviceMap = new HashMap<>();
        while(resultSet.next()) {
            String IMEI = resultSet.getString("IMEI");
            String deviceId = resultSet.getString("deviceId");
            deviceMap.put(IMEI, deviceId);
        }
        return deviceMap;
    }
}
