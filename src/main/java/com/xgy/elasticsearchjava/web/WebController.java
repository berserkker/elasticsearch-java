package com.xgy.elasticsearchjava.web;

import com.xgy.elasticsearchjava.service.AsyncTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class WebController {
    @Autowired
    private AsyncTaskService asyncTaskService;

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public String insert(){
        /*AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AsyncTaskConfig.class);
        AsyncTaskService asyncTaskService = context.getBean(AsyncTaskService.class);*/

        //创建了50个线程
        for (int i = 1; i <= 16; i++) {
            asyncTaskService.insert(i);
        }

        return "success";

    }

}
