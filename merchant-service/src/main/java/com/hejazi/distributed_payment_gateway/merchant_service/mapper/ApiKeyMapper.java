package com.hejazi.distributed_payment_gateway.merchant_service.mapper;
import com.hejazi.distributed_payment_gateway.merchant_service.dto.response.ApiKeyResponse;
import com.hejazi.distributed_payment_gateway.merchant_service.entity.ApiKey;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ApiKeyMapper {
    List<ApiKeyResponse> toResponseList(List<ApiKey> apiKeyList);
}
