package com.hejazi.distributed_payment_gateway.common_lib.exception;

public class IdempotencyConflictException extends  RuntimeException{
    public IdempotencyConflictException(String message) {
        super(message);
    }
}
