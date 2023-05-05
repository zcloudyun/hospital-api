package com.example.hospital.api.socket;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
//用于维护websocket连接和请求，并且把已有的websocket连接缓存到map中，以便将来向浏览器推送消息的时候，
// 从map中取出websocket连接
@Slf4j
@ServerEndpoint(value = "/socket")
@Component
public class WebSocketService {
//    处于线程安全的考量，这里采用ConcurrentAHashMap
    public static ConcurrentHashMap<String, Session> sessionMap=new ConcurrentHashMap<>();

//    创建连接之后返回的回调函数
    @OnOpen
    public void onOpen(Session session){
        log.info("连接成功,{}",session);
    }
  //关闭连接之后执行的回调函数
    @OnClose
    public void onClose(Session session) {
        log.info("断开连接,{}",session);
        Map map=session.getUserProperties();
        if(map.containsKey("userId")){
            String userId= MapUtil.getStr(map,"userId");
            sessionMap.remove(userId);
        }
    }
//接收到消息后执行的回调函数
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("onMessage接收到的消息,{},{}",message,session);
//        接收到的字符串消息解析成JSON对象
        JSONObject json= JSONUtil.parseObj(message);
//        opt参数代表要执行的行为（自己约定的，属于自定义参数）
        String opt=json.getStr("opt");
        //        即便是websocket也会有超时时间，过来超时时间也会被服务端切断
//        为了不让服务端切断连接，浏览器会自动定时发送心跳请求

        String token=json.getStr("token");
//        因为不是http请求，无法获取请求头数据，所以需要特殊手段从Token解析出userId
        String userId= StpUtil.stpLogic.getLoginIdByToken(token).toString();
        Map map=session.getUserProperties();
        if(!map.containsKey("userId")){
//            把userId缓存到websocket的session中，给onClose函数使用
            map.put("userId",userId);
        }
//        由于无法分清当前的session是之前连接的，还是断连后新建的
//        所以每次收到信息都把session缓存到Map中，因为当前的连接肯定是有效的
        if(sessionMap.containsKey(userId)){
            sessionMap.replace(userId,session);
        }
        else{
            sessionMap.put(userId,session);
        }
        if("ping".equals(opt)){
//            如果是心跳请求就无需处理
            return;
        }
        //给浏览器返回应答信息
        sendInfo("ok",userId);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误", error);
    }

    public static void sendInfo(String message,String userId){
        log.info("sendInfo接收到的数据为,{},{}",message,userId);
        if(StrUtil.isNotBlank(userId)&&sessionMap.containsKey(userId)){
            Session session=sessionMap.get(userId);
            sendMessage(message,session);
        }
    }

    private static void sendMessage(String message,Session session){
        log.info("sendMessage接收到的数据为,{},{}",message,session);
        try {
            session.getBasicRemote().sendText(message);
        }catch (Exception e){
            log.error("执行异常",e);
        }
    }
}