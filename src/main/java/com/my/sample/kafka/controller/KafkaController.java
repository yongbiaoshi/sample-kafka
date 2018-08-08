package com.my.sample.kafka.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("kafka")
public class KafkaController {
    @Resource
    KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("send")
    public String send() {
        kafkaTemplate.send("t-ag-1", System.currentTimeMillis() + "", "测试信息发送" + RandomStringUtils.randomAlphanumeric(5));
        return "success";
    }
}
