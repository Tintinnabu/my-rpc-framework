package top.tinn.remoting.dto;

import lombok.extern.slf4j.Slf4j;
import top.tinn.enumeration.RpcErrorMessage;
import top.tinn.enumeration.RpcResponseCode;
import top.tinn.exception.RpcException;

/**
 * @Author: Tinn
 * @Date: 2020/8/11 17:30
 */
@Slf4j
public class RpcMessageChecker {
    private static final String INTERFACE_NAME = "interfaceName";

    private RpcMessageChecker(){}

    public static <T> void check(RpcResponse<T> rpcResponse, RpcRequest rpcRequest){
        if (rpcResponse == null){
            throw new RpcException(RpcErrorMessage.SERVICE_INVOCATION_FAILURE,
                    INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
        if (!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())) {
            throw new RpcException(RpcErrorMessage.REQUEST_NOT_MATCH_RESPONSE,
                    INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        if (rpcResponse.getCode() == null || !rpcResponse.getCode().equals(RpcResponseCode.SUCCESS.getCode())) {
            throw new RpcException(RpcErrorMessage.SERVICE_INVOCATION_FAILURE,
                    INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
    }
}
