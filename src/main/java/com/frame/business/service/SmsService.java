package com.frame.business.service;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.frame.exception.BusinessException;
import com.frame.exception.code.BaseResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/3/13
 * Time: 11:00
 * Description: 短信服务
 */
@Service
@Slf4j
public class SmsService {

    @Value("${ali.sms.sign-name}")
    private String signName;

    @Value("${ali.sms.template-code}")
    private String templateCode;

    @Autowired
    private Client smsClient;

    public boolean sendMsgCode(String phoneNumber, String code) {
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setPhoneNumbers(phoneNumber)
                .setSignName(signName)
                .setTemplateCode(templateCode)
                .setTemplateParam("{'code':" + code + "}");

        SendSmsResponse response = null;
        try {
            response = smsClient.sendSms(sendSmsRequest);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        String responseCode = response.getBody().getCode();
        log.info("send msg response: {},{}", response.getBody().getMessage(), responseCode);
        if (responseCode.equalsIgnoreCase("isv.MOBILE_NUMBER_ILLEGAL")) {
            // 手机号码格式错误
            throw new BusinessException(BaseResponseCode.PHONE_NUMBER_ERROR);
        } else if (responseCode.equalsIgnoreCase("isv.BUSINESS_LIMIT_CONTROL")) {
            throw new BusinessException(BaseResponseCode.CODE_SEND_ERROR);
        }
        return true;
    }

}
