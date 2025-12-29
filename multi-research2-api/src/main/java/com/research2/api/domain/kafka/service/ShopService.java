package com.research2.api.domain.kafka.service;


import com.research2.api.domain.kafka.dto.req.ShopCartDto;
import com.research2.api.domain.kafka.dto.req.ShopOrderDto;


public interface ShopService {

    String createCart(ShopCartDto shopCartDto);

    int createOrder(ShopOrderDto shopOrderDto);

}
