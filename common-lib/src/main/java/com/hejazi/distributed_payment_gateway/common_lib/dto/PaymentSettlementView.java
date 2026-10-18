package com.hejazi.distributed_payment_gateway.common_lib.dto;

import java.util.UUID;

public record PaymentSettlementView(
        UUID paymentId,
        int amountUnits,
        int refundedAmountUnits,
        String currency
) {
}
