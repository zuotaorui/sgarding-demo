package com.izx.sharding.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "data_usage")
public class Usage {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "count")
    private Long count;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "st_time")
    private Date statisticTime;

    @Column(name = "created_at")
    private Date createTime;
}
