package top.tinn.remoting.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import top.tinn.entity.RpcServiceProperties;
import top.tinn.enumeration.RpcMessageType;

import java.io.Serializable;

/**
 * @Author: Tinn
 * @Date: 2020/8/11 15:11
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RpcRequest implements Serializable {

    private static final long serialVersionUID = 3254503217523871653L;
    private String requestId;
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] paramTypes;
    private RpcMessageType rpcMessageType;
    private String version;
    private String group;

    public RpcServiceProperties toRpcProperties() {
        return RpcServiceProperties.builder().serviceName(this.getInterfaceName())
                .version(this.getVersion())
                .group(this.getGroup()).build();
    }
}
