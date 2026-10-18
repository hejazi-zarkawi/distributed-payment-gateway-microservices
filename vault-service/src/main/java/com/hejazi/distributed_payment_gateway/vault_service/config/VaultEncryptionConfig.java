package com.hejazi.distributed_payment_gateway.vault_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.AesGcmBytesEncryptor;
import org.springframework.security.crypto.encrypt.BytesEncryptor;

import javax.crypto.spec.SecretKeySpec;

@Configuration
public class VaultEncryptionConfig {

    @Value("${vault.master-key}")
    private String masterKey;

    public static BytesEncryptor panEncrypter(byte[] dek) {
        SecretKeySpec decKey = new SecretKeySpec(dek, "AES");
        return AesGcmBytesEncryptor.withSecretKey(decKey).build();
    }

}
