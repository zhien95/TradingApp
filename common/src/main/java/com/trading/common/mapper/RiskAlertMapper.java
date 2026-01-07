package com.trading.common.mapper;

import com.trading.common.avro.RiskAlert;
import com.trading.common.dto.RiskAlertDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.HashMap;
import java.util.Map;

/**
 * 风险警报对象映射器
 */
@Mapper
public interface RiskAlertMapper {
    RiskAlertMapper INSTANCE = Mappers.getMapper(RiskAlertMapper.class);
    
    @Mapping(source = "alertId", target = "alertId")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "riskType", target = "riskType")
    @Mapping(source = "severity", target = "severity")
    @Mapping(source = "metadata", target = "metadata")
    RiskAlertDTO toDTO(RiskAlert avro);
    
    @Mapping(source = "riskType", target = "riskType")
    @Mapping(source = "severity", target = "severity")
    RiskAlert toAvro(RiskAlertDTO dto);
    
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