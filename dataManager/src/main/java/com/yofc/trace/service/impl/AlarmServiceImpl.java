package com.yofc.trace.service.impl;

import com.yofc.trace.entity.Alarm;
import com.yofc.trace.mapper.AlarmMapper;
import com.yofc.trace.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Filename: AlarmServiceImpl
 * @Author: wm
 * @Date: 2019/5/17 8:50
 * @Description:
 * @History:
 */
@Service
public class AlarmServiceImpl implements AlarmService {

    @Autowired
    private AlarmMapper alarmMapper;
    @Override
    public List<Alarm> findAlarm(List<String> list) {
        return alarmMapper.findAlarm(list);
    }
}
