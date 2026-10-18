package com.hejazi.distributed_payment_gateway.payment_service.gateway.adapter;

import com.hejazi.distributed_payment_gateway.common_lib.dto.PaymentProcessorResponse;
import com.hejazi.distributed_payment_gateway.common_lib.dto.VaultChargeRequest;
import com.hejazi.distributed_payment_gateway.payment_service.client.VaultServiceClient;
import com.hejazi.distributed_payment_gateway.payment_service.gateway.PaymentAdapter;
import com.hejazi.distributed_payment_gateway.payment_service.gateway.dto.PaymentRequest;
import com.hejazi.distributed_payment_gateway.payment_service.gateway.dto.PaymentResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CardPaymentAdapter implements PaymentAdapter {
    private final VaultServiceClient vaultServiceClient;

    @Override
    public PaymentResult initiate(PaymentRequest request) {
        String token = (String) request.methodDetails().get("token");

        PaymentProcessorResponse response = vaultServiceClient.charge(
                new VaultChargeRequest(request.paymentId(), token, request.amount(), request.methodDetails())
        );


        return switch (response) {
            case PaymentProcessorResponse.Success success -> new PaymentResult.Success(success.bankReference());
            case PaymentProcessorResponse.Failure failure -> new PaymentResult.Failure(failure.errorCode(), failure.errorDescription());
            case PaymentProcessorResponse.Pending pending -> new PaymentResult.Pending(pending.processorReference());
        };
    }

    @Override
    public PaymentResult capture(UUID paymentId) {
        return new PaymentResult.Success("CARD_REF");
    }
}
