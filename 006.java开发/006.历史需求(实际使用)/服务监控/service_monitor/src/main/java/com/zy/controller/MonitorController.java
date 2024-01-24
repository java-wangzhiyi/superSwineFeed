package com.zy.controller;

import com.zy.server.Server;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 服务器监控
 *
 * @author 志懿
 */
@RequestMapping("/monitor")
@Controller
public class MonitorController
{

    private String prefix = "monitor/server";

    @GetMapping("/server")
    public String server(ModelMap mmap) throws Exception
    {
        Server server = new Server();
        server.copyTo();
        mmap.put("server", server);
        return prefix + "/server";
    }
}
