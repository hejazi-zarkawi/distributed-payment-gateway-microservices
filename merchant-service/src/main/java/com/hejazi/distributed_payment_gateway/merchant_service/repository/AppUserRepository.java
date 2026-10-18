package com.hejazi.distributed_payment_gateway.merchant_service.repository;

import com.hejazi.distributed_payment_gateway.merchant_service.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findByEmail(String email);
}
