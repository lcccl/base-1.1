package base.framework.rpc.motan;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by cl on 2017/12/2.
 * 是否创建BasicServiceConfigBean的条件
 */
public class BasicServiceCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metaData) {
        Environment env = context.getEnvironment();
        return null != env.getProperty("motan.basicService.port");
    }

}
