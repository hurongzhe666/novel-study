package io.github.hrz666.novelstudy.core.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;


/**
 * JSON 全局反序列化器
 *
 * @author hrz666
 * @date 2026/11/20
 */
@JsonComponent //自动把这个反序列化器注册给 Jackson，全局生效——所有请求里 JSON → Java 的字符串字段，转换时都要过它一道。
    public class GlobalJsonDeserializer {

    /**
     * 字符串反序列化器：过滤特殊字符，解决 XSS 攻击
     */
    public static class StringDeserializer extends JsonDeserializer<String> {

        @Override
        public String deserialize(JsonParser jsonParser,
            DeserializationContext deserializationContext) throws IOException {
            return jsonParser.getValueAsString()
                .replace("<", "&lt;")
                .replace(">", "&gt;");
        }
    }
}
