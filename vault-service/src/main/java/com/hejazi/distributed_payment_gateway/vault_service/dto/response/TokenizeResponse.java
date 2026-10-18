package com.hejazi.distributed_payment_gateway.vault_service.dto.response;


import com.hejazi.distributed_payment_gateway.common_lib.enums.CardBrand;

public record TokenizeResponse(
        String token,
        String lastFour,
        CardBrand brand,
        Integer expiryMonth,
        Integer expiryYear
) {
}
