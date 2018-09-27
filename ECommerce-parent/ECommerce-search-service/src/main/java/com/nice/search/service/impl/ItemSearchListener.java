package com.nice.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.nice.pojo.TbItem;
import com.nice.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;
import java.util.Map;

/**
 * 添加solr ActiveMQ监听器
 */
@Component
public class ItemSearchListener implements MessageListener {

    @Autowired
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            List<TbItem> tbItems = JSON.parseArray(text, TbItem.class);
            for (TbItem tbItem : tbItems) {
                Map specMap = JSON.parseObject(tbItem.getSpec());
                tbItem.setSpecMap(specMap);
            }
            itemSearchService.importList(tbItems);
            System.out.println("add solr success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
