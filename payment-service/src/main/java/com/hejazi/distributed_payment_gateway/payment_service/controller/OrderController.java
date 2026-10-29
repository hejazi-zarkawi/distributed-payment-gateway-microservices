package com.hejazi.distributed_payment_gateway.payment_service.controller;

import com.hejazi.distributed_payment_gateway.common_lib.context.MerchantContext;
import com.hejazi.distributed_payment_gateway.payment_service.dto.request.CreateOrderRequest;
import com.hejazi.distributed_payment_gateway.payment_service.dto.response.OrderResponse;
import com.hejazi.distributed_payment_gateway.payment_service.dto.response.PaymentResponse;
import com.hejazi.distributed_payment_gateway.payment_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getById(
            @PathVariable UUID orderId
    ) {
        UUID merchantId = merchantContext.getMerchantId();

        OrderResponse response = orderService.getById(merchantId, orderId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancel(
            @PathVariable UUID orderId
    ) {
        UUID merchantId = merchantContext.getMerchantId();

        OrderResponse response = orderService.cancel(merchantId, orderId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}/payments")
    public ResponseEntity<List<PaymentResponse>> listPayments(
            @PathVariable UUID orderId
    ) {
        UUID merchantId = merchantContext.getMerchantId();

        List<PaymentResponse> response =
                orderService.listPayments(merchantId, orderId);

        return ResponseEntity.ok(response);
    }

}
