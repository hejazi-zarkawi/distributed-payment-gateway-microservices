package com.hejazi.distributed_payment_gateway.operations_service.repository;


import com.hejazi.distributed_payment_gateway.operations_service.entity.DlqEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DlqEventRepository extends JpaRepository<DlqEvent, UUID> {
}
