package com.yofc.trace.service;

import com.alibaba.fastjson.JSONObject;
import com.yofc.trace.entity.DataVO;
import com.yofc.trace.entity.DataMonthVO;
import com.yofc.trace.entity.TsKv;

import java.util.List;
import java.util.Map;

/**
 * @Filename: TsKvService
 * @Author: wm
 * @Date: 2019/5/15 16:34
 * @Description:
 * @History:
 */
public interface TsKvService {

    List<DataVO> findTsKvDay(TsKv tskv);


    List<DataMonthVO> findTsKvByMonth(String entityId, String key);
}
