package com.hejazi.distributed_payment_gateway.merchant_service.mapper;

import com.hejazi.distributed_payment_gateway.merchant_service.dto.response.WebhookConfigResponse;
import com.hejazi.distributed_payment_gateway.merchant_service.entity.MerchantWebhookConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WebhookConfigMapper {
    @Mapping(target = "webhookSecret", source = "rawSecret")
    WebhookConfigResponse toResponse(MerchantWebhookConfig merchantWebhookConfig, String rawSecret);
}
