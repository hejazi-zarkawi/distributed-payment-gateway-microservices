package com.hejazi.distributed_payment_gateway.merchant_service.dto.response;

import com.hejazi.distributed_payment_gateway.common_lib.enums.Environment;

import java.util.UUID;

public record CreateApiKeyResponse(
        UUID id,
        String keyId,
        String keySecret,
        Environment environment
) {
}
