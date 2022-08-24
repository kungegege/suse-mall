package com.frame.business.service;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/3/3
 * Time: 9:41
 * Description:     字符串，布隆过滤器
 */
@Service
public class BloomStringFilterService {

    private BloomFilter<String> bf;

    @PostConstruct
    public void initBloomFilter() {
        // 创建布隆过滤器(默认3%误差)
        bf = BloomFilter.create(Funnels.stringFunnel(Charset.forName("utf8")), 12);
    }

    public void addWords(String word) {
        bf.put(word);
    }

    public boolean check(String word) {
        return bf.mightContain(word);
    }

}
