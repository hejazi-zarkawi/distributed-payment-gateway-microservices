package com.hejazi.distributed_payment_gateway.payment_service.config;
import com.hejazi.distributed_payment_gateway.common_lib.enums.PaymentMethod;
import com.hejazi.distributed_payment_gateway.payment_service.processor.PaymentProcessor;
import com.hejazi.distributed_payment_gateway.payment_service.processor.strategy.CardPaymentProcessor;
import com.hejazi.distributed_payment_gateway.payment_service.processor.strategy.NetBankingPaymentProcessor;
import com.hejazi.distributed_payment_gateway.payment_service.processor.strategy.UpiPaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class PaymentProcessorConfig {

    private final NetBankingPaymentProcessor netBankingAdapter;
    private final CardPaymentProcessor cardPaymentAdapter;
    private final UpiPaymentProcessor upiPaymentAdapter;

    @Bean
    public Map<PaymentMethod, PaymentProcessor> paymentProcessorMap(){
       return  Map.of(
                PaymentMethod.CARD, cardPaymentAdapter,
                PaymentMethod.NETBANKING, netBankingAdapter,
                PaymentMethod.UPI, upiPaymentAdapter
        );
    }
}
