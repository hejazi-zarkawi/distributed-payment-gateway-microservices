package com.hejazi.distributed_payment_gateway.common_lib.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.AesGcmBytesEncryptor;
import org.springframework.security.crypto.encrypt.BytesEncryptor;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


public class AesEncryptionConfig {

    @Bean
    public BytesEncryptor masterKeyEncryptor(String masterKey) {
        byte[] masterKeyBytes = Base64.getDecoder().decode(masterKey);
        SecretKeySpec masterDecKey = new SecretKeySpec(masterKeyBytes, "AES");
        return AesGcmBytesEncryptor.withSecretKey(masterDecKey).build();
    }
}
