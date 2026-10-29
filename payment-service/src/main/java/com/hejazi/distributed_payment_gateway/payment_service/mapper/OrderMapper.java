package com.hejazi.distributed_payment_gateway.payment_service.mapper;


import com.hejazi.distributed_payment_gateway.payment_service.dto.response.OrderResponse;
import com.hejazi.distributed_payment_gateway.payment_service.entity.OrderRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    @Mapping(source = "orderStatus", target = "status")
    OrderResponse toResponse(OrderRecord orderRecord);
}
