/*
 * *
 *  * blog.coder4j.cn
 *  * Copyright (C) 2016-2019 All Rights Reserved.
 *
 */
package com.example.hospital.api.socket;
import com.alibaba.fastjson.JSON;
import com.example.hospital.api.db.Entity.Message;
import com.example.hospital.api.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author buhao
 * @version MyWSHandler.java, v 0.1 2019-10-17 17:10 buhao
 */
@Component
public class HttpAuthHandler extends TextWebSocketHandler {

    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private MessageService messageService;

    /**
     * socket 建立成功事件
     *
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Object token = session.getAttributes().get("token");
        if (token != null) {
            // 用户连接成功，放入在线用户缓存
            WsSessionManager.add(token.toString(), session);
        } else {
            throw new RuntimeException("用户登录已经失效!");
        }
    }

    /**
     * 接收消息事件
     *
     * @param session
     * @param textMessage
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        // 获得客户端传来的消息
        String payload = textMessage.getPayload();
        Object token = session.getAttributes().get("token");
        System.out.println("server 接收到 " + token + " 发送的: " + payload);
        Message message = JSON.parseObject(payload, Message.class);
        message.setCreateTime(LocalDateTime.now());
        // 先保存到数据库
        this.messageService.save(message);

        // 推送消息:
        TextMessage pushMessage = new TextMessage(objectMapper.writeValueAsString(message));
        // 首先推送给自己
        session.sendMessage(pushMessage);

        // 其次推送给对方
        // 获取对方的通道
        Integer receiverId = message.getReceiverId();
        WebSocketSession friendSession = WsSessionManager.get(receiverId.toString());
        if (Objects.nonNull(friendSession)) {
            // 对方在线，才推送
            friendSession.sendMessage(pushMessage);
        }
    }

    /**
     * socket 断开连接时
     *
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Object token = session.getAttributes().get("token");
        if (token != null) {
            // 用户退出，移除缓存
            WsSessionManager.remove(token.toString());
        }
    }


}