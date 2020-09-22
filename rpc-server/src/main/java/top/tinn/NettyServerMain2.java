package top.tinn;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import top.tinn.entity.RpcServiceProperties;
import top.tinn.remoting.transport.netty.server.NettyServer;
import top.tinn.serviceimpl.HelloServiceImpl2;

/**
 * @Author: Tinn
 * @Date: 2020/8/14 11:43
 */

@ComponentScan("top.tinn")
public class NettyServerMain2 {
    //启动 server
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(NettyServerMain2.class);
        NettyServer nettyServer = applicationContext.getBean(NettyServer.class);
        HelloService helloService2 = new HelloServiceImpl2();
        RpcServiceProperties rpcServiceProperties = RpcServiceProperties.builder()
                .group("group2").version("version2").build();
        int port = 8888;
        nettyServer.registerServiceOnPort(helloService2, rpcServiceProperties, port);
        nettyServer.startOnPort(port);
    }
}
