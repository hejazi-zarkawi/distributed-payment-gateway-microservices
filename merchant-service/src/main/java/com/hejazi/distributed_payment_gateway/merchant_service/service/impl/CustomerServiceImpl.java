package com.hejazi.distributed_payment_gateway.merchant_service.service.impl;

import com.hejazi.distributed_payment_gateway.common_lib.exception.ResourceNotFoundException;
import com.hejazi.distributed_payment_gateway.merchant_service.entity.Customer;
import com.hejazi.distributed_payment_gateway.merchant_service.entity.Merchant;
import com.hejazi.distributed_payment_gateway.merchant_service.repository.CustomerRepository;
import com.hejazi.distributed_payment_gateway.merchant_service.repository.MerchantRepository;
import com.hejazi.distributed_payment_gateway.merchant_service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final MerchantRepository merchantRepository;

    @Override
    @Transactional
    public UUID findOrCreate(UUID merchantId, String email, String name, String phone) {

        if (email == null || email.isBlank()) {
            return null;
        }

        return customerRepository.findByMerchant_IdAndEmail(merchantId, email)
                .map(Customer::getId)
                .orElseGet(() -> createNew(merchantId, email, name, phone));
    }


    private UUID createNew(UUID merchantId, String email, String name, String phone) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant", merchantId));

        Customer customer = Customer.builder()
                .merchant(merchant)
                .email(email)
                .name(name)
                .phone(phone)
                .build();

        customer = customerRepository.save(customer);
        log.info("Customer created via findOrCreate id={} merchantId={} email={}",
                customer.getId(), merchantId, email);
        return customer.getId();
    }
}
