package seed.seedframework.aop;

import seed.seedframework.exception.ExpressionParseException;
import seed.seedframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class ExecutionExpressionMethodMatcher implements MethodMatcher {

    //          方法修饰符          返回类型          包/类路径               方法名
    //execution(modifiers-pattern? ret-type-pattern declaring-type-pattern?name-pattern(param-pattern) throws-pattern?)
    //@annotation(注解类路径)

    private String separator = " ";

    private String multistagePackage = "..";

    private String all = "*";

    private String split = ".";

    private String and = "&&";

    private String or = "||";

    private char left = '(';

    private char right = ')';

    // ((a && b) || (c || d))

//    private String[] group(String expression) {
//        String[] split = expression.split(and);
//
//        char[] chars = expression.toCharArray();
//        List<Integer> leftIndex = new ArrayList<>();
//        List<Integer> rightIndex = new ArrayList<>();
//        for(int i=0; i< chars.length; i++){
//            if(chars[i] == left){
//                leftIndex.add(i);
//            }
//            else if(chars[i] == right){
//                rightIndex.add(i);
//            }
//        }
//    }

    // * seed.seedframework.aop.adapter..*.*(..) || * seed.seedframework.aop.proxy..*.*(..)
    // * seed.seedframework.aop.adapter..*.*(..) || * seed.seedframework.aop.proxy..*.*(..)

    private boolean executeMatch(String expression, Method method) throws ClassNotFoundException {
        String[] groups = expression.split(separator);
        String modifiers;
        String returnType;
        String pattern;
        if(groups.length == 2){
            modifiers = all;
            returnType = groups[0];
            pattern = groups[1];
        }
        else if(groups.length == 3){
            modifiers = groups[0];
            returnType = groups[1];
            pattern = groups[2];
        }
        else {
            throw new ExpressionParseException(expression);
            // throw exception
        }
        String parameters = pattern.substring(pattern.indexOf(left),pattern.indexOf(right));
//        String classPath =
        if(!all.equals(modifiers)){
            if(!modifiers.equals(Modifier.toString(method.getModifiers()))){
                return false;
            }
        }
        if(!all.equals(returnType)){
            if(!ClassUtils.forName(returnType).isAssignableFrom(method.getReturnType())){
                return false;
            }
        }
//        if()
        return false;
    }


    @Override
    public boolean match(Method method) {
        return false;
    }
}