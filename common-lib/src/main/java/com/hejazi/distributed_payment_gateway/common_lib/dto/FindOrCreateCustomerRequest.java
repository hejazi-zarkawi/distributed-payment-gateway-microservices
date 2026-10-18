package com.hejazi.distributed_payment_gateway.common_lib.dto;

import java.util.UUID;

public record FindOrCreateCustomerRequest(
        UUID merchantId,
        String email,
        String name,
        String phone
) {
}
