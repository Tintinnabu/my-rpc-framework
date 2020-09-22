package top.tinn.remoting.transport.netty.client;


import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import top.tinn.enumeration.RpcMessageType;
import top.tinn.factory.SingletonFactory;
import top.tinn.remoting.dto.RpcRequest;
import top.tinn.remoting.dto.RpcResponse;

import java.net.InetSocketAddress;


/**
 * 处理服务器发送的数据
 *
 * @Author: Tinn
 * @Date: 2020/8/14 10:06
 */
@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private final UnprocessedRequests unprocessedRequests;
    private final ChannelProvider channelProvider;

    public NettyClientHandler() {
        unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
        channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
    }

    /**
     * Read the message transmitted by the server
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
       try {
           log.info("client receive msg: [{}]", msg);
           if (msg instanceof RpcResponse){
               RpcResponse<Object> rpcResponse = (RpcResponse<Object>) msg;
               //RpcResponse的接收处理
               unprocessedRequests.complete(rpcResponse);
           }
       }finally {
           ReferenceCountUtil.release(msg);
       }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE){
                //写空闲，发送HEART_BEAT请求
                log.info("write idle happen [{}]", ctx.channel().remoteAddress());
                Channel channel = channelProvider.get((InetSocketAddress) ctx.channel().remoteAddress());
                RpcRequest rpcRequest = RpcRequest.builder().rpcMessageType(RpcMessageType.HEART_BEAT).build();
                channel.writeAndFlush(rpcRequest).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
