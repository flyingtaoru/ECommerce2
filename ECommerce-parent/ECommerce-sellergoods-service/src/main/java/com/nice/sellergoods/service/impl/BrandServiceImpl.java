package com.nice.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.nice.dao.TbBrandMapper;
import com.nice.pojo.TbBrand;
import com.nice.pojo.TbBrandExample;
import com.nice.sellergoods.service.BrandService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 品牌业务层
 */
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private TbBrandMapper brandMapper;


    /**
     * 查询所有品牌
     * @return
     */
    @Override
    public List<TbBrand> findAll() {

        return brandMapper.selectByExample(null);
    }

    /**
     * 分页查询
     * @param page  当前页码
     * @param size  当页条数
     * @return
     */
    @Override
    public PageResult findByPage(int page,int size) {
        PageHelper.startPage(page,size);
        Page<TbBrand> tbBrands = (Page<TbBrand>) brandMapper.selectByExample(null);

        return new PageResult(tbBrands.getTotal(),tbBrands.getResult());
    }

    /**
     * 品牌条件分页查询
     * @param tbBrand
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageResult findByPage(TbBrand tbBrand, int page, int size) {
        PageHelper.startPage(page,size);
        TbBrandExample example=new TbBrandExample();
        TbBrandExample.Criteria criteria = example.createCriteria();

        if (tbBrand != null) {
            if (tbBrand.getName()!=null&&tbBrand.getName().length()>0) {
                criteria.andNameLike("%"+tbBrand.getName()+"%");
            }
            if (tbBrand.getFirstChar()!=null&&tbBrand.getFirstChar().length()>0){
                criteria.andFirstCharEqualTo(tbBrand.getFirstChar());
            }
        }

        Page<TbBrand> tbBrands = (Page<TbBrand>) brandMapper.selectByExample(example);

        return new PageResult(tbBrands.getTotal(),tbBrands.getResult());
    }

    /**
     * 添加品牌
     * @param tbBrand
     */
    @Override
    public void save(TbBrand tbBrand) {
        brandMapper.insert(tbBrand);
    }

    /**
     * 修改品牌
     * @param tbBrand
     */
    @Override
    public void update(TbBrand tbBrand) {
        brandMapper.updateByPrimaryKey(tbBrand);
    }

    /**
     * 删除品牌
     * @param ids
     */
    @Override
    public void delete(long[] ids) {
        for (long id : ids) {
            brandMapper.deleteByPrimaryKey(id);
        }
    }

    @Override
    public List<Map> selectOptionList() {
        return brandMapper.selectOptionList();
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public TbBrand findById(long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

}