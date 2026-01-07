package com.trading.common.mapper;

import com.trading.common.avro.OrderUpdate;
import com.trading.common.dto.OrderUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单更新对象映射器
 */
@Mapper
public interface OrderUpdateMapper {
    OrderUpdateMapper INSTANCE = Mappers.getMapper(OrderUpdateMapper.class);
    
    @Mapping(source = "orderId", target = "orderId")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "symbol", target = "symbol")
    @Mapping(source = "orderType", target = "orderType")
    @Mapping(source = "side", target = "side")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "metadata", target = "metadata")
    OrderUpdateDTO toDTO(OrderUpdate avro);
    
    @Mapping(source = "orderType", target = "orderType")
    @Mapping(source = "side", target = "side")
    @Mapping(source = "status", target = "status")
    OrderUpdate toAvro(OrderUpdateDTO dto);
    
    default String mapCharSequenceToString(CharSequence value) {
        return value != null ? value.toString() : null;
    }
    
    default Map<String, String> mapMetadata(Map<CharSequence, CharSequence> metadata) {
        if (metadata == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<CharSequence, CharSequence> entry : metadata.entrySet()) {
            result.put(entry.getKey() != null ? entry.getKey().toString() : null, 
                      entry.getValue() != null ? entry.getValue().toString() : null);
        }
        return result;
    }
}