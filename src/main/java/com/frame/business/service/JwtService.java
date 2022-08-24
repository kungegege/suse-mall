package com.frame.business.service;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import com.frame.business.entity.Role;
import com.frame.business.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * @author: HP-zouKun
 * Date: 2022/3/12
 * Time: 23:44
 * Description:
 */
@Service
@Slf4j
public class JwtService {

    @Value("{jwt.secretKey:tyujhbn}")
    private String secretKey;

    @Value("jwt.issuer:zoukun")
    private String issuer;

    /**
     * token 有效时间
     */
    private Integer expireTime = 24;

    public String sign(Map<String, Object> map) {
        DateTime now = DateTime.now();
        JWT jwt = JWT.create()
                .setKey(secretKey.getBytes(StandardCharsets.UTF_8))
                .setIssuer(issuer);

        // 过期时间  6h
        map.put(JWTPayload.EXPIRES_AT, now.offsetNew(DateField.HOUR, expireTime));

        // 生效时间
        map.put(JWTPayload.NOT_BEFORE, now);

        //生效时间`
        map.put(JWTPayload.NOT_BEFORE, now);

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            jwt.setPayload(entry.getKey(), entry.getValue());
        }

        return jwt.sign();
    }

    public  boolean verify(String token) {
        JWTValidator.of(token).validateDate(DateUtil.date());
        return JWTUtil.verify(token, secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String genToken(User user) {
        return genToken(user, null);
    }

    public String genToken(User user, List<Role> roles) {
        HashMap<String, Object> map = new HashMap<>();

        map.put("id", user.getUserId());
        map.put("gender", user.getSex());
        map.put("pic", user.getPic());
        map.put("score", user.getScore());
        map.put("name", user.getNickName());

        // 添加角色消息
        if (roles != null && !roles.isEmpty()) {
            List<String> collect = roles.stream().map(i -> i.getRoleName()).collect(Collectors.toList());
            String res = StringUtils.join(collect, ",");
            map.put("roles", res);
        }

        String token = sign(map);
        log.info("generate token {} , 过期时间：{}", token, getExpireTime(token));
        return token;
    }

    public JSONObject parse(String token) {
        if (!verify(token)) {
            throw new IllegalArgumentException("token 非法");
        }
        JWT jwt = JWTUtil.parseToken(token);
        JWTPayload payload = jwt.getPayload();
        return payload.getClaimsJson();
    }

    public Object parse(String token, String key) {
        JSONObject jsonObject = parse(token);
        return jsonObject.get(key);
    }

    /**
     * 获取过期时间
     * @param token
     * @return
     */
    public Date getExpireTime(String token) {
        JWTPayload payload = JWT.of(token).getPayload();
        return payload.getClaimsJson().getDate(JWTPayload.EXPIRES_AT);
    }

    /**
     * 是否需要续期
     * @param token
     * @return
     */
    public boolean isExtendExpireTime(String token){
        // 检查当前是否过期 和 token 的合法性
        verify(token);

        DateTime centerTime = DateTime.now().offsetNew(DateField.HOUR, -expireTime / 2);
        // 如果超过令牌的有效时间的一半, 则进行续期
        if (getExpireTime(token).before(centerTime)) {
            return true;
        }
        return false;
    }


}
