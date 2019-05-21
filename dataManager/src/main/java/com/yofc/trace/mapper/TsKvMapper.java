package com.yofc.trace.mapper;

import com.yofc.trace.entity.DataVO;
import com.yofc.trace.entity.DataMonthVO;
import com.yofc.trace.entity.TsKv;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Filename: TsKvMapper
 * @Author: wm
 * @Date: 2019/5/15 15:57
 * @Description:
 * @History:
 */
@Repository
public interface TsKvMapper {


    List<DataVO> findTsKvDay(TsKv tskv);


    List<DataMonthVO> findTsKvByMonth(Map<String,Object> map);
}
