package com.nice.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.nice.search.service.ItemSearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/itemSearch")
public class ItemSearchController {

    @Reference
    private ItemSearchService itemSearchService;

    /**
     * solr
     * @param searchMap
     * @return
     */
    @RequestMapping("/search")
    public Map<String,Object> search(@RequestBody  Map searchMap) {
        System.out.println(1);
        return itemSearchService.search(searchMap);
    }

}
