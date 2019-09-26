package seed.seedframework.core;

import seed.seedframework.util.ArrayUtils;

import java.util.Comparator;
import java.util.List;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class Ordered {

    public static final int HIGHEST_LEVEL = Integer.MIN_VALUE;

    public static final int LOWER_LEVEL = Integer.MAX_VALUE;

    public static Order[] sortOrders(Order[] orders){
        List<Order> orderList = ArrayUtils.asList(orders);
        orderList.sort(Comparator.comparingInt(Order::getOrder));
        return orderList.toArray(orders);
    }


}