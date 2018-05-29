//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.io.IOException;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.*;
//
//public class JacksonTest {
//
//    public static void main(String[] args) throws Exception{
//        User user=new User();
//        user.setId("01");
//        user.setName("张三");
//        user.setAge(33);
//        user.setPay(6666.88);
//        user.setValid(true);
//        user.setOne('E');
//        user.setBirthday(new Date(20l*366*24*3600*1000)); //1990年
//
//        Map map=new HashMap();
//        map.put("aa", "this is aa");
//        map.put("bb", "this is bb");
//        map.put("cc", "this is cc");
//        user.setMap(map);
//
//        List list=new ArrayList(){};
//        list.add("普洱");
//        list.add("大红袍");
//        user.setList(list);
//
//        Set set=new HashSet();
//        set.add("篮球");
//        set.add("足球");
//        set.add("乒乓球");
//        user.setSet(set);
//
//        ObjectMapper mapper = new ObjectMapper();
//        String json = mapper.writeValueAsString(user);
//        System.out.println(json);
//        Map map1 = mapper.readValue(json, Map.class);
//        System.out.println(map1.toString());
//    }
//}
