package com.izx.sharding.algorithm;

import com.google.common.collect.Range;
import com.izx.sharding.util.DataUtil;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Date;

public class StatisticShardingAlgorithm
        implements PreciseShardingAlgorithm<Date>, RangeShardingAlgorithm<Date> {

    /**
     * 精确分片算法类名称，用于=和IN
     *
     * @param collection
     * @param preciseShardingValue
     * @return
     */
    @Override
    public String doSharding(Collection<String> collection,
            PreciseShardingValue<Date> preciseShardingValue) {

        if (!CollectionUtils.isEmpty(collection)) {
            String logicTable = collection.stream().findFirst().get();
            return DataUtil.getTableByDate(logicTable, preciseShardingValue.getValue());
        } else {
            throw new IllegalArgumentException(
                    "sharding jdbc not find logic table,please check config");
        }

    }

    /**
     * 范围分片算法类名称，用于BETWEEN，可选
     *
     * @param collection
     * @param rangeShardingValue
     * @return
     */
    @Override public Collection<String> doSharding(Collection<String> collection,
            RangeShardingValue<Date> rangeShardingValue) {
        if (!CollectionUtils.isEmpty(collection)) {
            String logicTable = collection.stream().findFirst().get();
            Range<Date> range = rangeShardingValue.getValueRange();
            Date start = range.lowerEndpoint();
            Date end = range.upperEndpoint();

            return DataUtil.getTableSet(logicTable, start, end);
        } else {
            throw new IllegalArgumentException(
                    "sharding jdbc not find logic table,please check config");
        }

    }
}
