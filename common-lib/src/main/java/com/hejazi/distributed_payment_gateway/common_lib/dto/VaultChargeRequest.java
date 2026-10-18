package com.hejazi.distributed_payment_gateway.common_lib.dto;

import com.hejazi.distributed_payment_gateway.common_lib.entity.Money;

import java.util.Map;
import java.util.UUID;

public record VaultChargeRequest(
        UUID paymentId,
        String token,
        Money amount,
        Map<String, Object> methodDetails
) {
}
