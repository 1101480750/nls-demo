package com.yun.nls.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouyaoming
 * @version 1.0.0
 * @date 2020/4/22 15:58
 */
@Slf4j
public class Test {



    public static void main(String[] args) throws UnsupportedEncodingException {
        List<String> msg = new ArrayList<>();
        FfmpegUtil ffmpegUtil = new FfmpegUtil();
        msg.add("zhongguo ");
        msg.add("begin to print water");
//        FfmpegUtil.addWater("D:\\ffmpeg-4.2.2-win64-shared\\bin\\ffmpeg.exe", "https://checkcar-1254328930.cos.ap-chengdu.myqcloud.com/25d475872890e225355ab1c67481c136.mp4", "D:\\120.mp4");
        FfmpegUtil.runCommend(ffmpegUtil.getAddWaterCommend("https://checkcar-1254328930.cos.ap-chengdu.myqcloud.com/25d475872890e225355ab1c67481c136.mp4", "D:\\".concat(UUID.randomUUID().toString().replace("-", "").concat(".mp4")), msg));


    }

    /**
     * 音频转码
     *
     * @param inFilePath
     * @param outFilePath
     * @param msg
     * @return
     */
    public static void getSoundToWav(String inFilePath, String outFilePath, List<String> msg) {

        String command = "";
        // 将mp3转换成wav 采样率16000 16kbs wav音频文件
        // FFMPEG_PATH ffmpeg路径 如：D:/ffmpeg/bin/ffmpeg.exe
        command = MessageFormat.format("{0} -i {1} -ar 16000 -acodec pcm_s16le {2}", "D:/ffmpeg-4.2.2-win64-shared/bin/ffmpeg.exe",
                "D:\\opt\\tmp_8753e8de4ce06c7e1476511180f965e7bd6661e4f699c76e.mp3", "D:/opt/1101480750.wav");
//        execCommand(command, 10000);
        try {
            log.info("开始执行命令");
            Process pro = Runtime.getRuntime().exec(command);
            pro.waitFor();
            InputStream in = pro.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(in));
            String result = read.readLine();
            log.info("执行结果:" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
