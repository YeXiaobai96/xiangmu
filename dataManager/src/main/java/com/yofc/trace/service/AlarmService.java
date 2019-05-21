package com.yofc.trace.service;

import com.yofc.trace.entity.Alarm;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Filename: AlarmService
 * @Author: wm
 * @Date: 2019/5/17 8:50
 * @Description:
 * @History:
 */
@Service
public interface AlarmService {
    List<Alarm> findAlarm(List<String> list);
}
