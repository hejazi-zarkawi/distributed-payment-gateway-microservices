package com.hejazi.distributed_payment_gateway.common_lib.dto;

import java.util.UUID;

public record WebhookTarget(
        UUID configId, String targetUrl, String webhookSecret
) {
}
