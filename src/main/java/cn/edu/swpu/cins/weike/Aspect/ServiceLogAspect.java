
package cn.edu.swpu.cins.weike.Aspect;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLogAspect {

    private Log logger = LogFactory.getLog(ServiceLogAspect.class);

    @Pointcut("execution(public * cn.edu.swpu.cins.weike.service.*.*(..))")
    public void service(){}

    @Before("service()")
    public void before(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        String method = signature.getDeclaringTypeName() + '.' + signature.getName();
        logger.info("\n");
        logger.info("calling : " + method);
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            logger.info("arg : " + arg);
        }
        logger.info("\n");

    }

    @AfterReturning(pointcut = "service()", returning = "ret")
    public void afterReturn(Object ret) {
        logger.info("\n");
        logger.info("service return : " + ret);
        logger.info("\n");
    }

}

