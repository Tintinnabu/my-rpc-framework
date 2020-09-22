package top.tinn.remoting.transport.netty.codec.kyro;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.tinn.serialize.Serializer;

/**
 * @Author: Tinn
 * @Date: 2020/8/12 14:40
 */
@Slf4j
@AllArgsConstructor
public class NettyKryoEncoder extends MessageToByteEncoder<Object> {
    private final Serializer serializer;
    private final Class<?> genericClass;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        if (genericClass.isInstance(o)){
            byte[] body = serializer.serialize(o);
            int dataLength = body.length;
            byteBuf.writeInt(dataLength);
            byteBuf.writeBytes(body);
            log.info("successful encode Object to ByteBuf");
        }
    }
}
