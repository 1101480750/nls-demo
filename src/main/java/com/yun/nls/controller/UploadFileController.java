package com.yun.nls.controller;

import com.yun.nls.base.SpeechTranscriberDemo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

@Slf4j
@RestController
@RequestMapping("/upload")
public class UploadFileController {


    private static final String appKey = "XKz6zUHAj7U6WwYy";
    private static final String id = "LTAI4G9V8w35YuSt8phKtZrp";
    private static final String secret = "THYsgtv2TsD8ShdjxCxvFUvWuuV4JM";

    private static final Logger LOG = LoggerFactory
            .getLogger(UploadFileController.class);

    @RequestMapping(value = "/wx_SubjectRecordKeeping", produces = "application/json")
    @ResponseBody
    public Object wx_SubjectRecordKeeping(HttpServletRequest request,
                                          @RequestParam("file") MultipartFile file) {
        LOG.info("进入上传...");

        // 设置用户信息
        String path = "D:\\opt\\";
        log.info("版本上传的路径:" + path);
        String fileName = file.getOriginalFilename();
        File targetFile = new File(path, fileName);
        log.info("版本上传的文件名:" + fileName);
        File targetPath = new File(path);
        if (!targetPath.exists()) {
            targetPath.mkdirs();
        }
        // 保存
        try {
            file.transferTo(targetFile);
            log.info("文件路径：" + path.concat(fileName));
            String url = "";
            String filepath = "E:\\sccd.wav";
            filepath = path.concat(fileName);
            String message = SpeechTranscriberDemo.GetImageStr(filepath);
            SpeechTranscriberDemo demo = new SpeechTranscriberDemo(appKey, id, secret, url);
            demo.process(filepath);
            demo.shutdown();
        } catch (Exception e) {
            log.error("保存文件失败", e);
            log.error("异常", e);
        }

        return "success";
    }
}
