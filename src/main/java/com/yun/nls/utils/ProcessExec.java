package com.yun.nls.utils;

import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProcessExec {

    private Process process;

    public void execute(Map<String,String> dto)
    {
        StringBuffer waterlogo = new StringBuffer();
        waterlogo.append("-i ");
        if(null!=dto.get("input_path")&& !StringUtils.isEmpty(dto.get("input_path"))){
            waterlogo.append(dto.get("input_path"));
        }
        waterlogo.append("  -vf \"movie=");
        if (null!=dto.get("logo")&&!StringUtils.isEmpty(dto.get("logo"))){
            waterlogo.append(dto.get("logo"));
        }
        waterlogo.append(",scale= 60: 30");
        waterlogo.append(" [watermark]; [in][watermark] overlay=main_w-overlay_w-10:main_h-overlay_h-10 [out]\" ");
        if (null!=dto.get("video_converted_path")&&!StringUtils.isEmpty(dto.get("video_converted_path"))){
            waterlogo.append(dto.get("video_converted_path"));
        }
        System.out.println(waterlogo);
        Runtime run = Runtime.getRuntime();
        String ffmegPath = null;
        if (!StringUtils.isEmpty(dto.get("ffmpeg_path"))){
            ffmegPath = dto.get("ffmpeg_path");
        }
// 执行命
        try {
            java.lang.Process process = run.exec(ffmegPath+waterlogo);
// 异步读取输出
            InputStream inputStream = process.getInputStream();
            InputStream errorStream = process.getErrorStream();
                    /* BufferedReader br=new BufferedReader(new InputStreamReader(inputStream,"gbk"));
                     String str1="";
                     while((str=br.readLine())!=null){
                         System.out.println(str1);
                     }*/

            ExecutorService service = Executors.newFixedThreadPool(2);

            ResultStreamHandler inputStreamHandler = new ResultStreamHandler(inputStream);
            ResultStreamHandler errorStreamHandler = new ResultStreamHandler(errorStream);

            service.execute(inputStreamHandler);
            service.execute(errorStreamHandler);

            process.waitFor();
            service.shutdownNow();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
