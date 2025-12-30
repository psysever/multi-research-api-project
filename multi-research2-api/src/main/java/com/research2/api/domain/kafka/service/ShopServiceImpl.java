package com.research2.api.domain.kafka.service;


import com.research2.api.domain.global.exception.CustomException;
import com.research2.api.domain.global.exception.error.ErrorCodes;
import com.research2.api.domain.kafka.dto.event.ShopOrderCreatedEventDto;
import com.research2.api.domain.kafka.dto.req.*;
import com.research2.api.domain.kafka.repository.ShopRepository;
import com.research2.api.domain.kafka.utill.OrderIdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;


@Service("ShopServiceImpl")
@RequiredArgsConstructor
@Slf4j
public class ShopServiceImpl implements ShopService {


    private final ShopRepository shopRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final OrderIdService orderIdService;


    @Transactional
    @Override
    public String createCart(ShopCartDto shopCartDto) {

        String orderId = null;
        if (shopCartDto == null || shopCartDto.getShopCartList().isEmpty()) {
            throw new CustomException(ErrorCodes.UserErrorCode.NEED_CART_LIST);
        } else {
            for (ShopCartListDto shopCart : shopCartDto.getShopCartList()) {
                if (StringUtils.isBlank(shopCart.getItemId())) {
                    throw new CustomException(ErrorCodes.UserErrorCode.NEED_ITEM_ID);
                }
                orderId = orderIdService.getOrCreateOrderId(
                        shopCart.getMbId(), shopCartDto.getOrderType());
                shopCart.setOrderId(orderId);
                shopRepository.createShopCart(shopCart);
            }
            return orderId;
        }

    }


    @Transactional
    @Override
    public int createOrder(ShopOrderDto shopOrderDto) {
        int cartCount = shopRepository.countCartForOrder(
                shopOrderDto.getOrderId(),
                shopOrderDto.getMbId()
        );

        if (cartCount <= 0) {
            throw new CustomException(ErrorCodes.UserErrorCode.INVALID_ORDER);
        }

        for (CouponListDto couponListReq : shopOrderDto.getCouponListReqList()) {
            String odId = shopOrderDto.getOrderId();
            if (odId != null) {
                ShopCouponLog couponLogReq = ShopCouponLog.builder()
                        .cpId(couponListReq.getCpId())
                        .cpPrice(couponListReq.getCpPrice())
                        .mbId(shopOrderDto.getMbId())
                        .odId(odId).build();
                shopRepository.createCouponLog(couponLogReq);
            }
        }

        int result = shopRepository.createShopOrder(shopOrderDto);
        if (result <= 0) {
            throw new CustomException(ErrorCodes.UserErrorCode.ORDER_CREATE_FAILED);
        }


        shopRepository.updateCartStatus(shopOrderDto.getOrderId());


        ShopOrderCreatedEventDto event = ShopOrderCreatedEventDto.builder()
                .eventId(UUID.randomUUID().toString())
                .orderId(shopOrderDto.getOrderId())
                .userId(shopOrderDto.getMbId())
                .occurredAt(Instant.now())
                .odReceiptPoint(shopOrderDto.getOdReceiptPoint())
                .build();

        applicationEventPublisher.publishEvent(event);

        return result;
    }


}

