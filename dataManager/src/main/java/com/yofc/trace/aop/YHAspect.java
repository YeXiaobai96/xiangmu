package com.yofc.trace.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import com.yofc.trace.entity.Result;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Configuration
public class YHAspect {
	
	/**
	 * 找到controller中所有的方法
	 */
	@Pointcut("execution(* com.yofc.trace.controller.*Controller.*(..))")
	public void excudeService(){
		
	}
	
	/**
	 * 统一处理包装controller中的返回值
	 * 同时处理异常
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) {
    	try{
    		Object result = pjp.proceed();
    		if(result instanceof Result)	
    			return result;
    		return new Result(result);
    	}catch (Throwable e) {
    		log.error("运行异常，e={}",e);
    		return new Result(false, 500, e.getLocalizedMessage(), null);
		}
   }

}
