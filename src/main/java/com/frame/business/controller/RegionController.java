package com.frame.business.controller;

import com.frame.utils.DataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/3/4
 * Time: 13:33
 * Description:
 */
@Controller
@RequestMapping("region")
public class RegionController {

    private final String LOCAL_REGION = "local_region";

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("")
    public String toPage() {
        return "region";
    }

    /**
     * 单位 km
     * @param distance
     * @param uid
     * @return
     */
    @GetMapping("/around/{distance}")
    @ResponseBody
    public DataResult getAround(@PathVariable Double distance, String uid) {
        Distance dis = new Distance(distance, RedisGeoCommands.DistanceUnit.KILOMETERS);
        GeoResults geoResults = redisTemplate.opsForGeo().geoRadiusByMember(LOCAL_REGION, uid, dis);

        // key uid,  value distance
        HashMap<String, String> map = new HashMap<>();
        List<GeoResult> list = geoResults.getContent();

        for (GeoResult<RedisGeoCommands.GeoLocation> result : list) {
            String name =(String)result.getContent().getName();
            map.put(name, getStringDistance(uid, name));
        }

        return DataResult.success(map);
    }

    /**
     * @param latitude  纬度
     * @param longitude 经度
     * @param uid      用户ID( TODO 根据Session获取UID)
     * @return
     */
    @GetMapping("/save")
    @ResponseBody
    public DataResult saveRegion(Double latitude, Double longitude,String uid) {
        Point point = new Point(longitude, latitude);
        RedisGeoCommands.GeoLocation<Object> location = new RedisGeoCommands.GeoLocation<>(uid, point);
        redisTemplate.opsForGeo().add(LOCAL_REGION, location);
        return DataResult.success();
    }

    /**
     * 获取距离 （km）
     * @return
     */
    @GetMapping("/distance/{tid}/{uid}")
    @ResponseBody
    public DataResult getDistance(@PathVariable String tid, @PathVariable String uid) {
        return DataResult.success(getStringDistance(tid,uid));
    }


    private String getStringDistance(String tid, String uid) {
        Distance distance = redisTemplate.opsForGeo().geoDist(LOCAL_REGION, tid, uid, RedisGeoCommands.DistanceUnit.KILOMETERS);
        return distance.getValue() + distance.getMetric().getAbbreviation();
    }

}
