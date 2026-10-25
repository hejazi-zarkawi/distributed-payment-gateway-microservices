package com.hejazi.distributed_payment_gateway.payment_service.service;


import com.hejazi.distributed_payment_gateway.payment_service.dto.request.PaymentInitRequest;
import com.hejazi.distributed_payment_gateway.payment_service.dto.response.PaymentResponse;

import java.util.UUID;

public interface PaymentService {

    PaymentResponse initiate(UUID merchantId, PaymentInitRequest request, String idempotencyKey);

    PaymentResponse capture(UUID merchantId, UUID paymentId);

    void resolveAuthorization(UUID id, boolean b, String bankRef, String errorCode, String errorDescription);
}
