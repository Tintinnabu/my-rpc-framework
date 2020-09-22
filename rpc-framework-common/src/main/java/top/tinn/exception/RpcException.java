package top.tinn.exception;

import top.tinn.enumeration.RpcErrorMessage;

/**
 * @Author: Tinn
 * @Date: 2020/8/11 15:13
 */
public class RpcException extends RuntimeException{
    public RpcException(RpcErrorMessage rpcErrorMessage, String detail) {
        super(rpcErrorMessage.getMessage() + ":" + detail);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(RpcErrorMessage rpcErrorMessage) {
        super(rpcErrorMessage.getMessage());
    }
}
