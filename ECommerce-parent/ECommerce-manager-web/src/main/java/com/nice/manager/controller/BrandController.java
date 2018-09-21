package com.nice.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.nice.pojo.TbBrand;
import com.nice.sellergoods.service.BrandService;
import entity.PageResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference
    private BrandService brandService;


    /**
     * 查询所有
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbBrand> findAll() {
        return brandService.findAll();
    }

    /**
     * 分页查询
     * @param page 当前页码
     * @param size 每页条数
     * @return
     */
    @RequestMapping("/findByPage")
    public PageResult findByPage(int page,int size) {
        return brandService.findByPage(page,size);
    }


    /**
     * 条件模糊查询
     * @param page 当前页码
     * @param size 每页条数
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbBrand tbBrand,int page,int size) {
        return brandService.findByPage(tbBrand,page,size);
    }

    /**
     * 新增
     * @param tbBrand
     * @return
     */
    @RequestMapping("/save")
    public Map save(@RequestBody TbBrand tbBrand) {
        Map<String,Boolean> flag = new HashMap<>();
        flag.put("flag",false);
        System.out.println(tbBrand.getName());
        try {
            brandService.save(tbBrand);
            flag.put("flag",true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public TbBrand findById(long id) {
        return brandService.findById(id);
    }

    /**
     * 修改
     * @param tbBrand
     * @return
     */
    @RequestMapping("/update")
    public Map update(@RequestBody TbBrand tbBrand) {
        Map<String,Boolean> flag = new HashMap<>();
        flag.put("flag",false);
        System.out.println(tbBrand.getName());
        try {
            brandService.update(tbBrand);
            flag.put("flag",true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 修改
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Map delete(long[] ids) {
        Map<String,Boolean> flag = new HashMap<>();
        flag.put("flag",false);
        try {
            brandService.delete(ids);
            flag.put("flag",true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    @RequestMapping("/selectOptionList")
    public List<Map> selectOptionList() {
        return brandService.selectOptionList();
    }

}

