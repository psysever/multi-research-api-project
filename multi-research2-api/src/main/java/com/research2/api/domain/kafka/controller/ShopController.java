package com.research2.api.domain.kafka.controller;


import com.research2.api.domain.global.dto.res.ResponseService;
import com.research2.api.domain.global.dto.res.SingleResponse;
import com.research2.api.domain.kafka.dto.req.ShopCartDto;
import com.research2.api.domain.kafka.dto.req.ShopOrderDto;
import com.research2.api.domain.kafka.service.ShopService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "SHOP API", description = "SHOP API")
public class ShopController {

    private final ShopService shopService;
    private final ResponseService responseService;


    @PostMapping("/shop/cart/create")
    @PreAuthorize("hasRole('USER')")
    public SingleResponse<String> createCart(
            @Valid @RequestBody ShopCartDto shopCartDto
    ) {
        String result = shopService.createCart(shopCartDto);
        return responseService.getSingleResponse(result);
    }


    @PostMapping("/shop/order/create")
    @PreAuthorize("hasRole('USER')")
    public SingleResponse<Boolean> createOrder(
            @Valid @RequestBody ShopOrderDto shopOrderDto
    ) {
        int result = shopService.createOrder(shopOrderDto);
        return responseService.getSingleResponse(result > 0);
    }


}
