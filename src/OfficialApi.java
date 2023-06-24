
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
import org.json.JSONObject;

public class OfficialApi extends Plugin{

public static String api_key="在这里填入api_key";  //api key
public static double temperature=0.7; //对话温度
public static String model="gpt-3.5-turbo";  //模型
public static String pathUrl="https://api.openai.com/v1/chat/completions"; //访问地址
public static int max_history_len=800;//最大记忆长度(字符数)，不建议超过3000

//public static ArrayList<String> mess = new ArrayList<String>();
public static ArrayList mess = new ArrayList<>();
public static int Initialization=1;
public static int len=0;
public static ArrayList len_list = new ArrayList<>();
    @Override
    public void registerClientCommands(CommandHandler handler){

        //register a simple reply command
        handler.<Player>register("c", "<content...>", "chat.", (args, player) -> {
            Thread newThread = new Thread(() -> {
                Call.sendMessage(">"+args[0]);
                Call.sendMessage("Assistant:" + post(args[0]));
            });
            newThread.start();
        });
    }

    public void cz(){
        for(int len = mess.size()-1; len>=0; len--){  
            mess.remove(len);
        }
    }
    
    public String post(String content){
        if(content.equals("重置会话")){
            cz();
            len=0;
            return "会话已重置";
        }
        while(len>max_history_len){
            int mess_size=mess.size();
            int len_list_size=len_list.size();
            len-=(int)len_list.get(len_list_size-1)+(int)len_list.get(len_list_size-2);
            mess.remove(mess_size-1);
            mess.remove(mess_size-2);
            len_list.remove(len_list_size-1);
            len_list.remove(len_list_size-2);
        }
        OutputStreamWriter out = null;
        BufferedReader br = null;
        String result = "";
        String ooo = "";
        JSONObject data = new JSONObject();
        JSONObject mes = new JSONObject();
        JSONObject mes1 = new JSONObject();
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
            
            mes.put("role","user");
            mes.put("content",content);
            
            mess.add(mes);
            data.put("model",model);
            data.put("temperature",temperature);
            data.put("messages",mess);
            System.out.println(data);
            
            //获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            //发送请求参数即数据
            out.write(data.toString());
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
            
            System.out.println(result);
            //关闭流
            is.close();
            //断开连接，disconnect是在底层tcp socket链接空闲时才切断，如果正在被其他线程使用就不切断。
            conn.disconnect();
            
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(result);
                ooo = rootNode.get("choices").get(0).get("message").get("content").asText();
                mes1.put("role","assistant");
                mes1.put("content",ooo);
                mess.add(mes1);
                len+=content.length()+ooo.length();
                len_list.add(content.length()+ooo.length());
            } catch (Exception e) {
                e.printStackTrace();
                mess.remove(mess.size()-1);
                ooo="出错了，请重试";
            }

        } catch (Exception e) {
            e.printStackTrace();
            mess.remove(mess.size()-1);
            ooo="出错了，请重试";
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
                mess.remove(mess.size()-1);
                ooo="出错了，请重试";
            }
        }

        return ooo;
    }

}