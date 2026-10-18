package com.hejazi.distributed_payment_gateway.merchant_service.dto.response;
import com.hejazi.distributed_payment_gateway.common_lib.enums.BusinessType;
import com.hejazi.distributed_payment_gateway.common_lib.enums.MerchantStatus;

import java.util.UUID;

public record MerchantResponse(
        UUID id,
        String name,
        String email,
        String businessName,
        BusinessType businessType,
        MerchantStatus merchantStatus
) {
}
