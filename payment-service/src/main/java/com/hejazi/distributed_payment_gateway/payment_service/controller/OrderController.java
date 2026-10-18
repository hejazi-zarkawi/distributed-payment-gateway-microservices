package com.hejazi.distributed_payment_gateway.payment_service.controller;

import com.hejazi.distributed_payment_gateway.common_lib.context.MerchantContext;
import com.hejazi.distributed_payment_gateway.payment_service.dto.request.CreateOrderRequest;
import com.hejazi.distributed_payment_gateway.payment_service.dto.response.OrderResponse;
import com.hejazi.distributed_payment_gateway.payment_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MerchantContext merchantContext;

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.create(merchantContext.getMerchantId(), request));
    }

}
