package com.hejazi.distributed_payment_gateway.common_lib.enums;

public enum PaymentStatus {
    CREATED,
    AUTHORIZING,
    AUTHORIZED,
    CAPTURING,
    CAPTURED,
    FAILED,
    CANCELLED,
    REFUNDED,
    PARTIALLY_REFUNDED,
    SETTLED,
    AUTH_EXPIRED
}
