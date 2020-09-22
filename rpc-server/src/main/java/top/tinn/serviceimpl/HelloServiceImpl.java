package top.tinn.serviceimpl;


import lombok.extern.slf4j.Slf4j;
import top.tinn.Hello;
import top.tinn.HelloService;
import top.tinn.annotation.RpcService;

/**
 * @author shuang.kou
 * @createTime 2020年05月10日 07:52:00
 */
@Slf4j
@RpcService(group = "group1", version = "version1")
public class HelloServiceImpl implements HelloService {

    static {
        System.out.println("HelloServiceImpl被创建");
    }

    @Override
    public String hello(Hello hello) {
        log.info("HelloServiceImpl收到: {}.", hello.getMessage());
        String result = "Hello description is " + hello.getDescription();
        log.info("HelloServiceImpl返回: {}.", result);
        return result;
    }
}
