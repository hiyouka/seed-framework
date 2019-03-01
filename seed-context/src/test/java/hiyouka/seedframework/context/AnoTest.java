package hiyouka.seedframework.context;

import hiyouka.seedframework.beans.annotation.Component;
import hiyouka.seedframework.beans.annotation.Lazy;
import hiyouka.seedframework.beans.annotation.Primary;
import hiyouka.seedframework.beans.annotation.Scope;
import hiyouka.seedframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
@Primary
@Component
@Lazy
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AnoTest {

}