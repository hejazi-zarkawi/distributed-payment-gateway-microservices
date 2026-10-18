package com.hejazi.distributed_payment_gateway.payment_service.gateway;



import com.hejazi.distributed_payment_gateway.payment_service.gateway.dto.PaymentRequest;
import com.hejazi.distributed_payment_gateway.payment_service.gateway.dto.PaymentResult;

import java.util.UUID;

public interface PaymentAdapter {

    PaymentResult initiate(PaymentRequest request);

    PaymentResult capture(UUID paymentId);
}
