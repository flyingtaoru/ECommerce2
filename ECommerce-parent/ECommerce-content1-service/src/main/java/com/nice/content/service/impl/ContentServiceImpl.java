package com.nice.content.service.impl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.nice.dao.TbContentMapper;
import com.nice.pojo.TbContent;
import com.nice.pojo.TbContentExample;
import com.nice.pojo.TbContentExample.Criteria;
import com.nice.content.service.ContentService;

import entity.PageResult;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private RedisTemplate redisTemplate;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbContent> findAll() {
		return contentMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbContent> page=   (Page<TbContent>) contentMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbContent content) {
	    redisTemplate.boundHashOps("content").delete(content.getCategoryId());
		contentMapper.insert(content);
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbContent content){
	    // 删除愿分类id的缓存
        Long oldCategoryId = contentMapper.selectByPrimaryKey(content.getId()).getCategoryId();
        redisTemplate.boundHashOps("content").delete(oldCategoryId);
        contentMapper.updateByPrimaryKey(content);
        // 删除新分类id的缓存
        redisTemplate.boundHashOps("content").delete(content.getCategoryId());
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbContent findOne(Long id){
		return contentMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
            Long categoryId = contentMapper.selectByPrimaryKey(id).getCategoryId();
            redisTemplate.boundHashOps("content").delete(categoryId);
            contentMapper.deleteByPrimaryKey(id);
		}
	}
	
	
		@Override
	public PageResult findPage(TbContent content, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbContentExample example=new TbContentExample();
		Criteria criteria = example.createCriteria();
		
		if(content!=null){			
						if(content.getTitle()!=null && content.getTitle().length()>0){
				criteria.andTitleLike("%"+content.getTitle()+"%");
			}
			if(content.getUrl()!=null && content.getUrl().length()>0){
				criteria.andUrlLike("%"+content.getUrl()+"%");
			}
			if(content.getPic()!=null && content.getPic().length()>0){
				criteria.andPicLike("%"+content.getPic()+"%");
			}
			if(content.getStatus()!=null && content.getStatus().length()>0){
				criteria.andStatusLike("%"+content.getStatus()+"%");
			}
	
		}
		
		Page<TbContent> page= (Page<TbContent>)contentMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public List<TbContent> findByCategoryId(Long categoryId) {

		// 先从缓存中获取
		List<TbContent> contentList = (List<TbContent>) redisTemplate.boundHashOps("content").get(categoryId);

		if (contentList == null) {  // 如果缓存没有 从数据库中查询
			TbContentExample example = new TbContentExample();
			Criteria criteria = example.createCriteria();
			criteria.andCategoryIdEqualTo(categoryId); // 根据分类id查询
			criteria.andStatusEqualTo("1");    // 激活状态
			example.setOrderByClause("sort_order");  // 排序
            contentList = contentMapper.selectByExample(example);

            redisTemplate.boundHashOps("content").put(categoryId,contentList); // 存入
        }
        return contentList;

	}

}
