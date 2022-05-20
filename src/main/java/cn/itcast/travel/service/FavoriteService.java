package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

public interface FavoriteService {

    /**
     * 判断用户是否收藏
     * @param rid
     * @param uid
     * @return
     */
    public boolean isFavorite(String rid,int uid);

    /**
     * 添加收藏
     * @param rid
     * @param uid
     */
    public void addFavorite(String rid, int uid);

    /**
     * 取消收藏
     * @param rid
     * @param uid
     */
    public void removeFavorite(String rid, int uid);

    /**
     * 查询我的收藏列表并分页查询
     * @param uid
     * @param currentPage
     * @param pageSize
     * @return
     */
    public PageBean<Route> favoritePageQuery(int uid, int currentPage, int pageSize);
}
