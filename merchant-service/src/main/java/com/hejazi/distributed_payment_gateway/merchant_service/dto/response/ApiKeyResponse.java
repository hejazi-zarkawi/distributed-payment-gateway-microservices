package com.hejazi.distributed_payment_gateway.merchant_service.dto.response;
import com.hejazi.distributed_payment_gateway.common_lib.enums.Environment;

import java.time.LocalDateTime;
import java.util.UUID;

public record ApiKeyResponse(
        UUID id,
        String keyId,
        Environment environment,
        boolean enabled,
        LocalDateTime lastUsedAt,
        LocalDateTime createdAt
) {
}
