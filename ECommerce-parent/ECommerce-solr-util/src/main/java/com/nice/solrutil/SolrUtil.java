package com.nice.solrutil;

import com.alibaba.fastjson.JSON;
import com.nice.dao.TbItemMapper;
import com.nice.pojo.TbItem;
import com.nice.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SolrUtil {


    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private SolrTemplate solrTemplate;

    public void importItemData() {
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("1");
        List<TbItem> tbItems = itemMapper.selectByExample(example);
        System.out.println(tbItems);
        for (TbItem tbItem : tbItems) {
            Map specMap = (Map) JSON.parse(tbItem.getSpec());
            tbItem.setSpecMap(specMap);
        }

        solrTemplate.saveBeans(tbItems);
        solrTemplate.commit();
        System.out.println("end");
    }


    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
        SolrUtil solrUtil = (SolrUtil) ac.getBean("solrUtil");
        solrUtil.importItemData();
    }

}
