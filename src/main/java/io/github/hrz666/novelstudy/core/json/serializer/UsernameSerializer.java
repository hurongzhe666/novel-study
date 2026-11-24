package io.github.hrz666.novelstudy.core.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * 用户名序列化器（敏感信息，不应该在页面上完全显示）
 * 将用户名序列化，中间值直接替换掉
 * 数据出去到前端的时候要经过这个序列化器，局部序列化器将用户名和手机号进行处理
 * 把用户名/手机号序列化成「前 4 位+****+后 4 位」脱敏输出，防止前端直接显示
 * @author hrz666
 * @date 2026/11/20
 */
public class UsernameSerializer extends JsonSerializer<String> {

    //JsonGenerator用来接受json数据，SerializerProvider用来接受提供服务
    @Override
    public void serialize(String s, JsonGenerator jsonGenerator,
        SerializerProvider serializerProvider) throws IOException {
        // 将用户名中间部分替换掉
        jsonGenerator.writeString(s.substring(0, 4) + "****" + s.substring(8));
    }

}
