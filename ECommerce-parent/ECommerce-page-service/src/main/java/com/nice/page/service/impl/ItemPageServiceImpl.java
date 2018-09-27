package com.nice.page.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nice.dao.TbGoodsDescMapper;
import com.nice.dao.TbGoodsMapper;
import com.nice.dao.TbItemCatMapper;
import com.nice.dao.TbItemMapper;
import com.nice.page.service.ItemPageService;
import com.nice.pojo.TbGoods;
import com.nice.pojo.TbGoodsDesc;
import com.nice.pojo.TbItem;
import com.nice.pojo.TbItemExample;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import sun.reflect.annotation.ExceptionProxy;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemPageServiceImpl implements ItemPageService {

    @Value("${pagedir}")
    private String pagedir;
    @Autowired
    private TbGoodsMapper goodsMapper;
    @Autowired
    private TbGoodsDescMapper goodsDescMapper;
    @Autowired
    private TbItemCatMapper itemCatMapper;
    @Autowired
    private FreeMarkerConfig freeMarkerConfig;
    @Autowired
    private TbItemMapper itemMapper;

    /**
     * 生成页面
     * @param goodsId
     * @return
     */
    @Override
    public boolean genItemHtml(Long goodsId) {
        System.out.println(goodsId);
        try {
            Configuration configuration = freeMarkerConfig.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");
            Map dataMap = new HashMap();
            // 加载商品
            TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
            TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
            dataMap.put("goods",goods);
            dataMap.put("goodsDesc",goodsDesc);
            // 面包屑
            String itemCat1 = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
            String itemCat2 = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
            String itemCat3 = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();
            dataMap.put("itemCat1",itemCat1);
            dataMap.put("itemCat2",itemCat2);
            dataMap.put("itemCat3",itemCat3);
            // 对应item
            TbItemExample exmaple = new TbItemExample();
            TbItemExample.Criteria criteria = exmaple.createCriteria();
            criteria.andGoodsIdEqualTo(goodsId);
            criteria.andStatusEqualTo("1");
            exmaple.setOrderByClause("is_default desc");
            List<TbItem> tbItems = itemMapper.selectByExample(exmaple);
            dataMap.put("itemList",tbItems);
            Writer out = new FileWriter(pagedir+goodsId+".html");
            template.process(dataMap,out);
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除页面
     * @param goodsIds
     * @return
     */
    @Override
    public boolean deleteItemHtml(Long[] goodsIds) {
        try {
            for(Long goodsId:goodsIds){
                new File(pagedir+goodsId+".html").delete();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
