package top.tinn;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import top.tinn.entity.RpcServiceProperties;
import top.tinn.remoting.transport.netty.server.NettyServer;
import top.tinn.serviceimpl.HelloServiceImpl2;

import java.util.Scanner;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: Tinn
 * @Date: 2020/8/14 11:43
 */

@ComponentScan("top.tinn")
public class NettyServerMain {
    //启动 server
    public static void main(String[] args) {
        //仅仅加载SpringBeanPostProcessor、NettyServer两个类
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(NettyServerMain.class);
        //NettyServer是由Spring容器加载的，调用getBean方法得到NettyServer
        NettyServer nettyServer = applicationContext.getBean(NettyServer.class);
        HelloService helloService2 = new HelloServiceImpl2();
        RpcServiceProperties rpcServiceProperties = RpcServiceProperties.builder()
                .group("group2").version("version2").build();
        nettyServer.registerService(helloService2, rpcServiceProperties);
        nettyServer.start();
    }
}
