package com.yun.nls.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 语音识别
 *
 * @author zhouyaoming
 * @create 2020-05-10 15:40
 **/
@Slf4j
@RestController
public class NlsController {

    @RequestMapping("/nlsData")
    @ResponseBody
    public String grabOrderInfo(String data) {

        log.info("nlsData", data);
        return "ok";
    }
}
