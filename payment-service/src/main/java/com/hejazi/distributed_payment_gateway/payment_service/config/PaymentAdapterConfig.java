package com.hejazi.distributed_payment_gateway.payment_service.config;

import com.hejazi.distributed_payment_gateway.common_lib.enums.PaymentMethod;
import com.hejazi.distributed_payment_gateway.payment_service.gateway.PaymentAdapter;
import com.hejazi.distributed_payment_gateway.payment_service.gateway.adapter.CardPaymentAdapter;
import com.hejazi.distributed_payment_gateway.payment_service.gateway.adapter.NetBankingAdapter;
import com.hejazi.distributed_payment_gateway.payment_service.gateway.adapter.UPIPaymentAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class PaymentAdapterConfig {

    private final NetBankingAdapter netBankingAdapter;
    private final CardPaymentAdapter cardPaymentAdapter;
    private final UPIPaymentAdapter upiPaymentAdapter;
    @Bean
    public Map<PaymentMethod, PaymentAdapter> paymentAdapterMap() {
        return Map.of(
                PaymentMethod.CARD, cardPaymentAdapter,
                PaymentMethod.NETBANKING, netBankingAdapter,
                PaymentMethod.UPI, upiPaymentAdapter
        );
    }
}
