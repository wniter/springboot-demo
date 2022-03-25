package com.example.netty.protocol;


import com.example.common.util.JsonUtil;
import lombok.Data;

/**
 * JSON序列化与反序列化演示案例
 */
@Data
public class JsonMsg {
    //id Field(域)
    private int id;
    //content Field(域)
    private String content;

    //在通用方法中，使用阿里FastJson转成Java对象
    public static JsonMsg parseFromJson(String json) {
        return JsonUtil.jsonToPojo(json, JsonMsg.class);
    }

    //在通用方法中，使用谷歌Gson转成字符串
    public String convertToJson() {
        return JsonUtil.pojoToJson(this);
    }

}
