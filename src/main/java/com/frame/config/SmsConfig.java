package com.frame.config;

import com.aliyun.dysmsapi20170525.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.aliyun.teaopenapi.models.*;
/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/3/13
 * Time: 11:08
 * Description:
 */
@Configuration
public class SmsConfig {

    @Value("${ali.access-key-id}")
    private String accessKeyId;

    @Value("${ali.access-key-secret}")
    private String accessKeySecret;

    @Bean
    public Client smsClient() throws Exception {
        Config config = new Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new Client(config);
    }
}
