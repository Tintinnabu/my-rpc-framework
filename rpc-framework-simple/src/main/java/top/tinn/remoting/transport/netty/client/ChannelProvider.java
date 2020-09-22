package top.tinn.remoting.transport.netty.client;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import top.tinn.factory.SingletonFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Tinn
 * @Date: 2020/8/14 10:08
 */
@Slf4j
public class ChannelProvider {

    private final Map<String, Channel> channelMap;
    private final NettyClient nettyClient;

    public ChannelProvider() {
        channelMap = new ConcurrentHashMap<>();
        nettyClient = SingletonFactory.getInstance(NettyClient.class);
    }

    public Channel get(InetSocketAddress inetSocketAddress){
        String key = inetSocketAddress.toString();
        if (channelMap.containsKey(key)){
            Channel channel = channelMap.get(key);
            if (channel != null && channel.isActive()){
                return channel;
            } else {
                channelMap.remove(key);
            }
        }
        //不存在就重新创建一个channel
        Channel channel = nettyClient.doConnect(inetSocketAddress);
        channelMap.put(key, channel);
        return channel;
    }

    public void remove(InetSocketAddress inetSocketAddress){
        String key = inetSocketAddress.toString();
        channelMap.remove(key);
        log.info("Channel map size :[{}]", channelMap.size());
    }
}
