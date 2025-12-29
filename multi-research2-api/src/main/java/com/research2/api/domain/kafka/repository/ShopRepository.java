package com.research2.api.domain.kafka.repository;


import com.research2.api.domain.kafka.dto.req.OrderListDto;
import com.research2.api.domain.kafka.dto.req.ShopCartListDto;
import com.research2.api.domain.kafka.dto.req.ShopCouponLog;
import com.research2.api.domain.kafka.dto.req.ShopOrderDto;
import com.research2.api.domain.kafka.entity.ShopOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Mapper
public interface ShopRepository {

    int createShopOrder(ShopOrderDto shopOrderDto);

    void createShopCart(ShopCartListDto shopCart);

    String findLatestOrderId(@Param("mbId") String mbId, @Param("orderType") int orderType);

    void updateCartStatus(@Param("odId") String odId);

    int countCartForOrder(@Param("odId") String odId, @Param("mbId") String mbId);


    @Transactional
    void updateMemberUsePoint(@Param("mbId") String mbId, @Param("odReceiptPoint") Integer odReceiptPoint);

    ShopOrder findByOrderId(@Param("odId") String odId);

    void createCouponLog(ShopCouponLog shopCouponLog);


}




