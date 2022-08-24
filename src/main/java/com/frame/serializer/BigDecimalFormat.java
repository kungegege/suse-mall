package com.frame.serializer;

import com.alibaba.fastjson.serializer.JSONSerializableSerializer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/5/8
 * Time: 11:13
 * Description:
 */
public class BigDecimalFormat extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal bigDecimal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (bigDecimal == null) {
            jsonGenerator.writeString("0");
        }
        BigDecimal decimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        jsonGenerator.writeString(decimal + "");
    }
}
