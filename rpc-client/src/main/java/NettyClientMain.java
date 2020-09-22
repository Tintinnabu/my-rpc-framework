import top.tinn.Hello;
import top.tinn.HelloService;
import top.tinn.entity.RpcServiceProperties;
import top.tinn.proxy.JDKDynamic.RpcClientProxy;
import top.tinn.remoting.transport.netty.client.ClientTransport;
import top.tinn.remoting.transport.netty.client.NettyClientTransport;

/**
 * @author shuang.kou
 * @createTime 2020年05月10日 07:25:00
 */
public class NettyClientMain {
    public static void main(String[] args) throws InterruptedException {
        ClientTransport rpcClient = new NettyClientTransport();
        RpcServiceProperties rpcServiceProperties = RpcServiceProperties.builder()
                .group("group2").version("version2").build();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient, rpcServiceProperties);
        //得到代理对象
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String hello = helloService.hello(new Hello("111", "222"));
        //如需使用 assert 断言，需要在 VM options 添加参数：-ea
        assert "Hello description is 222".equals(hello);
        Thread.sleep(12000);
        for (int i = 0; i < 10; i++) {
            helloService.hello(new Hello("111", "222"));
        }
    }
}
