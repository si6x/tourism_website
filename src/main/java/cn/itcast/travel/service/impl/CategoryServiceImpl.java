package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {
    CategoryDao categoryDao = new CategoryDaoImpl();

    @Override
    public List<Category> findAll() {
        //1.从redis中查询
        //1.1获取jedis客户端
        Jedis jedis = JedisUtil.getJedis();
        //1.2可使用sortedset排序查询
//        Set<String> categorys = jedis.zrange("category", 0, -1);
        //1.3查询sortedset中的分数(cid)的值(cname)
        Set<Tuple> categorys = jedis.zrangeWithScores("category", 0, -1);
        List<Category> categoryList = null;
        //2.判断查询的集合是否为空
        if (categorys == null || categorys.size() == 0){
            //3.如果为空，需要从数据库查询，再将数据存入redis
            //3.1 从数据库查询
            categoryList = categoryDao.findAll();
            //3.2 将集合数据存储到redis中的 category的key
            for (int i = 0; i < categoryList.size(); i++) {
                jedis.zadd("category",categoryList.get(i).getCid(),categoryList.get(i).getCname());
            }
        }else {
            //4.如果不为空,将set的数据存入List
                categoryList = new ArrayList<Category>();
                for (Tuple tuple : categorys) {
                    Category category = new Category();
                    category.setCname(tuple.getElement());
                    category.setCid((int) tuple.getScore());
                    categoryList.add(category);
                }
        }
        return categoryList;
    }
}
