package com.hejazi.distributed_payment_gateway.operations_service.settlement;


import com.hejazi.distributed_payment_gateway.common_lib.entity.Money;
import com.hejazi.distributed_payment_gateway.operations_service.settlement.dto.BankTransferResult;

import java.util.UUID;

public interface BankTransferProcessor {
    BankTransferResult initiate(UUID settlementId, UUID merchantId, Money amount,
                                String bankAccount, String ifsc);
}
