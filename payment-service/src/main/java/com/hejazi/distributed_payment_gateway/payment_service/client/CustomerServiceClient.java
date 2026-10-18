package com.hejazi.distributed_payment_gateway.payment_service.client;

import com.hejazi.distributed_payment_gateway.common_lib.dto.FindOrCreateCustomerRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "merchant-service", path = "/internal/customers")
public interface CustomerServiceClient {
    @PostMapping("/find-or-create")
    UUID findOrCreate(@RequestBody FindOrCreateCustomerRequest request);
}
