package com.hejazi.distributed_payment_gateway.payment_service.dto.request;

import com.hejazi.distributed_payment_gateway.common_lib.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.UUID;

public record PaymentInitRequest(
        @NotNull(message = "Order Id is required")
        UUID orderId,

        @NotNull(message = "Payment method is required")
        PaymentMethod method,

        Map<String, Object> methodDetails
) {
}
