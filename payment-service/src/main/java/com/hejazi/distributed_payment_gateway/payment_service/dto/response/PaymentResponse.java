package com.hejazi.distributed_payment_gateway.payment_service.dto.response;


import com.hejazi.distributed_payment_gateway.common_lib.entity.Money;
import com.hejazi.distributed_payment_gateway.common_lib.enums.PaymentMethod;
import com.hejazi.distributed_payment_gateway.common_lib.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record PaymentResponse(
        UUID id,
        UUID orderId,
        UUID merchantId,
        Money amount,
        PaymentStatus status,
        PaymentMethod method,
        Map<String, Object> methodDetails,
        String errorCode,
        String errorDescription,
        LocalDateTime capturedAt,
        LocalDateTime createdAt
) {
}
