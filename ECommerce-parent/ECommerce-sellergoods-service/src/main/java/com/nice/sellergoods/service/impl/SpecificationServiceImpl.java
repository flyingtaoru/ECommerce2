package com.nice.sellergoods.service.impl;
import java.util.List;
import java.util.Map;

import com.nice.dao.TbSpecificationOptionMapper;
import com.nice.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.nice.dao.TbSpecificationMapper;
import com.nice.pojo.TbSpecificationExample.Criteria;
import com.nice.sellergoods.service.SpecificationService;

import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {

	@Autowired
	private TbSpecificationMapper specificationMapper;
	@Autowired
	private TbSpecificationOptionMapper specificationOptionMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbSpecification> findAll() {
		return specificationMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbSpecification> page=   (Page<TbSpecification>) specificationMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Specification specification) {
		specificationMapper.insert(specification.getSpecification());
		for (TbSpecificationOption tbSpecificationOption : specification.getSpecificationOptionList()) {
			tbSpecificationOption.setSpecId(specification.getSpecification().getId());
			specificationOptionMapper.insert(tbSpecificationOption);
		}

	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(Specification specification){
	    // 修改规格名称
		specificationMapper.updateByPrimaryKey(specification.getSpecification());
        // 删除旧的规格参数
        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
        TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
        criteria.andSpecIdEqualTo(specification.getSpecification().getId());
        specificationOptionMapper.deleteByExample(example);
        for (TbSpecificationOption tbSpecificationOption : specification.getSpecificationOptionList()) {
            // 添加新的
            // 为新增的参数设置对应id
            tbSpecificationOption.setSpecId(specification.getSpecification().getId());
            specificationOptionMapper.insert(tbSpecificationOption);
        }
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Specification findOne(Long id){
	    // 创建返回规格类
		Specification specification = new Specification();
		// 添加规格类型
        TbSpecification tbSpecification = specificationMapper.selectByPrimaryKey(id);
        specification.setSpecification(tbSpecification);
        // 添加规格参数
        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
        TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
        // 根据SpecId查询
        criteria.andSpecIdEqualTo(tbSpecification.getId());
        specification.setSpecificationOptionList(specificationOptionMapper.selectByExample(example));
		return specification;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
		    // 删除规格参数
            TbSpecificationOptionExample example = new TbSpecificationOptionExample();
            TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
            criteria.andSpecIdEqualTo(id);
            specificationOptionMapper.deleteByExample(example);
            // 删除规格类型
			specificationMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbSpecification specification, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbSpecificationExample example=new TbSpecificationExample();
		Criteria criteria = example.createCriteria();
		
		if(specification!=null){			
						if(specification.getSpecName()!=null && specification.getSpecName().length()>0){
				criteria.andSpecNameLike("%"+specification.getSpecName()+"%");
			}
	
		}
		
		Page<TbSpecification> page= (Page<TbSpecification>)specificationMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

    @Override
    public List<Map> selectOptionList() {
        return specificationMapper.selectOptionList();
    }
	
}
