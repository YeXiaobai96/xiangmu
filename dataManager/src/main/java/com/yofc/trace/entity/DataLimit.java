package com.yofc.trace.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @Filename: DataLimit
 * @Author: wm
 * @Date: 2019/5/16 11:07
 * @Description:
 * @History:
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataLimit {


    private String key;
    private double min;
    private double max;
}
