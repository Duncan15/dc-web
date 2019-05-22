package format;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RespWrapper {
    public enum AnsMode {
        SUCCESS(0),SYSERROR(500);
        private int value;
        private AnsMode(int value){
            this.value=value;
        }
        public int getValue(){
            return this.value;
        }

    }
    public static ObjectMapper objectMapper = new ObjectMapper();

    private static Map<String,Object> generateBase(AnsMode ansMode) {
        Map<String,Object> ans = new HashMap<>();
        ans.put("errno", ansMode.getValue());
        return ans;
    }

    private static String writeToString(Map<String, Object> ans) {
        try {
            return objectMapper.writeValueAsString(ans);
        } catch (JsonProcessingException ex) {
            //ignored
            return "{\"errno\":500, \"errmsg\":\"JSON格式化错误\"}";
        }
    }

    /*
    生成返回结构型数据
     */
    public static String build(Object data){
        Map<String,Object> ans = generateBase(AnsMode.SUCCESS);
        ans.put("data",data);
        return writeToString(ans);
    }
    /*
    生成列表数据
     */
    public static String build(List list, int total){
        Map<String,Object> ans=generateBase(AnsMode.SUCCESS);
        Map<String,Object> data=new HashMap<>();
        data.put("content",list);
        data.put("total",total);
        ans.put("data",data);
        return writeToString(ans);
    }
    /*
    生成返回错误信息
     */
    public static String build(AnsMode ansMode,Object data){
        Map<String,Object> ans = generateBase(ansMode);
        ans.put("data", data);
        return writeToString(ans);
    }


}

