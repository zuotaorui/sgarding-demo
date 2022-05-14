package com.izx.sharding.service;

import com.izx.sharding.entity.Usage;

import java.util.Date;
import java.util.List;

public interface StatisticService {

    List<Usage> findByStatisticTime(Date statisticTime);

    List<Usage> findByStatisticTimeBetween(Date startTime,Date endTime);
}
