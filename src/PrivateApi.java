
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
import org.json.JSONObject;

public class PrivateApi extends Plugin{
public static String message="users:你好\nAI:你好，需要帮助吗？\n";
public static int max_len=400;//经过粗略测试极限约800字符

    @Override
    public void registerClientCommands(CommandHandler handler){
        
        //register a simple reply command
        handler.<Player>register("c", "<content...>", "chat.", (args, player) -> {
            Thread newThread = new Thread(() -> {
                Call.sendMessage(">"+args[0]);
                Call.sendMessage(post(args[0]));
            });
            newThread.start();
        });
    }
    
    
    public String post(String content){
        if(content.equals("重置会话")){
            message="users:你好\nAI:你好，需要帮助吗？\n";
            return "会话已重置";
        }
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
            for(int num=0;((message+"users:"+content+"\n").substring(num)).length()>max_len;num++){
                if((message+"users:"+content+"\n").substring(num,num+6).equals("users:")){
                    message=message.substring(num);
                }
            }
            /*
            while(length(message+"users:"+content+"\n")>400){
                if(
                substring(1);
            }*/
            // 将需要添加的属性逐一加入到 JSON 对象中
            data.put("prompt", message+"users:"+content+"\n");
            /*
            JSONObject bodyJson = new JSONObject();
            bodyJson.put("prompt", content);
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
            System.out.println(result);
            message+="users:"+content+"\n"+result+"\n";

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            result="出错了，请重试";
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
        
        return result;
    }

}