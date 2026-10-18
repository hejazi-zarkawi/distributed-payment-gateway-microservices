package com.hejazi.distributed_payment_gateway.merchant_service.controller;

import com.hejazi.distributed_payment_gateway.common_lib.dto.SettlementBankDetails;
import com.hejazi.distributed_payment_gateway.common_lib.dto.WebhookTarget;
import com.hejazi.distributed_payment_gateway.merchant_service.api.MerchantLookupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/internal/merchants")
public class InternalMerchantController {
    private final MerchantLookupService merchantLookupService;

    @GetMapping("/{merchantId}/webhook-targets")
    public List<WebhookTarget> getActiveConfigsForEvent(@PathVariable UUID merchantId,
                                                        @RequestParam String eventType) {
        return merchantLookupService.getActiveConfigsForEvent(merchantId, eventType);
    }

    @GetMapping("/active-ids")
    public List<UUID> listActiveMerchantIds() {
        return merchantLookupService.listActiveMerchantIds();
    }

    @GetMapping("/{merchantId}/settlement-bank-details")
    public SettlementBankDetails getSettlementBankDetails(@PathVariable UUID merchantId) {
        return merchantLookupService.getSettlementBankDetails(merchantId);
    }
}
