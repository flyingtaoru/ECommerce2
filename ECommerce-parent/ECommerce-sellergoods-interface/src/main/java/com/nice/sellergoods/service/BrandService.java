package com.nice.sellergoods.service;

import com.nice.pojo.TbBrand;
import entity.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 品牌接口
 * @author Administrator
 *
 */
public interface BrandService {

    List<TbBrand> findAll();

    PageResult findByPage(int page, int size);

    PageResult findByPage(TbBrand tbBrand,int page,int size);

    void save(TbBrand tbBrand);

    TbBrand findById(long id);

    void update(TbBrand tbBrand);

    void delete(long[] ids);

    List<Map> selectOptionList();

}