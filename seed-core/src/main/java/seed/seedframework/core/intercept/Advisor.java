package seed.seedframework.core.intercept;

import seed.seedframework.core.Order;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface Advisor extends Order {

    Advice getAdvice();

}