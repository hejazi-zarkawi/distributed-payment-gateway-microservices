package com.hejazi.distributed_payment_gateway.merchant_service.controller;

import com.hejazi.distributed_payment_gateway.common_lib.context.MerchantContext;
import com.hejazi.distributed_payment_gateway.merchant_service.dto.request.CreateApiKeyRequest;
import com.hejazi.distributed_payment_gateway.merchant_service.dto.response.ApiKeyResponse;
import com.hejazi.distributed_payment_gateway.merchant_service.dto.response.CreateApiKeyResponse;
import com.hejazi.distributed_payment_gateway.merchant_service.service.ApiKeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/merchants/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {
    private final ApiKeyService apiKeyService;
    private final MerchantContext merchantContext;

    @PostMapping
    public ResponseEntity<CreateApiKeyResponse> create(@Valid @RequestBody CreateApiKeyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiKeyService.create(merchantContext.getMerchantId(), request));
    }

    @GetMapping
    public ResponseEntity<List<ApiKeyResponse>> getAllApiKeys(){
        return ResponseEntity.ok(apiKeyService.listByMerchant(merchantContext.getMerchantId()));
    }

    @DeleteMapping("/{keyId}")
    public ResponseEntity<Void> revoke( @PathVariable UUID keyId) {
        apiKeyService.revoke(merchantContext.getMerchantId(), keyId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{keyId}/rotate")
    public ResponseEntity<CreateApiKeyResponse> rotateKey( @PathVariable UUID keyId) {
        return ResponseEntity.ok(apiKeyService.rotate(merchantContext.getMerchantId(), keyId));
    }
}
