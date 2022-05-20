package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Seller;

public interface SellerDao {

    /**
     * 根据route的sid查询商家
     * @param sid
     * @return
     */
    public Seller findBySid(int sid);
}
