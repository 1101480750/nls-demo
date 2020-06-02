package com.yun.nls.socket;

import com.alibaba.fastjson.JSONObject;
import com.yun.nls.base.SpeechTranscriberDemo;
import com.yun.nls.form.AjaxResultDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;
import sun.misc.BASE64Decoder;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.*;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author zhouyaoming
 * @version 1.0.0
 * @date 2020/5/6 14:50
 */
@Slf4j
@Component
@ServerEndpoint("/websocket")
public class WebSocketServer {

    private  static final String appKey = "XKz6zUHAj7U6WwYy";
    private  static final String id = "LTAI4G9V8w35YuSt8phKtZrp";
    private  static final String secret = "THYsgtv2TsD8ShdjxCxvFUvWuuV4JM";

    private Session session;

    private static ArrayList<WebSocketServer> webSocketUtils  = new ArrayList<>();

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();


    @OnMessage
    public void onMessage(String message,Session session){
        System.out.print("数据：" + message);
        String imageBase64 = message.replace("data:mp3/wav;base64=", "");

        String inFile ="D:/opt/".concat(UUID.randomUUID().toString()).concat(".mp3");
        String outFilePath = "D:/opt/".concat(UUID.randomUUID().toString()).concat(".wav");
        decoderBase64File(imageBase64, inFile);


        String command = "";
        // 将mp3转换成wav 采样率16000 16kbs wav音频文件
        command = MessageFormat.format("{0} -i {1} -ar 16000 -acodec pcm_s16le {2}", "D:/ffmpeg-4.2.2-win64-shared/bin/ffmpeg.exe",
                inFile, outFilePath);
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

        SpeechTranscriberDemo demo = new SpeechTranscriberDemo(appKey, id, secret, "");
        demo.process(outFilePath);
        demo.shutdown();
    }

    @OnOpen
    public void onOpen(Session session){
        webSocketSet.add(this);     //加入set中
        log.info("有新窗口开始监听:当前在线人数为" + webSocketSet.size());
        webSocketUtils.add(this);
        this.session = session;
        System.out.println("client connected");
    }

    @OnClose
    public void onClose(){
        webSocketUtils.remove(this);
        webSocketSet.remove(this);     //加入set中
        log.info("有新窗口开始监听:当前在线人数为" + webSocketSet.size());
        System.out.println("Client close");
    }

    @OnError
    public void onError(Throwable t){
        System.out.println("no client");
    }

    /**
     * 	发送到坐席
     * */
    public static void sendInfoToWorkbench( AjaxResultDto msgDto){
        log.info("推送消息内容:"+ JSONObject.toJSONString(msgDto));
        sendInfo(JSONObject.toJSONString(msgDto));
    }

    public static void sendInfo(String eventMessage){
        for (WebSocketServer item:webSocketSet){
            try {
                item.session.getAsyncRemote().sendText(eventMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 将base64字符解码保存文件
     * @param base64Code
     * @param targetPath
     * @throws Exception
     */

    public void decoderBase64File(String base64Code, String targetPath){
        log.info("文件路径：" , targetPath);
        FileOutputStream out = null;
        try {
            byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
            out = new FileOutputStream(targetPath);
            out.write(buffer);
            out.close();
        }catch (Exception e){
            log.info("保存文件异常");
        }finally {
        }
    }
}
