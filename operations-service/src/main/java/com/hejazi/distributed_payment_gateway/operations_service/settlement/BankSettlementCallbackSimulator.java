package com.hejazi.distributed_payment_gateway.operations_service.settlement;


import com.hejazi.distributed_payment_gateway.common_lib.enums.SettlementStatus;
import com.hejazi.distributed_payment_gateway.operations_service.entity.Settlement;
import com.hejazi.distributed_payment_gateway.operations_service.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class BankSettlementCallbackSimulator {
    private final SettlementRepository settlementRepository;
    private final SettlementTransactionExecutor settlementTransactionExecutor;

    @Scheduled(fixedDelayString = "5000")
    @SchedulerLock(name = "operations-service-bank-settlement-simulator", lockAtMostFor = "10s", lockAtLeastFor = "1s")
    public void processCallbacks() {
        List<Settlement> settlements = settlementRepository.findByStatus(SettlementStatus.TRANSFER_PENDING);
        if (settlements.isEmpty()) return;

        for (Settlement settlement: settlements) {
            simulateCallback(settlement);
        }
    }

    private void simulateCallback(Settlement settlement) {
        log.info("Initiating settlement callback for settlementId: {}", settlement.getId());
        settlementTransactionExecutor.resolveTransfer(settlement.getId(), null, null);
    }
}
