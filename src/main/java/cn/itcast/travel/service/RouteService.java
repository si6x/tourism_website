package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteService {
    /**
     * 根据类别进行分页查询
     * @param currentPage
     * @param pageSize
     * @param cid
     * @return
     */
    PageBean<Route> pageQuery(int currentPage, int pageSize, int cid, String rname);

    /**
     * 根据rid查询route表
     * @param rid
     * @return
     */
    Route findOne(String rid);
}
