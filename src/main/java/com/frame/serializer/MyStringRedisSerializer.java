/*
 * Copyright 2011-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.frame.serializer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Simple {@link java.lang.String} to {@literal byte[]} (and back) serializer. Converts {@link java.lang.String Strings}
 * into bytes and vice-versa using the specified charset (by default {@literal UTF-8}).
 * <p>
 * Useful when the interaction with the Redis happens mainly through Strings.
 * <p>
 * Does not perform any {@literal null} conversion since empty strings are valid keys/values.
 *
 * @author Costin Leau
 * @author Christoph Strobl
 * @author Mark Paluch
 */
public class MyStringRedisSerializer implements RedisSerializer<Object> {

    private final Charset charset;

    public static final MyStringRedisSerializer US_ASCII = new MyStringRedisSerializer(StandardCharsets.US_ASCII);
    public static final MyStringRedisSerializer ISO_8859_1 = new MyStringRedisSerializer(StandardCharsets.ISO_8859_1);
    public static final MyStringRedisSerializer UTF_8 = new MyStringRedisSerializer(StandardCharsets.UTF_8);
    public MyStringRedisSerializer() {
        this(StandardCharsets.UTF_8);
    }

    public MyStringRedisSerializer(Charset charset) {
        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }

    @Override
    public String deserialize(@Nullable byte[] bytes) {
        return (bytes == null ? null : new String(bytes, charset));
    }


    @Override
    public byte[] serialize(@Nullable Object object) {
        if(object==null){
            return new byte[0];
        }
        if(object instanceof String){
            return object.toString().getBytes(charset);
        }else {
            String str= JSON.toJSONString(object);
            return str.getBytes(charset);
        }

    }
}
