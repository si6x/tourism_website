package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.MyFavorite;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;

public class FavoriteDaoImpl implements FavoriteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 根据rid和uid查询收藏信息
     * @param rid
     * @param uid
     * @return
     */
    @Override
    public Favorite findByRidAndUid(int rid, int uid) {
        Favorite favorite = null;
        try {
            String sql = "select * from tab_favorite where rid = ? and uid = ?";
            favorite = template.queryForObject(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), rid, uid);
        } catch (DataAccessException e) {

        }
        return favorite;
    }

    /**
     * 根据rid 查询收藏次数
     * @param rid
     * @return
     */
    @Override
    public int findCountByRid(int rid) {
        String sql = "select count(*) from tab_favorite where rid = ?";
        return template.queryForObject(sql,Integer.class,rid);
    }

    /**
     * 添加收藏
     * @param rid
     * @param uid
     */
    @Override
    public void addFavorite(int rid, int uid) {
        String sql = "INSERT INTO tab_favorite(rid,date,uid) VALUES(?,?,?)";
        template.update(sql,rid,new Date(),uid);
    }

    /**
     * 取消收藏
     * @param rid
     * @param uid
     */
    @Override
    public void removeFavorite(int rid, int uid) {
        String sql = "delete from tab_favorite where rid = ? and uid = ?";
        template.update(sql,rid,uid);
    }

    /**
     * 查询用户收藏总记录数
     * @param uid
     * @return
     */
    @Override
    public int findFavoriteTotalCount(int uid) {
        String sql = "select count(*) from tab_favorite where uid = ?";
        return template.queryForObject(sql,Integer.class,uid);
    }

    /**
     * 分页查询rid列表
     * @param uid
     * @param start
     * @param pageSize
     * @return
     */
    @Override
    public List<MyFavorite> findByUidAndPage(int uid, int start, int pageSize) {
        String sql = "select * from tab_favorite where uid = ? limit ?, ?";
        return template.query(sql,new BeanPropertyRowMapper<MyFavorite>(MyFavorite.class),uid,start,pageSize);

    }

}
