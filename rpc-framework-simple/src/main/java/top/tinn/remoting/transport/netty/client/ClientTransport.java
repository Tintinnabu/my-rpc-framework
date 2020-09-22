package top.tinn.remoting.transport.netty.client;

import top.tinn.remoting.dto.RpcRequest;

/**
 * 传输 RpcRequest
 *
 * @Author: Tinn
 * @Date: 2020/8/12 14:38
 */
public interface ClientTransport {
    /**
     * 发送消息到服务端
     *
     * @param rpcRequest
     * @return
     */
    Object sendRpcRequest(RpcRequest rpcRequest);
}
