package com.frame.business.service;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import toolgood.words.IllegalWordsSearch;
import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/3/3
 * Time: 9:57
 * Description:
 */
@Service
@Slf4j
public class SensitivewordFilter {

    @Deprecated
    private BloomFilter<String> bf;

    private String sensitivewordFile = "sensi_words.txt";

    private IllegalWordsSearch sensitiveWordFilter = new IllegalWordsSearch();

//    @PostConstruct
    public void initBloomFilter() throws IOException {
        log.info("init sensi_words to BloomFilter");
        // 创建布隆过滤器(默认3%误差)
        bf = BloomFilter.create(Funnels.stringFunnel(Charset.forName("utf8")), 200);

        ArrayList<String> list = new ArrayList<>();
        // 添加脏词
        InputStream inputStream = new ClassPathResource(sensitivewordFile).getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
            bf.put(line);
            list.add(line);
        }
        sensitiveWordFilter.SetKeywords(list);
    }


    public boolean check(String msg) {
        return sensitiveWordFilter.ContainsAny(msg);
    }

    public String replace(String msg, char c) {
        return sensitiveWordFilter.Replace(msg, c);
    }

}
