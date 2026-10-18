package com.hejazi.distributed_payment_gateway.merchant_service.service.impl;



import com.hejazi.distributed_payment_gateway.common_lib.cache.ApiKeyCache;
import com.hejazi.distributed_payment_gateway.common_lib.exception.ResourceNotFoundException;
import com.hejazi.distributed_payment_gateway.common_lib.util.RandomizerUtil;
import com.hejazi.distributed_payment_gateway.merchant_service.dto.request.CreateApiKeyRequest;
import com.hejazi.distributed_payment_gateway.merchant_service.dto.response.ApiKeyResponse;
import com.hejazi.distributed_payment_gateway.merchant_service.dto.response.CreateApiKeyResponse;
import com.hejazi.distributed_payment_gateway.merchant_service.entity.ApiKey;
import com.hejazi.distributed_payment_gateway.merchant_service.entity.Merchant;
import com.hejazi.distributed_payment_gateway.merchant_service.mapper.ApiKeyMapper;
import com.hejazi.distributed_payment_gateway.merchant_service.repository.ApiKeyRepository;
import com.hejazi.distributed_payment_gateway.merchant_service.repository.MerchantRepository;
import com.hejazi.distributed_payment_gateway.merchant_service.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApiKeyServiceImpl implements ApiKeyService {

    private final MerchantRepository merchantRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final ApiKeyMapper apiKeyMapper;
    private final PasswordEncoder passwordEncoder;
    private final ApiKeyCache apiKeyCache;
    @Override
    @Transactional
    public CreateApiKeyResponse create(UUID merchantId, CreateApiKeyRequest request) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("merchant", merchantId));

        String keyId = "rzp_"+request.environment().name().toLowerCase()+"_"+ RandomizerUtil.randomBase64(24);
        String rawSecret = RandomizerUtil.randomBase64(40);


        ApiKey apiKey = ApiKey.builder()
                .merchant(merchant)
                .keyId(keyId)
                .keySecretHash(passwordEncoder.encode(rawSecret))
                .environment(request.environment())
                .build();

        apiKey = apiKeyRepository.save(apiKey);

        return new CreateApiKeyResponse(apiKey.getId(), keyId, rawSecret, request.environment());
    }

    @Override
    public List<ApiKeyResponse> listByMerchant(UUID merchantId) {

        if (!merchantRepository.existsById(merchantId)) {
            throw new ResourceNotFoundException("merchant", merchantId);
        }
        return apiKeyMapper.toResponseList(apiKeyRepository.findByMerchant_Id(merchantId));
    }

    @Override
    @Transactional
    public void revoke(UUID merchantId, UUID keyId) {
        ApiKey key = apiKeyRepository.findById(keyId)
                .filter(k -> k.getMerchant().getId().equals(merchantId))
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey", keyId));

        key.setEnabled(false);
        apiKeyCache.evict(key.getKeyId());
    }

    @Override
    @Transactional
    public @Nullable CreateApiKeyResponse rotate(UUID merchantId, UUID keyId) {
        ApiKey apiKey = apiKeyRepository.findById(keyId)
                .filter(k -> k.getMerchant().getId().equals(merchantId))
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey", keyId));

        if(!apiKey.isEnabled()) throw new RuntimeException("Cannot rotate a disabled key");

        String newRawSecret = RandomizerUtil.randomBase64(40);
        apiKey.setPreviousKeySecretHash(apiKey.getKeySecretHash());
        apiKey.setKeySecretHash(passwordEncoder.encode(newRawSecret));
        apiKey.setRotatedAt(LocalDateTime.now());
        apiKey.setGracePeriodExpiresAt(LocalDateTime.now().plusHours(24));
        apiKey = apiKeyRepository.save(apiKey);

        apiKeyCache.evict(apiKey.getKeyId());

        return new CreateApiKeyResponse(apiKey.getId(), apiKey.getKeyId(),
                newRawSecret, apiKey.getEnvironment());
    }

}
