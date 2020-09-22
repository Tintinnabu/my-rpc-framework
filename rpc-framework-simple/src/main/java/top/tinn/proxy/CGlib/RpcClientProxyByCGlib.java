package top.tinn.proxy.CGlib;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

/**
 * @Author: Tinn
 * @Date: 2020/9/3 20:08
 */
@Slf4j
public class RpcClientProxyByCGlib{
    Class<?> superClass;
    MethodInterceptor methodInterceptor;


    public RpcClientProxyByCGlib(Class<?> superClass, MethodInterceptor methodInterceptor) {
        this.superClass = superClass;
        this.methodInterceptor = methodInterceptor;
    }

    public Object getProxy(){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(superClass);
        enhancer.setCallback(methodInterceptor);
        return enhancer.create();
    }





}
