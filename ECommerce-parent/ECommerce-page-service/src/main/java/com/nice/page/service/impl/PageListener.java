package com.nice.page.service.impl;

import com.nice.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 生成页面
 */
@Component
public class PageListener implements MessageListener {

    @Autowired
    private ItemPageService itemPageService;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage= (TextMessage)message;
        try {
            String text = textMessage.getText(); System.out.println("接收到消息:"+text);
            boolean b = itemPageService.genItemHtml(Long.parseLong(text));
            System.out.println("add html success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
