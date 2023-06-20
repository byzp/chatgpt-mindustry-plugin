
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import arc.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.mod.*;
import mindustry.net.Administration.*;
import mindustry.world.blocks.storage.*;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PrivateApi extends Plugin{
public static String message="";

    @Override
    public void registerClientCommands(CommandHandler handler){
        
        //register a simple reply command
        handler.<Player>register("c", "<text...>", "chat.", (args, player) -> {
            Thread newThread = new Thread(() -> {
                player.sendMessage(">"+args[0]);
                player.sendMessage("chat:" + post(args[0]));
            });
            newThread.start();
        });
    }
    
    
    public static String post(String text){
        String pathUrl="https://linglu.pro/api/generate";
        OutputStreamWriter out = null;
        BufferedReader br = null;
        String result = "";
        try {
            URL url = new URL(pathUrl);
            //打开连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //请求方式
            conn.setRequestMethod("POST");
            //请求头
            conn.setRequestProperty("Referer","https://linglu.pro/zh");
            conn.setRequestProperty("Referrer-Policy","strict-origin-when-cross-origin");
            //DoOutput设置是否向httpUrlConnection输出，DoInput设置是否从httpUrlConnection读入，此外发送post请求必须设置这两个
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 使用 Java 内置的 JSONObject 类，创建一个 JSON 对象
            JSONObject data = new JSONObject();

            // 将需要添加的属性逐一加入到 JSON 对象中
            data.put("prompt", text);
            /*
            JSONObject bodyJson = new JSONObject();
            bodyJson.put("prompt", text);
            data.put("body", bodyJson);
            */
            System.out.println(data);
           
            //获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            //发送请求参数即数据
            out.write(data.toString());
            //flush输出流的缓冲
            out.flush();
            //获取URLConnection对象对应的输入流
            InputStream is = conn.getInputStream();
            //构造一个字符流缓存
            br = new BufferedReader(new InputStreamReader(is));
            String str = "";
            while ((str = br.readLine()) != null){
                result += str;
            }
            
            //System.out.println(result);
            //关闭流
            is.close();
            //断开连接，disconnect是在底层tcp socket链接空闲时才切断，如果正在被其他线程使用就不切断。
            conn.disconnect();
            
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (out != null){
                    out.close();
                }
                if (br != null){
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /*
        String ooo;
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(result);
            String content = rootNode.get("choices").get(0).get("message").get("content").asText();
            ooo=content;
        } catch (Exception e) {
            ooo=e.getMessage();
        }
        */
        System.out.println(result);
        return result;
    }

}