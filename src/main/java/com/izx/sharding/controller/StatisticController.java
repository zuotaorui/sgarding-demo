package com.izx.sharding.controller;

import com.izx.sharding.entity.Usage;
import com.izx.sharding.service.StatisticService;
import com.izx.sharding.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistic")
public class StatisticController {

    @Autowired
    private StatisticService statisticService;

    @GetMapping
    public List<Usage> get(@RequestParam("create_at") String createAt) {
        return statisticService.findByStatisticTime(DataUtil.formatDate(createAt));
    }

    @GetMapping("/range")
    public List<Usage> getRange(@RequestParam("start_at") String startAt,
            @RequestParam("end_at") String endAt) {
        return statisticService.findByStatisticTimeBetween(DataUtil.formatDate(startAt),
                DataUtil.formatDate(endAt));
    }
}
