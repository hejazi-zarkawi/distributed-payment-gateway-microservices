package com.hejazi.distributed_payment_gateway.merchant_service.service;


import com.hejazi.distributed_payment_gateway.merchant_service.dto.request.LoginRequest;
import com.hejazi.distributed_payment_gateway.merchant_service.dto.request.MerchantSignupRequest;
import com.hejazi.distributed_payment_gateway.merchant_service.dto.response.LoginResponse;
import com.hejazi.distributed_payment_gateway.merchant_service.dto.response.MerchantResponse;

public interface AuthService {
     MerchantResponse signup(MerchantSignupRequest request);

    LoginResponse login(LoginRequest request);
}
