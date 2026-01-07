package com.trading.common.utils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 序列号生成工具类
 */
public class SequenceGenerator {
    private static final AtomicLong sequence = new AtomicLong(1);
    
    /**
     * 获取下一个序列号
     * @return 下一个序列号
     */
    public static long getNextSeqId() {
        return sequence.getAndIncrement();
    }
    
    /**
     * 重置序列号
     * @param startValue 起始值
     */
    public static void reset(long startValue) {
        sequence.set(startValue);
    }
    
    /**
     * 获取当前序列号值（不递增）
     * @return 当前序列号值
     */
    public static long getCurrentSeqId() {
        return sequence.get();
    }
}