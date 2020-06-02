package com.yun.nls.utils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 视频处理
 *
 * @author zxcl07
 */
@Slf4j
@Component
public class FfmpegUtil {

    private static Logger logger = LoggerFactory.getLogger(FfmpegUtil.class);

    private static final boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

    @Value("${video.water.font.path}")
    private String videoWaterFontPath;

    @Value("${video.water.commend.path}")
    private String videoWaterCommendPath;

    //文字size
    private static final int FONT_SIZE = 24;
    //行距
    private static final int TEXT_Y_LENGTH = 28;
    //文字靠右距离
    private static final int TEXT_XY_LENGTH = 20;

    private static final String FONT_COLOR = "black";

    private static final String FONT_COLOR_WHITE = "white";

    /**
     * 组合添加水印的命令
     *
     * @param inFilePath
     * @param outFilePath
     * @param msg
     * @return
     */
    public List<String> getAddWaterCommend(String inFilePath, String outFilePath, List<String> msg) {
        String text = "";
        for (int i = 0; i < msg.size(); i++) {
            text = text + "[" + i + "]drawtext=fontfile='" + videoWaterFontPath + "':text='" + msg.get(i) + "':x=(w-tw-" + TEXT_XY_LENGTH + "):y=(h-" + (i + 1) * TEXT_Y_LENGTH + "):fontsize=" + FONT_SIZE + ":fontcolor=" + FONT_COLOR;
            if (i != msg.size() - 1) {
                text = text + "[" + (i + 1) + "];";
            }
        }

        // linuxlogo添加
//        String logoImg = "D:\\logo.png";
//        for (int i = 0; i < msg.size(); i++) {
//            text = text + "[" + i + "]drawtext=fontfile='" + videoWaterFontPath + "':text='" + msg.get(i) + "':x=(w-tw-" + TEXT_XY_LENGTH + "):y=(h-" + (i + 1) * TEXT_Y_LENGTH + "):fontsize=" + FONT_SIZE + ":fontcolor=" + FONT_COLOR;
//            if(i != msg.size() - 1){
//                text = text + "[" + (i + 1) + "];";
//            }
//            if (!StringUtils.isEmpty(logoImg) && i == msg.size() - 1) {
//                text = text +"[" + (i + 1) + "];movie='"+logoImg+"',scale=30:30[log];["+( i + 1 )+"][log]overlay=10:10";
//            }
//        }

        List<String> commend = new ArrayList<String>();
        commend.add("D:/ffmpeg-4.2.2-win64-shared/bin/ffmpeg.exe");
        commend.add("-i");
        commend.add(inFilePath);
        commend.add("-vf");
        commend.add(text);
        commend.add(outFilePath);
        logger.info("commend:{}", commend);
        return commend;
    }

    /**
     * 执行命令
     *
     * @param commend
     * @return
     */
    public static boolean runCommend(List<String> commend) {
        long l = System.currentTimeMillis();

        try {
            //windows系统
            if (isWindows) {
                ProcessBuilder builder = new ProcessBuilder();
                builder.command(commend);
                builder.redirectErrorStream(true);
                logger.info("开始执行命令");
                Process process = builder.start();
                InputStream in = process.getInputStream();
                byte[] bytes = new byte[1024];
                logger.info("正在进行水印，请稍候");
                while (in.read(bytes) != -1) {
                    //超过2*60秒停止执行
                    if (System.currentTimeMillis() - l > 2 * 60 * 1000) {
                        throw new RuntimeException();
                    }
                }
                return true;
            } else {
                String[] coms = (String[]) commend.toArray();
                logger.info("开始执行命令");
                Process pro = Runtime.getRuntime().exec(coms);
                pro.waitFor();
                InputStream in = pro.getInputStream();
                BufferedReader read = new BufferedReader(new InputStreamReader(in));
                String result = read.readLine();
                logger.info("执行结果:" + result);
                return true;
            }
        } catch (IOException e) {
            logger.info("执行失败", e);
        } catch (InterruptedException e) {
            logger.info("执行失败", e);
        } finally {
            logger.info("执行完成" + (System.currentTimeMillis() - l));
        }

        return false;
    }

    /**
     * 视频裁剪
     *
     * @param ffmepgPath
     * @param videoRealPath
     * @param imageRealName
     */
    public static void makeScreenCut(String ffmepgPath, String videoRealPath, String imageRealName) {
        List<String> commend = new ArrayList<String>();
        commend.add(ffmepgPath);
        commend.add("-i");
        commend.add(videoRealPath);
        commend.add("-y");
        commend.add("-f");
        commend.add("image2");
        commend.add("-ss");
        commend.add("8");
        commend.add("-t");
        commend.add("0.001");
        commend.add(imageRealName);

        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            builder.redirectErrorStream(true);
            System.out.println("视频截图开始...");

            Process process = builder.start();
            InputStream in = process.getInputStream();
            byte[] bytes = new byte[1024];
            System.out.print("正在进行截图，请稍候");
            while (in.read(bytes) != -1) {
                System.out.println(".");
            }
            System.out.println("");
            System.out.println("视频截取完成...");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("视频截图失败！");
        }
    }



    public static void command(List<String> commend) {
        long l = System.currentTimeMillis();
        String command = new String();
        for (int i = 0; i < commend.size(); i++) {
            if (i == 0) {
                command = command.concat(commend.get(i));
            } else {

                command = command.concat(" ").concat(commend.get(i));
            }
        }
        logger.info("开始执行命令:{}", command);

        try {
            log.info("开始执行命令");
            Process pro = Runtime.getRuntime().exec(command);
            pro.waitFor();
            InputStream in = pro.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(in));
            String result = read.readLine();
            log.info("执行结果:" + result);
            byte[] bytes = new byte[1024];
            logger.info("正在进行水印，请稍候");
            while (in.read(bytes) != -1) {
                //超过2*60秒停止执行
                if (System.currentTimeMillis() - l > 2 * 60 * 1000) {
                    throw new RuntimeException();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
