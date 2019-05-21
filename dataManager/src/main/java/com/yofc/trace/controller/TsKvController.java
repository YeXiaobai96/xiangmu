package com.yofc.trace.controller;

import com.yofc.trace.config.ExceptionEnum;
import com.yofc.trace.entity.TsKv;
import com.yofc.trace.service.AlarmService;
import com.yofc.trace.service.TsKvService;
import com.yofc.trace.util.ResultUtil;
import com.yofc.trace.util.UUIDConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Filename: TsKvHController
 * @Author: wm
 * @Date: 2019/3/20 13:41
 * @Description:设备数据控制器
 * @History:
 */
@RestController
@RequestMapping(value = "tskv")
public class TsKvController {

    @Autowired
    private TsKvService tskvService;

    @Autowired
    private AlarmService alarmService;

    @PostMapping("/list")
    public Object list(@RequestBody TsKv tsKv) {
        if (StringUtils.isBlank(tsKv.getEntityId()) || StringUtils.isBlank(tsKv.getKey())) {
            return ResultUtil.error(ExceptionEnum.PARAM_ERROR);
        }
        try {
            tsKv.setEntityId(UUIDConverter.fromTimeUUID(UUID.fromString(tsKv.getEntityId())));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(ExceptionEnum.PARAM_ERROR);
        }
        return tskvService.findTsKvDay(tsKv);
    }


    @PostMapping("/alarm")
    public Object alarm(@RequestBody TsKv tsKv) {

        if (tsKv.getEntityIdList()==null||tsKv.getEntityIdList().size()==0){
            return ResultUtil.error(ExceptionEnum.PARAM_NULL);
        }
        List<String> list= tsKv.getEntityIdList();
        List<String> list1= list.stream().map(s->{
            return UUIDConverter.fromTimeUUID(UUID.fromString(s));
        }).collect(Collectors.toList());
        return alarmService.findAlarm(list1);
    }

    @PostMapping("/findTsKvByMonth")
    public Object findTsKvByMonth(@RequestBody TsKv tsKv) {
        if (StringUtils.isBlank(tsKv.getEntityId()) || StringUtils.isBlank(tsKv.getKey())) {
            return ResultUtil.error(ExceptionEnum.PARAM_ERROR);
        }
        try {
            tsKv.setEntityId(UUIDConverter.fromTimeUUID(UUID.fromString(tsKv.getEntityId())));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(ExceptionEnum.PARAM_ERROR);
        }
        return tskvService.findTsKvByMonth(tsKv.getEntityId(),tsKv.getKey());
    }
}
