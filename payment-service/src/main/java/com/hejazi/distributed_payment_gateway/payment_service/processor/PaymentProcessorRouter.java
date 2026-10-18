package com.hejazi.distributed_payment_gateway.payment_service.processor;

import com.hejazi.distributed_payment_gateway.common_lib.dto.PaymentProcessorRequest;
import com.hejazi.distributed_payment_gateway.common_lib.dto.PaymentProcessorResponse;
import com.hejazi.distributed_payment_gateway.common_lib.enums.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentProcessorRouter {

    private final Map<PaymentMethod, PaymentProcessor> paymentProcessors;

    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {
        PaymentProcessor processor = paymentProcessors.get(request.method());
        if (processor == null) {
            throw new IllegalArgumentException("No payment processor registered for method: "+request.method());
        }
        return processor.charge(request);
    }
}
