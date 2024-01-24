package com.jd.jrdp.exts.server.config;


import com.jd.jrdp.exts.server.code.PlatformStatusCode;
import com.jd.jrdp.exts.server.constants.HystrixCommandPropertiesEnum;
import com.jd.jrdp.exts.server.constants.LogConst;
import com.jd.jrdp.exts.server.exception.BizException;
import com.netflix.hystrix.*;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;

/**
 * 业务熔断降级类
 */
public class FuXiHystrixCommand extends HystrixCommand<Map<String, Object>> {
    protected static Logger log = LoggerFactory.getLogger(LogConst.JDT_LOG_STR);
    private final String hystrixThreadPoolKey;
    private final int timeoutInMilliseconds;
    private final int requestVolumeThreshold;
    private final int sleepWindowInMilliseconds;
    private boolean circuitBreakerForceOpen;

    /**
     * @param hystrixThreadPoolKey      命令线程池
     * @param timeoutInMilliseconds     超时时间 ms
     * @param requestVolumeThreshold    熔断触发的最小个数/10s
     * @param sleepWindowInMilliseconds 熔断多少秒后去尝试请求
     * @param circuitBreakerForceOpen   是否强制打开断路器
     */
    public FuXiHystrixCommand(
            String hystrixThreadPoolKey,
            int timeoutInMilliseconds,
            int requestVolumeThreshold,
            int sleepWindowInMilliseconds,
            boolean circuitBreakerForceOpen
    ) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(HystrixCommandPropertiesEnum.FUXI_GROUP_KEY.getHystrixProperties()))
                /**
                 * 配置命令名称(没什么用处，基本就是为了区分不同的接口)，一般不用配置，即以下属性代表全局
                 */
                //.andCommandKey(HystrixCommandKey.Factory.asKey(""))
                /**
                 * 配置当前线程池名称
                 */
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(hystrixThreadPoolKey))
                /**
                 * 配置当前线程池属性
                 * @HystrixThreadPoolProperties.class 可根据此类查看默认参数
                 */
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        /**
                         * 设置线程池的线程数量
                         */
                        .withCoreSize(10)
                        /**
                         * 设置线程池的最大数量
                         */
                        .withMaximumSize(10)
                        /**
                         * 当前线程池无可用线程时，请求等待队列最大容量
                         */
                        .withMaxQueueSize(10000)
                        /**
                         * 此属性设置队列大小拒绝阈值,即使未达到maxQueueSize也将发生拒绝的人为最大队列大小
                         */
                        .withQueueSizeRejectionThreshold(10000)

                )
                /**
                 * 设置当前 Command 的属性
                 */
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        /**
                         * 隔离策略，有THREAD和SEMAPHORE
                         * THREAD - 它在单独的线程上执行，并发请求受线程池中的线程数量的限制
                         * SEMAPHORE - 它在调用线程上执行，并发请求受到信号量计数的限制
                         * 默认使用THREAD模式，以下几种场景可以使用SEMAPHORE模式：
                         * 只想控制并发度
                         * 外部的方法已经做了线程隔离
                         * 调用的是本地方法或者可靠度非常高、耗时特别小的方法（如medis）
                         */
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                        /**
                         * 属性如果为真，强制断路器进入打开（跳闸）状态，其中它将拒绝所有请求。
                         */
                        .withCircuitBreakerForceOpen(circuitBreakerForceOpen)
                        /**
                         * 定义业务服务调用的响应时间 ms
                         */
                        .withExecutionTimeoutInMilliseconds(timeoutInMilliseconds)
                        /**
                         * 当调用出现错误时，开启一个时间窗,在这个时间窗内进行统计 ms
                         */
                        .withMetricsRollingStatisticalWindowInMilliseconds(Integer.parseInt(HystrixCommandPropertiesEnum.FUXI_METRICS_ROLLING_STATISTICAL_WINDOW_IN_MILLISECONDS.getHystrixProperties()))
                        /**
                         * 在当前时间窗内的最小请求数,达到这个请求数量，Hystrix才进行统计 int
                         */
                        .withCircuitBreakerRequestVolumeThreshold(requestVolumeThreshold)
                        /**
                         * 设置在当前窗口内的错误请求百分比，达到即拉闸 %
                         */
                        .withCircuitBreakerErrorThresholdPercentage(Integer.parseInt(HystrixCommandPropertiesEnum.CIRCUIT_BREAKER_ERROR_THRESHOLD_PERCENTAGE.getHystrixProperties()))
                        /**
                         * 熔断多少秒后去尝试请求（跳闸后活动窗口的长度） ms
                         */
                        .withCircuitBreakerSleepWindowInMilliseconds(sleepWindowInMilliseconds)
                ));
        this.timeoutInMilliseconds = timeoutInMilliseconds;
        this.requestVolumeThreshold = requestVolumeThreshold;
        this.sleepWindowInMilliseconds = sleepWindowInMilliseconds;
        this.hystrixThreadPoolKey = hystrixThreadPoolKey;
    }

    @Override
    protected Map<String, Object> getFallback() {
        log.info("======================================fallback==================================");
        Throwable executionException = super.getExecutionException();
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        //非Hystrix造成的异常
        if (super.isFailedExecution()) {
            if (executionException instanceof BizException) throw (BizException) executionException;
            stringObjectHashMap.put("msg", "调用失败: " + "业务系统异常");
            stringObjectHashMap.put("Hystrix熔断器是否开启", super.isCircuitBreakerOpen());
            log.info("异常原因：{}, 断路器{}开启", stringObjectHashMap.get("msg"), super.isCircuitBreakerOpen() ? "已" : "未");
            return stringObjectHashMap;
        }
        //Hystrix引起的异常
        stringObjectHashMap.put("code", PlatformStatusCode.CURRENCY_CODE_OPERATION_FAILED);
        if (executionException instanceof HystrixTimeoutException) stringObjectHashMap.put("msg", "hystrix调用超时");
        else if (executionException instanceof RejectedExecutionException)
            stringObjectHashMap.put("msg", "hystrix触发限流");
        else if (executionException instanceof HystrixRuntimeException) stringObjectHashMap.put("msg", "hystrix触发短路");
        /**
         *  限流的前置还可以增加截流，被限流调的请求很可能不断地不断地再次请求，服务器也要一遍一遍的返回安抚结果(返回个简单页面啊 或者基本的信息啊)，
         *  也会增加服务器负担，当某个相同的请求发送超过一定阈值，直接对该请求截流，在客户端就截掉，使之到不了网络层，限制住。
         */
        else stringObjectHashMap.put("msg", "hystrix触发截流");
        stringObjectHashMap.put("12345", "上山打老虎");
        stringObjectHashMap.put("Hystrix熔断器是否开启", super.isCircuitBreakerOpen());
        log.info("异常原因：{}, 断路器{}开启", stringObjectHashMap.get("msg"), super.isCircuitBreakerOpen() ? "已" : "未");
        return stringObjectHashMap;
    }

    @Override
    protected Map<String, Object> run() throws Exception {
        // TODO 子类实现业务逻辑
        return null;
    }
}







