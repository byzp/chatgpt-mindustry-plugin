

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OfficialApi extends Plugin{

public static String api_key="";  //Enter the api key here.  在这里输入api key。

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
        String pathUrl="https://api.openai.com/v1/chat/completions";
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
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Authorization","Bearer "+api_key);
            //DoOutput设置是否向httpUrlConnection输出，DoInput设置是否从httpUrlConnection读入，此外发送post请求必须设置这两个
            conn.setDoOutput(true);
            conn.setDoInput(true);
            String data="{\"model\": \"gpt-3.5-turbo\",\"messages\": [{\"role\": \"user\", \"content\": \""+text+"\"}], \"temperature\": 0.7}";
            /*
            JSONObject data = new JSONObject();
            data.put("model","gpt-3.5-turbo");
            JSONObject hst = new JSONObject();
            hst.put("role","user");
            hst.put("content",text);
            ArrayList mes =new ArrayList<>();
            mes.add(hst);
            data.put("messages",mes);
            data.put("temperature",0.7);
            */

            /**
             * 下面的三句代码，就是调用第三方http接口
             */
            //获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            //发送请求参数即数据
            out.write(data);
            //flush输出流的缓冲
            out.flush();

            /**
             * 下面的代码相当于，获取调用第三方http接口后返回的结果
             */
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
        String ooo;
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(result);
            String content = rootNode.get("choices").get(0).get("message").get("content").asText();
            ooo=content;
        } catch (Exception e) {
            ooo=e.getMessage();
        }
        return ooo;
    }

}