package com.frame.business.service;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/3/18
 * Time: 10:37
 * Description:
 */
@Service
public class OssService {

    @Value("${ali.oss.endpoint}")
    private String endpoint;

    @Value("${ali.oss.bucket-name}")
    private String bucketName;

    @Value("${ali.access-key-id}")
    private String accessKeyId;

    @Value("${ali.access-key-secret}")
    private String accessKeySecret;

    public OSS getOssClient() {
        // 创建ClientConfiguration实例，您可以根据实际情况修改默认参数。
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        // 设置是否支持CNAME。CNAME用于将自定义域名绑定到目标Bucket。
        conf.setSupportCname(false);
        // 创建OSSClient实例。
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret, conf);
    }

    public String upload(InputStream inputStream, String fileName) {
        OSS client = getOssClient();
        PutObjectRequest putObject = new PutObjectRequest(bucketName, fileName, inputStream);
        client.putObject(putObject);
        client.shutdown();
        return genOnlineUri(fileName);
    }

    private String genOnlineUri(String fileName) {
        return "https://" + bucketName + "." + endpoint + "/" + fileName;
    }
}
