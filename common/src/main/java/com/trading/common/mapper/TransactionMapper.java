package com.trading.common.mapper;

import com.trading.common.avro.Transaction;
import com.trading.common.dto.TransactionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.HashMap;
import java.util.Map;

/**
 * 交易对象映射器
 */
@Mapper
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);
    
    @Mapping(source = "transactionId", target = "transactionId")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "currency", target = "currency")
    @Mapping(source = "transactionType", target = "transactionType")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "metadata", target = "metadata")
    TransactionDTO toDTO(Transaction avro);
    
    @Mapping(source = "transactionType", target = "transactionType")
    @Mapping(source = "status", target = "status")
    Transaction toAvro(TransactionDTO dto);
    
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