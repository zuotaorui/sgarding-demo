package com.izx.sharding.scheduler;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.izx.sharding.util.DataUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class InitTableScheduler implements InitializingBean {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Environment environment;

    private static final Bindable<Map<String, ShardingTable>> STRING_SHARDING_TABLE_MAP = Bindable
            .mapOf(String.class, ShardingTable.class);

    private Map<String, ShardingTable> shardingTableMap;

    //@Scheduled(cron = "：0 0 2 L * ?") 每月最后一天凌晨2点同步当月表结构至下一月分表
    //@Scheduled(cron = "0 0/1 * * * ?")
    public void dauScheduler() {
        if (!CollectionUtils.isEmpty(shardingTableMap)) {
            shardingTableMap.keySet().forEach(shardingTable -> copyTable(shardingTable, 1, 0, 0));
        }
    }

    private void copyTable(String table, Integer targetMonthDistance, Integer sourceMonthDistance,
            Integer times) {
        if (times > 10) {
            return;
        }
        Instant target =
                LocalDateTime.now().plusMonths(targetMonthDistance).atZone(ZoneId.systemDefault())
                        .toInstant();
        Instant source =
                LocalDateTime.now().plusMonths(sourceMonthDistance).atZone(ZoneId.systemDefault())
                        .toInstant();

        String targetTable = DataUtil.getTableNameByDate(table, Date.from(target), false),
                sourceTable = DataUtil.getTableNameByDate(table, Date.from(source), false);
        try {
            log.info("create table start . sourceTable:{},targetTable:{}", sourceTable,
                    targetTable);
            jdbcTemplate.execute(
                    String.format("CREATE TABLE IF NOT EXISTS %s LIKE %s", targetTable,
                            sourceTable));
            log.info("create table end. sourceTable:{},targetTable:{}", sourceTable, targetTable);
        } catch (BadSqlGrammarException be) {
            if (be.getMessage().contains("already exists")) {
                log.warn("table already create.sourceTable:{},targetTable:{}", sourceTable,
                        targetTable);
                return;
            }
            copyTable(table, targetMonthDistance, sourceMonthDistance, times + 1);
        } catch (Exception e) {
            log.error("create table is failed,targetTable:{},sourceTableName:{}", targetTable,
                    sourceTable, e);
            copyTable(table, targetMonthDistance, sourceMonthDistance, times + 1);
        }
    }

    @Override public void afterPropertiesSet() throws Exception {
        shardingTableMap = Binder.get(environment)
                .bind("spring.shardingsphere.sharding.tables", STRING_SHARDING_TABLE_MAP)
                .orElse(Collections.emptyMap());
        log.info("sharding tables:{}", shardingTableMap);
    }

    @Data
    public static class ShardingTable {
        @JsonProperty("actual-data-nodes")
        private String actualDataNodes;
    }
}
