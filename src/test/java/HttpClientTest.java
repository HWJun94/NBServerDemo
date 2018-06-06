import com.leisen.http.httpclient.HttpAPIImpl;

public class HttpClientTest {

    public static void main(String[] args) throws Exception{
        HttpAPIImpl httpAPIImpl = new HttpAPIImpl();
        httpAPIImpl.Auth();
        httpAPIImpl.deviceCommands("ecf8dedb-1c1f-4f33-8c45-81700ea53e36", "12345");
    }
}
