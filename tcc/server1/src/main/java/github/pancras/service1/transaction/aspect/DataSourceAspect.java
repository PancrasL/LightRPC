package github.pancras.service1.transaction.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.sql.Connection;

import github.pancras.service1.transaction.connection.MyConnection;

@Aspect
public class DataSourceAspect {

    // 用自己的数据源代理掉默认数据源
    @Around("execution(* javax.sql.DataSource.getConnection(..))")
    public Connection around(ProceedingJoinPoint point) {
        try {
            Connection connection = (Connection) point.proceed();
            return new MyConnection(connection);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
