package com.hejazi.distributed_payment_gateway.merchant_service.dto.request;

import com.hejazi.distributed_payment_gateway.common_lib.enums.Environment;

public record CreateApiKeyRequest(Environment environment) {
}
