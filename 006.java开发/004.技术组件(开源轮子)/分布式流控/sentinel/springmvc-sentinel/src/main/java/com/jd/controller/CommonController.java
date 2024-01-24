package com.jd.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


@Controller
@RequestMapping
public class CommonController {
    private static String threadName = Thread.currentThread().getName();
    private static long time1 = new Date().getTime();
    private static long time2 = new Date().getTime();
    /**
     * 主页
     * 
     * @return
     */
    @RequestMapping("/")
    public String index() {
        return "test";
    }

    /**
     * 配置2秒超时，超时后调用testFallback方法返回到error界面<br>
     * 当并发量比较大时，并非所有阻断或失败的请求都会走fallback方法，当处理线程忙不过来时，会直接抛出HystrixRuntimeException异常
     *
     * @param mav
     * @param time 睡眠时间
     * @return
     */
    @HystrixCommand(groupKey = "groupTest", commandKey = "commandTest", fallbackMethod = "testFallback"
            , commandProperties = {
            @HystrixProperty(name = "execution.timeout.enabled", value = "true"),
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000")
            }
    )
    @RequestMapping("/test")
    public ModelAndView test(ModelAndView mav, @RequestParam(defaultValue = "1") int time) throws Exception{
        int a = 0;
        try {
            a++;
            time1 = new Date().getTime();
            System.out.println(threadName+"执行了，与上一次间隔"+(time2 - time1));
            time2 = time1;
            Thread.sleep(1000 * time);
            a--;
            System.out.println(threadName+"结束了");
            System.out.println("还有"+a+"个线程未执行完毕");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        /**
         * 调用第三方业务逻辑
         *//*
        if (time != 1){
            //使用RestTemplate请求第三方接口
            ResponseEntity<String> res = null;
            try {
                long time1 = new Date().getTime();
                System.out.println("调用开始");
                RestTemplate restTemplate = new RestTemplate();
                res = restTemplate.getForEntity("http://localhost:7777/hello/hello1", String.class);
                long time2 = new Date().getTime();
                System.out.println("调用结束，用时："+(time2-time1)+"ms");
                String body = res.getBody();
                System.out.println(body);
            } catch (RestClientException e) {
                e.printStackTrace();
            }

        }*/
        mav.setViewName("success");
        System.out.println("/n"+"请求成功！！！！！！！！！！！！！！！！！！！！！！！！！！");
        return mav;
    }

    /**
     * test访问熔断后回调页面
     * 
     * @param mav
     * @param time
     * @return
     */
    protected ModelAndView testFallback(ModelAndView mav, @RequestParam(defaultValue = "1") int time) {
        System.out.println(threadName+"：-----fallback-----");
        mav.setViewName("fallback");
        return mav;
    }


}
