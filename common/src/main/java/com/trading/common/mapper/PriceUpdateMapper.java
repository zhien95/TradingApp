package com.trading.common.mapper;

import com.trading.common.avro.PriceUpdate;
import com.trading.common.dto.PriceUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.HashMap;
import java.util.Map;

/**
 * 价格更新对象映射器
 */
@Mapper
public interface PriceUpdateMapper {
    PriceUpdateMapper INSTANCE = Mappers.getMapper(PriceUpdateMapper.class);
    
    @Mapping(source = "symbol", target = "symbol")
    @Mapping(source = "source", target = "source")
    @Mapping(source = "metadata", target = "metadata")
    PriceUpdateDTO toDTO(PriceUpdate avro);  // Avro to DTO conversion
    
    @Mapping(source = "symbol", target = "symbol")
    @Mapping(source = "source", target = "source")
    @Mapping(source = "metadata", target = "metadata")
    PriceUpdate toAvro(PriceUpdateDTO dto);  // DTO to Avro conversion
    
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