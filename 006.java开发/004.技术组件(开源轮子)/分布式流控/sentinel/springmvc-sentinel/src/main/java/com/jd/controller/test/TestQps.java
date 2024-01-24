package com.jd.controller.test;


import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


import java.net.URL;

public class TestQps {
    public static void main(String[] args) throws Exception {
        final URL url = new URL("http://localhost:8080/springmvc_sentinel_war/test?time=5");
        int req;
        for (req = 0; req < 10; req++) {
            new Thread() {
                @Override
                public void run() {
                    for (int i = 0; i < 10; i++) {
                        try {
                            RestTemplate restTemplate = new RestTemplate();
                            ResponseEntity<String> forEntity = restTemplate.getForEntity(url.toURI(), String.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
            System.out.println("第"+req+"次");
            /*if (req == 6){
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> forEntity = restTemplate.getForEntity(url.toURI(), String.class);
            }*/
        }
    }

}

