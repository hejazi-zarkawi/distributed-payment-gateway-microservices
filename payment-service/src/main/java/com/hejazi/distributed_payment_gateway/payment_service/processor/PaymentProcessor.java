package com.hejazi.distributed_payment_gateway.payment_service.processor;


import com.hejazi.distributed_payment_gateway.common_lib.dto.PaymentProcessorRequest;
import com.hejazi.distributed_payment_gateway.common_lib.dto.PaymentProcessorResponse;

public interface PaymentProcessor {

    PaymentProcessorResponse charge(PaymentProcessorRequest request);
}
