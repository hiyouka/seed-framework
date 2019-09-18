package seed.seedframework.util;

import seed.seedframework.exception.ExpressionParseException;

/**
 *  . 一级
 *  .. 多级
 *  *.com..seed.ServiceClass
 *
 * @author hiyouka
 * @since JDK 1.8
 */
public class ClassPathMatcher implements ExpressionMatcher<Class<?>>{

    @Override
    public boolean match(String expression, Class clazz) throws ExpressionParseException {
        String packageName = ClassUtils.getPackageName(clazz);
        String[] strings = packageName.split(String.valueOf(ClassUtils.PACKAGE_SEPARATOR));
        return false;
    }
}