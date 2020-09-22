package top.tinn.remoting.transport.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import top.tinn.config.CustomShutdownHook;
import top.tinn.entity.RpcServiceProperties;
import top.tinn.factory.SingletonFactory;
import top.tinn.provider.ServiceProvider;
import top.tinn.provider.ServiceProviderImpl;
import top.tinn.remoting.dto.RpcRequest;
import top.tinn.remoting.dto.RpcResponse;
import top.tinn.remoting.transport.netty.codec.kyro.NettyKryoDecoder;
import top.tinn.remoting.transport.netty.codec.kyro.NettyKryoEncoder;
import top.tinn.serialize.kyro.KryoSerializer;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

/**
 * Server. Receive the client message, call the corresponding method according to the client message,
 * and then return the result to the client.
 * @Author: Tinn
 * @Date: 2020/8/12 10:00
 */
@Slf4j
@Component
public class NettyServer implements InitializingBean {
    private final KryoSerializer kryoSerializer = new KryoSerializer();
    private final ServiceProvider serviceProvider =
            SingletonFactory.getInstance(ServiceProviderImpl.class);
    public static final int PORT = 9998;

    public void registerService(Object service){
        serviceProvider.publishService(service);
    }

    public void registerService(Object service, RpcServiceProperties rpcServiceProperties){
        serviceProvider.publishService(service, rpcServiceProperties);
    }

    public void registerServiceOnPort(Object service, RpcServiceProperties rpcServiceProperties, int port){
        serviceProvider.publishService(service, rpcServiceProperties, port);
    }

    @SneakyThrows
    public void start(){
        //本机(服务器)ip地址
        String host = InetAddress.getLocalHost().getHostAddress();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                            socketChannel.pipeline().addLast(new NettyKryoEncoder(kryoSerializer, RpcResponse.class));
                            socketChannel.pipeline().addLast(new NettyKryoDecoder(kryoSerializer, RpcRequest.class));
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
                    });
            ChannelFuture f = b.bind(host, PORT).sync();
            f.channel().closeFuture().sync();
        }catch (InterruptedException e){
            log.error("occur exception when start server:", e);
        }finally {
            log.error("shutdown bossGroup and workerGroup");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @SneakyThrows
    public void startOnPort(int port){
        String host = InetAddress.getLocalHost().getHostAddress();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                            socketChannel.pipeline().addLast(new NettyKryoEncoder(kryoSerializer, RpcResponse.class));
                            socketChannel.pipeline().addLast(new NettyKryoDecoder(kryoSerializer, RpcRequest.class));
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
                    });
            ChannelFuture f = b.bind(host, port).sync();
            f.channel().closeFuture().sync();
        }catch (InterruptedException e){
            log.error("occur exception when start server:", e);
        }finally {
            log.error("shutdown bossGroup and workerGroup");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * Called after setting all bean properties
     * w完成初始化过程后，增加销毁钩子，优雅关闭线程池，curator
     */
    @Override
    public void afterPropertiesSet() {
        CustomShutdownHook.getCustomShutdownHook().clearAll();
    }
}
