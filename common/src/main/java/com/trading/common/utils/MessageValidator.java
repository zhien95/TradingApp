package com.trading.common.utils;

import com.trading.common.dto.OrderUpdateDTO;
import com.trading.common.dto.PriceUpdateDTO;
import com.trading.common.dto.RiskAlertDTO;
import com.trading.common.dto.TransactionDTO;

/**
 * 消息验证工具类
 */
public class MessageValidator {
    
    /**
     * 验证交易消息
     * @param transaction 交易DTO
     * @return 验证结果
     */
    public static boolean validateTransaction(TransactionDTO transaction) {
        return transaction != null &&
               transaction.getSeqId() != null &&
               transaction.getTransactionId() != null &&
               transaction.getUserId() != null &&
               transaction.getAmount() != null &&
               transaction.getAmount() > 0;
    }
    
    /**
     * 验证价格更新消息
     * @param priceUpdate 价格更新DTO
     * @return 验证结果
     */
    public static boolean validatePriceUpdate(PriceUpdateDTO priceUpdate) {
        return priceUpdate != null &&
               priceUpdate.getSeqId() != null &&
               priceUpdate.getSymbol() != null &&
               priceUpdate.getPrice() != null &&
               priceUpdate.getPrice() > 0;
    }
    
    /**
     * 验证订单更新消息
     * @param orderUpdate 订单更新DTO
     * @return 验证结果
     */
    public static boolean validateOrderUpdate(OrderUpdateDTO orderUpdate) {
        return orderUpdate != null &&
               orderUpdate.getSeqId() != null &&
               orderUpdate.getOrderId() != null &&
               orderUpdate.getUserId() != null &&
               orderUpdate.getSymbol() != null &&
               orderUpdate.getQuantity() != null &&
               orderUpdate.getQuantity() > 0;
    }
    
    /**
     * 验证风险警报消息
     * @param riskAlert 风险警报DTO
     * @return 验证结果
     */
    public static boolean validateRiskAlert(RiskAlertDTO riskAlert) {
        return riskAlert != null &&
               riskAlert.getSeqId() != null &&
               riskAlert.getAlertId() != null &&
               riskAlert.getUserId() != null &&
               riskAlert.getScore() != null &&
               riskAlert.getScore() >= 0;
    }
    
    /**
     * 检查序列号连续性
     * @param expectedSeqId 期望的序列号
     * @param actualSeqId 实际的序列号
     * @return 是否连续
     */
    public static boolean isSequential(long expectedSeqId, long actualSeqId) {
        return actualSeqId == expectedSeqId;
    }
    
    /**
     * 检查序列号是否已处理（去重）
     * @param processedSeqIds 已处理的序列号集合
     * @param seqId 待检查的序列号
     * @return 是否已处理
     */
    public static boolean isProcessed(java.util.Set<Long> processedSeqIds, Long seqId) {
        return processedSeqIds != null && processedSeqIds.contains(seqId);
    }
}