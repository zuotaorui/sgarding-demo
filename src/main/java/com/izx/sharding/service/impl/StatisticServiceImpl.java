package com.izx.sharding.service.impl;

import com.izx.sharding.entity.Usage;
import com.izx.sharding.repository.StatisticRepository;
import com.izx.sharding.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class StatisticServiceImpl implements StatisticService {

    @Autowired
    private StatisticRepository repository;

    @Override
    public List<Usage> findByStatisticTime(Date createTime) {
        return repository.findByStatisticTime(createTime);
    }

    @Override
    public List<Usage> findByStatisticTimeBetween(Date startTime, Date endTime) {
        return repository.findByStatisticTimeBetween(startTime, endTime);
    }
}
