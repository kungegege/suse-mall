package com.frame.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/5/3
 * Time: 19:44
 * Description:
 */
@Configuration
@ConfigurationProperties(prefix = "pay.ali")
@Data
public class PayConfig {

    private String appId;

    private String appPrivateKey;

    private String charset;

    private String alipayPublicKey;

    private String signType;

    private String notifyUrl;

    private String returnUrl;

    private String serverUrl;

    @Bean
    public AlipayClient alipayClient() {
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(
                serverUrl, appId, appPrivateKey, "json", charset, alipayPublicKey, signType);
        return alipayClient;
    }

    @Bean
    @Scope(value = "prototype")
    @Deprecated
    public AlipayTradeWapPayRequest alipayTradeWapPayRequest() {
        AlipayTradeWapPayRequest alipayTradeWapPayRequest = new AlipayTradeWapPayRequest();
//        alipayTradeWapPayRequest.setNotifyUrl(notifyUrl);
//        alipayTradeWapPayRequest.setReturnUrl(returnUrl);
        return alipayTradeWapPayRequest;
    }

    @Data
    public class PayOrder {
        // 商户订单号(必填)
        private String orderId;
        // 订单名称(必填)
        private String subject;
        // 付款金额(必填)
        private String totalAmount;
        // 商品描述
        private String body;
        // 超时时间
        private String timeoutExpress="1m";
        // 销售产品码(必填)
        private String productCode;
    }
}
