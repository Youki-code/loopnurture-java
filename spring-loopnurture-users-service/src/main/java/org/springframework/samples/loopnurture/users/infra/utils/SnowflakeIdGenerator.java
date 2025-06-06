package org.springframework.samples.loopnurture.users.infra.utils;

import org.springframework.stereotype.Component;

/**
 * 雪花算法ID生成器
 * 用于生成分布式系统中的唯一ID
 */
@Component
public class SnowflakeIdGenerator {
    /**
     * 开始时间戳（2024-01-01 00:00:00）
     */
    private final static long START_TIMESTAMP = 1704038400000L;

    /**
     * 机器ID所占位数
     */
    private final static long WORKER_ID_BITS = 5L;

    /**
     * 数据中心ID所占位数
     */
    private final static long DATACENTER_ID_BITS = 5L;

    /**
     * 序列号所占位数
     */
    private final static long SEQUENCE_BITS = 12L;

    /**
     * 机器ID最大值
     */
    private final static long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);

    /**
     * 数据中心ID最大值
     */
    private final static long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);

    /**
     * 序列号最大值
     */
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

    /**
     * 机器ID左移位数
     */
    private final static long WORKER_ID_SHIFT = SEQUENCE_BITS;

    /**
     * 数据中心ID左移位数
     */
    private final static long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    /**
     * 时间戳左移位数
     */
    private final static long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;

    /**
     * 工作机器ID
     */
    private long workerId;

    /**
     * 数据中心ID
     */
    private long datacenterId;

    /**
     * 序列号
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间戳
     */
    private long lastTimestamp = -1L;

    /**
     * 构造函数
     *
     * @param workerId 工作机器ID (0~31)
     * @param datacenterId 数据中心ID (0~31)
     */
    public SnowflakeIdGenerator(long workerId, long datacenterId) {
        // 参数校验
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("Worker ID can't be greater than %d or less than 0", MAX_WORKER_ID));
        }
        if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("Datacenter ID can't be greater than %d or less than 0", MAX_DATACENTER_ID));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    /**
     * 生成下一个ID
     *
     * @return 雪花算法生成的ID
     */
    public synchronized long nextId() {
        long timestamp = timeGen();

        // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过，抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format(
                "Clock moved backwards. Refusing to generate id for %d milliseconds",
                lastTimestamp - timestamp));
        }

        // 如果是同一时间生成的，则进行序列号自增
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 序列号用完了，等待下一毫秒
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            // 时间戳改变，序列号重置为0
            sequence = 0L;
        }

        // 更新上次生成ID的时间戳
        lastTimestamp = timestamp;

        // 组合生成ID
        return ((timestamp - START_TIMESTAMP) << TIMESTAMP_SHIFT)
            | (datacenterId << DATACENTER_ID_SHIFT)
            | (workerId << WORKER_ID_SHIFT)
            | sequence;
    }

    /**
     * 等待下一个毫秒的到来
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 获取当前时间戳
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }
} 