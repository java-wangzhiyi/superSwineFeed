package com.jd.jrdp.exts.server.constants;

public enum HystrixCommandPropertiesEnum {

    FUXI_GROUP_KEY("fuxi-group-key","FuXiHystrixGroupKey"),
    FUXI_COMMAND_KEY("fuxi-command-key","default"),
    FUXI_THREAD_POOL_KEY1("threadPool1","FuXiHystrixThreadPool1"),
    FUXI_THREAD_POOL_KEY2("threadPool2","FuXiHystrixThreadPool2"),
    FUXI_THREAD_POOL_KEY3("threadPool3","FuXiHystrixThreadPool3"),
    FUXI_THREAD_POOL_KEY4("threadPool4","FuXiHystrixThreadPool4"),
    FUXI_THREAD_POOL_KEY5("threadPool5","FuXiHystrixThreadPool5"),
    FUXI_THREAD_POOL_KEY6("threadPool6","FuXiHystrixThreadPool6"),
    FUXI_THREAD_POOL_KEY7("threadPool7","FuXiHystrixThreadPool7"),
    FUXI_THREAD_POOL_KEY8("threadPool8","FuXiHystrixThreadPool8"),
    FUXI_THREAD_POOL_KEY9("threadPool9","FuXiHystrixThreadPool9"),
    FUXI_THREAD_POOL_KEY10("threadPool10","FuXiHystrixThreadPool10"),
    FUXI_METRICS_ROLLING_STATISTICAL_WINDOW_IN_MILLISECONDS("metricsRollingStatisticalWindowInMilliseconds","10000"),
    CIRCUIT_BREAKER_ERROR_THRESHOLD_PERCENTAGE("circuitBreakerErrorThresholdPercentage","50");

    private final String hystrixProperties;
    private final String businessName;


    HystrixCommandPropertiesEnum(String businessName,String hystrixProperties) {
        this.businessName = businessName;
        this.hystrixProperties = hystrixProperties;
    }

    public String getHystrixProperties() {
        return hystrixProperties;
    }

    public String getBusinessName() {
        return businessName;
    }
}
