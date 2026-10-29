package com.hejazi.distributed_payment_gateway.payment_service.service.impl;

import com.hejazi.distributed_payment_gateway.common_lib.enums.EventAggregateType;
import com.hejazi.distributed_payment_gateway.common_lib.enums.OrderStatus;
import com.hejazi.distributed_payment_gateway.payment_service.dto.request.CreateOrderRequest;
import com.hejazi.distributed_payment_gateway.payment_service.dto.response.OrderResponse;
import com.hejazi.distributed_payment_gateway.payment_service.entity.OrderRecord;
import com.hejazi.distributed_payment_gateway.payment_service.mapper.OrderMapper;
import com.hejazi.distributed_payment_gateway.payment_service.outbox.OutboxEventPublisher;
import com.hejazi.distributed_payment_gateway.payment_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderPersistenceService {
    private final OrderRepository orderRepository;
    private final OutboxEventPublisher eventPublisher;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderResponse persist(UUID merchantId, CreateOrderRequest request, UUID customerId,
                                 int defaultOrderExpiryMinutes) {
        OrderRecord order = OrderRecord.builder()
                .receipt(request.receipt())
                .amount(request.amount())
                .notes(request.notes())
                .merchantId(merchantId)
                .customerId(customerId)
                .orderStatus(OrderStatus.CREATED)
                .expiresAt(request.expiresAt() != null ? request.expiresAt() :
                        LocalDateTime.now().plusMinutes(defaultOrderExpiryMinutes))
                .build();

        order = orderRepository.save(order);

        eventPublisher.publish(EventAggregateType.ORDER, order.getId(), "ORDER_CREATED",
                Map.of("orderId", order.getId(),
                        "merchantId", merchantId.toString(),
                        "orderStatus", order.getOrderStatus().name(),
                        "amountUnits", order.getAmount().getAmountUnits(),
                        "amountCurrency", order.getAmount().getCurrency()
                )
        );

        return orderMapper.toResponse(order);
    }
}
