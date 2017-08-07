package cn.edu.swpu.cins.weike.Aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class WebLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(WebLogAspect.class);


    @Pointcut(value = "execution(public * cn.edu.swpu.cins.weike.web.*.*(..))")
    public void controllerLog(){


    }

    @Before("controllerLog()")
    public void beforeMethod(JoinPoint point){

        logger.info("controller aspect beginning");
        Object[] args = point.getArgs();
        for (Object arg : args) {
            logger.info("arg : " + arg);
        }
        String method = point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName();
        logger.info("aspect finishing");
        logger.info("calling " + method);
    }

    @AfterReturning(pointcut = "controllerLog()", returning = "ret")
    public void afterReturning(Object ret) {
        logger.info("controller return " + ret);
    }

    @AfterThrowing(pointcut = "controllerLog()", throwing = "throwable")
    public void afterThrowing(Throwable throwable) {
        logger.info("controller throw " + throwable.getMessage());
    }

}
