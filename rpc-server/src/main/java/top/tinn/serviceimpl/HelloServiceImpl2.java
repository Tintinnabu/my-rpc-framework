package top.tinn.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import top.tinn.Hello;
import top.tinn.HelloService;
import top.tinn.annotation.RpcService;

/**
 * @Author: Tinn
 * @Date: 2020/8/14 11:52
 */
@Slf4j
public class HelloServiceImpl2 implements HelloService {
    static {
        System.out.println("HelloServiceImpl2被创建");
    }

    @Override
    public String hello(Hello hello) {
        log.info("HelloServiceImpl2收到: {}.", hello.getMessage());
        String result = "Hello description is " + hello.getDescription();
        log.info("HelloServiceImpl2返回: {}.", result);
        return result;
    }
}
