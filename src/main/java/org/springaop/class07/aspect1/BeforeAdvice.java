package org.springaop.class07.aspect1;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @author asus
 * 通知，在每个服务执行前进行提示
 */

@Aspect
@Component

public class BeforeAdvice {

    @Pointcut(value = "execution(* org.springaop.class07.jdbc.StudentHomeworkJdbc.*(..))")
    public void pointCut(){

    }

    @Before(value = "pointCut()")
    private void beforMethod(JoinPoint joinPoint){
        //获取调用的方法名
        String methodName = joinPoint.getSignature().getName();
        System.out.println("准备调用方法:");
    }
    @After(value = "pointCut()")
    private void afterMethod(JoinPoint joinPoint){
        //获取调用的方法名
        String methodName = joinPoint.getSignature().getName();
        System.out.println("调用方法之后:");
    }
}
