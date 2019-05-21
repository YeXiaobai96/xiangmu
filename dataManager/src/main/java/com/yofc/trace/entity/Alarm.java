package com.yofc.trace.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Filename: Alarm
 * @Author: wm
 * @Date: 2019/5/16 17:39
 * @Description:告警记录实体
 * @History:
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alarm {
    private String id;
    private String ackTs;
    private Long clearTs;
    private String additionalInfo;
    private Long endTs;
    private String originatorId;
    private String originatorType;
    private String propagate;
    private String severity;
    private String startTs;
    private String status;
    private String tenantId;
    private String type;
}
