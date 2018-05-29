import com.leisen.db.DeviceStorage;

import java.util.Map;

public class DBTest {

    public static void main(String[] args) throws Exception{
        DeviceStorage deviceStorage = new DeviceStorage();
        deviceStorage.loadFromDB();
        System.out.println(deviceStorage.getDeviceMap());
    }
}
