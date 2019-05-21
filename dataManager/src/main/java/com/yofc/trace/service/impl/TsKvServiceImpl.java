package com.yofc.trace.service.impl;

import com.yofc.trace.entity.DataVO;
import com.yofc.trace.entity.DataMonthVO;
import com.yofc.trace.entity.TsKv;
import com.yofc.trace.mapper.TsKvMapper;
import com.yofc.trace.service.TsKvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Filename: TsKvServiceImpl
 * @Author: wm
 * @Date: 2019/5/15 16:34
 * @Description:
 * @History:
 */
@Service
public class TsKvServiceImpl implements TsKvService {



    @Autowired
    private TsKvMapper tsKvMapper;

    @Override
    public List<DataVO> findTsKvDay(TsKv tskv) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Long start=c.getTimeInMillis();
        Integer now=(int)((System.currentTimeMillis()-start)/900000)+1;

        List<DataVO> list=tsKvMapper.findTsKvDay(tskv);
        List<DataVO> list1=new ArrayList<>();
        for (int i = now+1; i <= 96; i++) {
            list1.add(list.get(i-1));
        }
        for (int i = 1; i <= now; i++) {
            list1.add(list.get(i-1));
        }
        return list1;
    }

   /* public static void main(String[] args) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Long start=c.getTimeInMillis();
        Integer now=(int)((System.currentTimeMillis()-start)/900000)+1;
        System.out.println(now);
    }*/
    @Override
    public List<DataMonthVO> findTsKvByMonth(String entityId, String key) {
        Map<String,Object> map=new HashMap<>();
        map.put("time",System.currentTimeMillis()/1000);
        map.put("entityId",entityId);
        map.put("key",key);
        List<DataMonthVO> list=tsKvMapper.findTsKvByMonth(map);
        return list;
    }
}
