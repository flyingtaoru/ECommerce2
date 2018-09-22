import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/applicationContext-redis.xml")
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test1() {
        Long itemCat = (Long) redisTemplate.boundHashOps("itemCat").get("手机");
        System.out.println(itemCat);
        // 分类id查询品牌
        List brandList = (List) redisTemplate.boundHashOps("brandList").get(itemCat);
        // 分类id查询规格
        List specList = (List) redisTemplate.boundHashOps("specList").get(itemCat);
        System.out.println(brandList);
        System.out.println(specList);
    }

}
