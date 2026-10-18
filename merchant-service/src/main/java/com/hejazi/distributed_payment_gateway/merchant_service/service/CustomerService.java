package com.hejazi.distributed_payment_gateway.merchant_service.service;

import java.util.UUID;

public interface CustomerService {
    UUID findOrCreate(UUID merchantId, String email, String name, String phone);
}
