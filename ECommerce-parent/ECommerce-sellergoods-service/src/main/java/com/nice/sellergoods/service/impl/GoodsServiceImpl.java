package com.nice.sellergoods.service.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.nice.dao.*;
import com.nice.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.nice.pojo.TbGoodsExample.Criteria;
import com.nice.sellergoods.service.GoodsService;

import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper goodsMapper;
	@Autowired
	private TbGoodsDescMapper goodsDescMapper;
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemCatMapper itemCatMapper;
	@Autowired
	private TbBrandMapper brandMapper;
	@Autowired
	private TbSellerMapper sellerMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbGoods> page=   (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Goods goods) {
	    goods.getGoods().setAuditStatus("0");
		goodsMapper.insert(goods.getGoods());
		TbGoodsDesc goodsDesc = goods.getGoodsDesc();
		goodsDesc.setGoodsId(goods.getGoods().getId());
		goodsDescMapper.insert(goodsDesc);
		setTbItem(goods);

	}

	private void setTbItem  (Goods goods) {

        if ("1".equals(goods.getGoods().getIsEnableSpec())) {
            for (TbItem tbItem : goods.getItemList()) {
                String title = goods.getGoods().getGoodsName();
                Map<String, Object> specMap = JSON.parseObject(tbItem.getSpec());
                for (String key : specMap.keySet()) {
                    title += " " + specMap.get(key);
                }
                tbItem.setTitle(title);   // 标题
                setTbItemValue(goods,tbItem);
                itemMapper.insert(tbItem);

            }
        } else {
            TbItem tbItem = new TbItem();
            tbItem.setTitle(goods.getGoods().getGoodsName()); // 标题
            tbItem.setPrice(goods.getGoods().getPrice());   // 价格
            tbItem.setIsDefault("1"); // 是否默认规格
            tbItem.setStatus("1");    // 状态激活
            tbItem.setNum(99999);		// 库存
            tbItem.setSpec("{}");   // 规格
            setTbItemValue(goods,tbItem);  // 其他属性设置方法调用
            itemMapper.insert(tbItem);
        }
    }

	/**
	 * tbItem部分属性设置方法
	 * @param goods
	 * @param tbItem
	 */
	private void setTbItemValue(Goods goods, TbItem tbItem){
		tbItem.setSellerId(goods.getGoods().getSellerId());  // 商家id
		tbItem.setGoodsId(goods.getGoods().getId());        // 商品id
		tbItem.setCreateTime(new Date());                    // 创建时间
		tbItem.setUpdateTime(new Date());                    // 上传时间
		tbItem.setCategoryid(goods.getGoods().getCategory3Id()); // 设置3级类目id
		// 所属3级类目
		tbItem.setCategory(
				itemCatMapper.selectByPrimaryKey(goods.getGoods().getCategory3Id()).getName()
		);
		// 所属品牌
		tbItem.setBrand(
				brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId()).getName()
		);
		// 商家名称
		tbItem.setSeller(
				sellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId()).getName()
		);
		// 图片
		List<Map> imageList = JSON.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);
		if (imageList.size() > 0) {
			tbItem.setImage((String) imageList.get(0).get("url"));
		}
	}


	
	/**
	 * 修改
	 */
	@Override
	public void update(Goods goods){
        goods.getGoods().setAuditStatus("0");
		goodsMapper.updateByPrimaryKey(goods.getGoods());
		goodsDescMapper.updateByPrimaryKey(goods.getGoodsDesc());
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(goods.getGoods().getId());
        itemMapper.deleteByExample(example);
        setTbItem(goods);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Goods findOne(Long id){
		Goods goods = new Goods();
        TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
        TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(id);
        goods.setGoods(tbGoods);
        goods.setGoodsDesc(tbGoodsDesc);

		TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(id);
        List<TbItem> tbItems = itemMapper.selectByExample(example);
        goods.setItemList(tbItems);
        return goods;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
            TbGoods goods = goodsMapper.selectByPrimaryKey(id);
            goods.setIsDelete("1");    // 修改删除状态表示删除
            goodsMapper.updateByPrimaryKey(goods);
        }
	}
	
	
		@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbGoodsExample example=new TbGoodsExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeleteIsNull();   // 非删除状态
		
		if(goods!=null){			
			if(goods.getSellerId()!=null && goods.getSellerId().length()>0){
//				criteria.andSellerIdLike("%"+goods.getSellerId()+"%");
				criteria.andSellerIdEqualTo(goods.getSellerId());
			}
			if(goods.getGoodsName()!=null && goods.getGoodsName().length()>0){
				criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
			}
			if(goods.getAuditStatus()!=null && goods.getAuditStatus().length()>0){
				criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");
			}
			if(goods.getIsMarketable()!=null && goods.getIsMarketable().length()>0){
				criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
			}
			if(goods.getCaption()!=null && goods.getCaption().length()>0){
				criteria.andCaptionLike("%"+goods.getCaption()+"%");
			}
			if(goods.getSmallPic()!=null && goods.getSmallPic().length()>0){
				criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
			}
			if(goods.getIsEnableSpec()!=null && goods.getIsEnableSpec().length()>0){
				criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
			}
			if(goods.getIsDelete()!=null && goods.getIsDelete().length()>0){
				criteria.andIsDeleteLike("%"+goods.getIsDelete()+"%");
			}
	
		}
		
		Page<TbGoods> page= (Page<TbGoods>)goodsMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public void updateStatus(Long[] ids,String status) {
		if (ids!=null&&ids.length>0) {
			for (Long id : ids) {
				TbGoods goods = goodsMapper.selectByPrimaryKey(id);
				goods.setAuditStatus(status);
				goodsMapper.updateByPrimaryKey(goods);
			}
		}
	}

}
