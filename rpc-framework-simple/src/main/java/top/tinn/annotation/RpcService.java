package top.tinn.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Author: Tinn
 * @Date: 2020/8/14 10:32
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
@Component
public @interface RpcService {
    /**
     * Service version, default value is empty string
     */
    String version() default "";

    /**
     * Service group, default value is empty string
     */
    String group() default "";
}
