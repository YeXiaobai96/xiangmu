package com.yofc.trace.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Filename: DataVO
 * @Author: wm
 * @Date: 2019/5/17 17:07
 * @Description:
 * @History:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataVO {

    private String id;
    private Double max;
    private String time;
}
