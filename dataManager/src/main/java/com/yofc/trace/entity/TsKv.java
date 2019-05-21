package com.yofc.trace.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Filename: TsKv
 * @Author: wm
 * @Date: 2019/5/15 15:53
 * @Description:
 * @History:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TsKv {
    private String entityId;
    private String entityType;
    private String key;
    private String ts;
    private String strV;
    private Boolean boolV;
    private Double longV;
    private Double dblV;
    private Integer type;
    private Date time;
    private List<String> entityIdList;
}
