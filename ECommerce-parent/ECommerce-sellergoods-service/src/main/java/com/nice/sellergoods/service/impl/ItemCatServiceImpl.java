package com.nice.sellergoods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.nice.dao.TbItemCatMapper;
import com.nice.pojo.TbItemCat;
import com.nice.pojo.TbItemCatExample;
import com.nice.pojo.TbItemCatExample.Criteria;
import com.nice.sellergoods.service.ItemCatService;

import entity.PageResult;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper itemCatMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询全部
     */
    @Override
    public List<TbItemCat> findAll() {
        return itemCatMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbItemCat> page = (Page<TbItemCat>) itemCatMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbItemCat itemCat) {
        itemCatMapper.insert(itemCat);
    }


    /**
     * 修改
     */
    @Override
    public void update(TbItemCat itemCat) {
        itemCatMapper.updateByPrimaryKey(itemCat);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbItemCat findOne(Long id) {
        return itemCatMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            itemCatMapper.deleteByPrimaryKey(id);
            TbItemCatExample example = new TbItemCatExample();
            Criteria criteria = example.createCriteria();
            criteria.andParentIdEqualTo(id);
            itemCatMapper.deleteByExample(example);
        }
    }


    @Override
    public PageResult findPage(TbItemCat itemCat, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbItemCatExample example = new TbItemCatExample();
        Criteria criteria = example.createCriteria();

        if (itemCat != null) {
            if (itemCat.getName() != null && itemCat.getName().length() > 0) {
                criteria.andNameLike("%" + itemCat.getName() + "%");
            }
            criteria.andParentIdEqualTo(itemCat.getParentId());

        }

        Page<TbItemCat> page = (Page<TbItemCat>) itemCatMapper.selectByExample(example);

        // 缓存
        List<TbItemCat> list = findAll();
        for (TbItemCat itemCat1 : list) {
            if (itemCat1.getName()!=null) {
                redisTemplate.boundHashOps("itemCat").put(itemCat1.getName(),
                        itemCat1.getTypeId());
            }
        }

        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public List<TbItemCat> findByParentId(Long parentId) {
        TbItemCatExample example = new TbItemCatExample();
        Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);

        // 缓存
        List<TbItemCat> list = findAll();
        for (TbItemCat itemCat : list) {
            if (itemCat.getName()!= null) {
                redisTemplate.boundHashOps("itemCat").put(itemCat.getName(),
                        itemCat.getTypeId());
            }
        }

        return itemCatMapper.selectByExample(example);
    }


}
