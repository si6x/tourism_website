package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.MyFavorite;

import java.util.List;

public interface FavoriteDao {

    /**
     * 根据rid和uid查询收藏信息
     * @param rid
     * @param uid
     * @return
     */
    public Favorite findByRidAndUid(int rid, int uid);


    /**
     * 根据rid 查询收藏次数
     * @param rid
     * @return
     */
    public int findCountByRid(int rid);

    /**
     * 添加收藏
     * @param rid
     * @param uid
     */
    public void addFavorite(int rid, int uid);

    /**
     * 取消收藏
     * @param parseInt
     * @param uid
     */
    public void removeFavorite(int parseInt, int uid);

    /**
     * 查询用户收藏总记录数
     * @param uid
     * @return
     */
    public int findFavoriteTotalCount(int uid);

    /**
     * 分页查询rid列表
     * @param uid
     * @param start
     * @param pageSize
     * @return
     */
    public List<MyFavorite> findByUidAndPage(int uid, int start, int pageSize);
}
