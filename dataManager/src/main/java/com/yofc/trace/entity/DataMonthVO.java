package com.yofc.trace.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Filename: MDataVO
 * @Author: wm
 * @Date: 2019/5/17 19:33
 * @Description:
 * @History:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataMonthVO {

    private String id;
    private String time;
    private Double max;
    private Double min;
    private Double avg;
    private Double maxavg;
    private Double minavg;
}
