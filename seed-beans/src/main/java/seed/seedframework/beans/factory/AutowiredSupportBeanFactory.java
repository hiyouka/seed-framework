package seed.seedframework.beans.factory;

import seed.seedframework.beans.metadata.DependencyDescriptor;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface AutowiredSupportBeanFactory extends BeanFactory {

    Object resolveDepend(DependencyDescriptor dsr, String beanName);

}