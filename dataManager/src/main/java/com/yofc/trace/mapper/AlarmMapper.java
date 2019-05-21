package com.yofc.trace.mapper;

import com.yofc.trace.entity.Alarm;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Filename: AlarmMapper
 * @Author: wm
 * @Date: 2019/5/16 17:46
 * @Description:
 * @History:
 */
@Repository
public interface AlarmMapper {

    List<Alarm> findAlarm(List<String> entityIdList);
}
