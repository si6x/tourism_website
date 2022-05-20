package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.MyFavorite;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.FavoriteService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Si6x
 */
public class FavoriteServiceImpl implements FavoriteService {
    FavoriteDao favoriteDao = new FavoriteDaoImpl();
    RouteDao routeDao = new RouteDaoImpl();

    /**
     * 判断用户是否收藏
     * @param rid
     * @param uid
     * @return
     */
    @Override
    public boolean isFavorite(String rid, int uid) {
        Favorite favorite = favoriteDao.findByRidAndUid(Integer.parseInt(rid), uid);
        //如果对象有值，则为true，反之，则为false
        return favorite != null;
    }

    /**
     * 添加收藏
     * @param rid
     * @param uid
     */
    @Override
    public void addFavorite(String rid, int uid) {
        favoriteDao.addFavorite(Integer.parseInt(rid),uid);
    }

    /**
     * 取消收藏
     * @param rid
     * @param uid
     */
    @Override
    public void removeFavorite(String rid, int uid) {
        favoriteDao.removeFavorite(Integer.parseInt(rid),uid);
    }

    @Override
    public PageBean<Route> favoritePageQuery(int uid, int currentPage, int pageSize) {
        //封装PageBean
        PageBean<Route> routePageBean = new PageBean<Route>();

        //设置总记录数
        int totalCount = favoriteDao.findFavoriteTotalCount(uid);
        if (totalCount == 0) {
            //没有记录
            return routePageBean;
        }


        //计算起始记录数start，计算总页数totalPage
        int start = (currentPage - 1) * pageSize;
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        //分页查询rid列表(封装在MyFavorite类中)
        List<MyFavorite> pageFavoriteList = favoriteDao.findByUidAndPage(uid,start,pageSize);
        //创建一个空的List<Route>集合
        List<Route> routeList = new ArrayList<>();
        //遍历pageFavoriteList来查询用户收藏的rid线路,并联合查询rid表装进route集合中
        for (MyFavorite myFavorite : pageFavoriteList){
            //根据其rid属性利用routeDao查route对象
            Route route = routeDao.findOne(myFavorite.getRid());
            //向routeList添加查询出来的route数据
            routeList.add(route);
        }


        //组装PageBean<Route>对象
        routePageBean.setCurrentPage(currentPage);  //设置当前页码
        routePageBean.setPageSize(pageSize);        //设置每页显示条数
        routePageBean.setTotalCount(totalCount);    //设置总记录数
        routePageBean.setTotalPage(totalPage);      //设置总页数
        routePageBean.setList(routeList);
        return routePageBean;
    }
}
