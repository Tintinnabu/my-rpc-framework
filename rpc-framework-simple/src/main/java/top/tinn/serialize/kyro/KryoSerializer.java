package top.tinn.serialize.kyro;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.extern.slf4j.Slf4j;
import top.tinn.exception.SerializeException;
import top.tinn.remoting.dto.RpcRequest;
import top.tinn.remoting.dto.RpcResponse;
import top.tinn.serialize.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


/**
 * @Author: Tinn
 * @Date: 2020/8/12 14:42
 */
@Slf4j
public class KryoSerializer implements Serializer {
    private final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
            Kryo kryo = new Kryo();
            kryo.register(RpcRequest.class);
            kryo.register(RpcResponse.class);
            return kryo;
    });

    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)){
            Kryo kryo = kryoThreadLocal.get();
            kryo.writeObject(output, obj);
            //清理弱引用
            kryoThreadLocal.remove();
            return output.toBytes();
        }catch (Exception e){
            throw new SerializeException("Serialization failed");
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            // byte->Object:从byte数组中反序列化出对对象
            Object o = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();
            return clazz.cast(o);
        } catch (Exception e) {
            throw new SerializeException("Deserialization failed");
        }
    }
}
