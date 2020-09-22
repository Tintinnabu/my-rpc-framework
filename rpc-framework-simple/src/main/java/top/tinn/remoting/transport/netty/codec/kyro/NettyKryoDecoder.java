package top.tinn.remoting.transport.netty.codec.kyro;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import top.tinn.remoting.dto.RpcResponse;
import top.tinn.serialize.Serializer;

import java.util.List;

/**
 * @Author: Tinn
 * @Date: 2020/8/12 14:40
 */
@AllArgsConstructor
@Slf4j
public class NettyKryoDecoder extends ByteToMessageDecoder {
    private final Serializer serializer;
    private final Class<?> genericClass;

    private static final int BODY_LENGTH = 4;

    /**
     * 解码 ByteBuf 对象
     *
     * @param ctx 解码器关联的 ChannelHandlerContext 对象
     * @param in  "入站"数据，也就是 ByteBuf 对象
     * @param out 解码之后的数据对象需要添加到 out 对象里面
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out){
        if (in.readableBytes() >= BODY_LENGTH){
            in.markReaderIndex();
            int dataLength = in.readInt();
            if (dataLength < 0 | in.readableBytes() < 0){
                log.error("data length or byteBuf readableBytes is not valid");
                return;
            }
            if (in.readableBytes() < dataLength){
                in.resetReaderIndex();
                return;
            }
            byte[] body = new byte[dataLength];
            in.readBytes(body);
            Object obj = serializer.deserialize(body, genericClass);
            out.add(obj);
            log.info("successful decode ByteBuf to Object");
        }
    }
}
