package com.izx.sharding.repository;

import com.izx.sharding.entity.Usage;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface StatisticRepository extends CrudRepository<Usage, Long> {

    List<Usage> findByStatisticTime(Date statisticTime);

    List<Usage> findByStatisticTimeBetween(Date startTime,Date endTime);
}
