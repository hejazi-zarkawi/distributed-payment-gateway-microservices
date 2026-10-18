package com.hejazi.distributed_payment_gateway.merchant_service.service;

import com.hejazi.distributed_payment_gateway.merchant_service.dto.request.CreateApiKeyRequest;
import com.hejazi.distributed_payment_gateway.merchant_service.dto.response.ApiKeyResponse;
import com.hejazi.distributed_payment_gateway.merchant_service.dto.response.CreateApiKeyResponse;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface ApiKeyService {
    CreateApiKeyResponse create(UUID merchantId, CreateApiKeyRequest request);

    List<ApiKeyResponse> listByMerchant(UUID merchantId);

    void revoke(UUID merchantId, UUID keyId);


    @Nullable CreateApiKeyResponse rotate(UUID merchantId, UUID keyId);
}
