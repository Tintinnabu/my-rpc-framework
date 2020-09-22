package top.tinn.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import top.tinn.annotation.RpcService;
import top.tinn.entity.RpcServiceProperties;
import top.tinn.factory.SingletonFactory;
import top.tinn.provider.ServiceProvider;
import top.tinn.provider.ServiceProviderImpl;
import top.tinn.registry.ServiceDiscovery;
import top.tinn.remoting.dto.RpcRequest;

/**
 * @Author: Tinn
 * @Date: 2020/8/14 10:34
 */
@Slf4j
@Component
public class SpringBeanPostProcessor implements BeanPostProcessor {
    private final ServiceProvider serviceProvider;

    public SpringBeanPostProcessor() {
        serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);
    }

    /**
     * 使用@RpcService注解注册service到ServiceProvider
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(RpcService.class)){
            log.info("[{}] is annotated with  [{}]", bean.getClass().getName(), RpcService.class.getCanonicalName());
            RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
            RpcServiceProperties rpcServiceProperties = RpcServiceProperties.builder()
                    .group(rpcService.group()).version(rpcService.version()).build();
            serviceProvider.publishService(bean, rpcServiceProperties);
        }
        return bean;
    }
}
