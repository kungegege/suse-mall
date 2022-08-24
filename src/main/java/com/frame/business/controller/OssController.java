package com.frame.business.controller;

import cn.hutool.core.util.IdUtil;
import com.frame.business.base.BaseController;
import com.frame.business.service.OssService;
import com.frame.utils.DataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/3/18
 * Time: 10:54
 * Description:
 */
@RestController
public class OssController extends BaseController {

    @Autowired
    private OssService ossService;

    /**
     * 单文件上传
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @RequestMapping("/upload")
    public DataResult<String> uploadFile(@RequestParam(value = "file") MultipartFile multipartFile) throws IOException {

        InputStream inputStream = multipartFile.getInputStream();
        String link =  ossService.upload(inputStream, genFileByTime(multipartFile));
        return DataResult.success(link);
    }

    /**
     * 多文件上传
     * @param files
     * @return
     * @throws IOException
     */
    @PostMapping("/upload/batch")
    public DataResult<List> uploadFileBatch(List<MultipartFile> files) throws IOException {
        List<String> res = new ArrayList<>();
        for (MultipartFile multipartFile : files) {
            String url = uploadFile(multipartFile).getData();
            res.add(url);
        }
        return DataResult.success(res);
    }
    private  String getSuffix(String file) {
        int index = file.lastIndexOf(".");
        return file.substring(index);
    }

    private String genPathByTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM");
        String format = dateFormat.format(new Date());
        return format;
    }

    private String genFileByTime(MultipartFile multipartFile) {
        String filename = multipartFile.getOriginalFilename();
        String suffix = getSuffix(filename);
        String res = genPathByTime() + "/" + IdUtil.simpleUUID() + suffix;
        return res;
    }

    private String genFileByTime(String filename) {
        String suffix = getSuffix(filename);
        String res = genPathByTime() + "/" + IdUtil.simpleUUID() + suffix;
        return res;
    }
}
