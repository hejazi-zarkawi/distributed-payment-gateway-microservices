package com.hejazi.distributed_payment_gateway.merchant_service.service.impl;


import com.hejazi.distributed_payment_gateway.common_lib.enums.MerchantStatus;
import com.hejazi.distributed_payment_gateway.common_lib.enums.UserRole;
import com.hejazi.distributed_payment_gateway.common_lib.exception.BusinessRuleViolationException;
import com.hejazi.distributed_payment_gateway.common_lib.exception.DuplicateResourceException;
import com.hejazi.distributed_payment_gateway.common_lib.exception.ResourceNotFoundException;
import com.hejazi.distributed_payment_gateway.merchant_service.dto.request.LoginRequest;
import com.hejazi.distributed_payment_gateway.merchant_service.dto.request.MerchantSignupRequest;
import com.hejazi.distributed_payment_gateway.merchant_service.dto.response.LoginResponse;
import com.hejazi.distributed_payment_gateway.merchant_service.dto.response.MerchantResponse;
import com.hejazi.distributed_payment_gateway.merchant_service.entity.AppUser;
import com.hejazi.distributed_payment_gateway.merchant_service.entity.Merchant;
import com.hejazi.distributed_payment_gateway.merchant_service.mapper.MerchantMapper;
import com.hejazi.distributed_payment_gateway.merchant_service.repository.AppUserRepository;
import com.hejazi.distributed_payment_gateway.merchant_service.repository.MerchantRepository;
import com.hejazi.distributed_payment_gateway.merchant_service.security.JwtUtil;
import com.hejazi.distributed_payment_gateway.merchant_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MerchantRepository merchantRepository;
    private final AppUserRepository appUserRepository;

    private final MerchantMapper merchantMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    @Override
    @Transactional
    public MerchantResponse signup(MerchantSignupRequest request) {
        if (merchantRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("DUPLICATE_MERCHANT_EMAIL",
                    "Merchant with email already exists: " + request.email());
        }

        Merchant merchant = merchantMapper.toEntityFromSignUpRequest(request);
        merchant.setStatus(MerchantStatus.PENDING_KYC);
        merchant = merchantRepository.save(merchant);

        AppUser appUser = AppUser.builder()
                .email(request.email())
                .merchant(merchant)
                .passwordHash(passwordEncoder.encode(request.password()))
                .role(UserRole.OWNER)
                .build();
        appUserRepository.save(appUser);

        return merchantMapper.toResponse(merchant);
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        AppUser appUser = appUserRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User", request.email()));

        if (!passwordEncoder.matches(request.password(), appUser.getPasswordHash())) {
            throw new BusinessRuleViolationException("INVALID_CREDENTIALS", "Invalid email or password");
        }

        String token = jwtUtil.generateAccessToken(request.email(),
                appUser.getMerchant().getId(), appUser.getRole().toString());

        return new LoginResponse(token);
    }
}
