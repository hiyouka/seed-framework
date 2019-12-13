package seed.seedframework.util;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public boolean match(String expression, Class clazz) throws FileNotFoundException {
        List<String> exPackageNames = new ArrayList<>();
        String[] multiplePackages = StringUtils.groupBySeparator
                (expression,ClassUtils.PACKAGE_SEPARATOR_MULTIPLE);
        System.out.println(ArrayUtils.asList(multiplePackages));
        for(String mpk : multiplePackages){
            if(!mpk.equals(ClassUtils.PACKAGE_SEPARATOR_MULTIPLE)){
                String[] packages = StringUtils.split(mpk, String.valueOf(ClassUtils.PACKAGE_SEPARATOR));
                exPackageNames.addAll(ArrayUtils.asList(packages));
            }
            else {
                exPackageNames.add(mpk);
            }
        }
        // *.com.seed..service.*
        // com.seed.seedframework.service.test.service.Service.class
        String[] packageNames = processClassArray(StringUtils.groupBySeparator
                (ClassUtils.getPackageName(clazz), String.valueOf(ClassUtils.PACKAGE_SEPARATOR)));

        String[] exPackageRegs = processClassArray(exPackageNames.toArray(new String[0]));
        int currentMatch = 0;
//        int currentMatchIndex = 0;
        StringBuilder currentPackage = null;
        for(String pk : packageNames){
            if(currentPackage == null){
                currentPackage = new StringBuilder(pk);
            }
            else {
                currentPackage.append(pk).append(ClassUtils.PACKAGE_SEPARATOR);
            }
            if(allMatch.equals(exPackageRegs[currentMatch])){
                currentMatch ++;
            }
            else if(ClassUtils.PACKAGE_SEPARATOR_MULTIPLE.equals(exPackageRegs[currentMatch])){
                currentMatch ++;
            }
            else if(isMatch(exPackageRegs[currentMatch],pk)){
                currentMatch++;
            }
        }
        if(currentMatch == exPackageRegs.length){
            return true;
        }
        return false;
    }

    private static String[] processClassArray(String[] array){
        String str = array[array.length - 1];
        String[] result = array;
        if("class".equals(str)){
            result = Arrays.copyOf(array, array.length - 1);
            result[result.length-1] = result[result.length-1] + ClassUtils.CLASS_SUFFIX;
        }
        return result;
    }

    static boolean testPath(String reg, String classPath){
        List<String> exPackageNames = new ArrayList<>();
        String[] multiplePackages = StringUtils.groupBySeparator
                (reg,ClassUtils.PACKAGE_SEPARATOR_MULTIPLE);
        System.out.println(ArrayUtils.asList(multiplePackages));
        for(String mpk : multiplePackages){
            if(!mpk.equals(ClassUtils.PACKAGE_SEPARATOR_MULTIPLE)){
                String[] packages = StringUtils.split(mpk, String.valueOf(ClassUtils.PACKAGE_SEPARATOR));
                exPackageNames.addAll(ArrayUtils.asList(packages));
            }
            else {
                exPackageNames.add(mpk);
            }
        }
        // *.com.seed..service..code.*
        // com.seed.seedframework.service.test.code.service.Service.class
        String[] packageNames = processClassArray(StringUtils.split
                (classPath, String.valueOf(ClassUtils.PACKAGE_SEPARATOR)));

        String[] exPackageRegs = processClassArray(exPackageNames.toArray(new String[0]));
        // match start and end
//        if(!isMatch(exPackageRegs[exPackageRegs.length-1],packageNames[packageNames.length-1]) ||
//                !isMatch(exPackageRegs[0],packageNames[0])){
//            return false;
//        }
//        exPackageRegs = Arrays.copyOfRange(exPackageRegs,1,exPackageRegs.length-1);
        int currentMatch = 0;
        String previousReg = null;
        for(String pk : packageNames){
//            if(currentMatch == exPackageRegs.length){
//                break;
//            }
            if(allMatch.equals(exPackageRegs[currentMatch])){
                currentMatch ++;
            }

            if(ClassUtils.PACKAGE_SEPARATOR_MULTIPLE.equals(exPackageRegs[currentMatch])){
                currentMatch ++;
            }
            if(isMatch(exPackageRegs[currentMatch],pk)){
                if(ClassUtils.PACKAGE_SEPARATOR_MULTIPLE.equals(previousReg)){
                    currentMatch ++;
                }
            }

            previousReg = exPackageRegs[currentMatch];

        }
        if(currentMatch == exPackageRegs.length){
            return true;
        }
        return false;
    }

    static String[] split(String origin,String reg){
        return origin.split(reg);
    }

    public static void test(String expression, Class<?> clazz){
        List<String> exPackageNames = new ArrayList<>();
        String[] multiplePackages = StringUtils.groupBySeparator
                (expression,ClassUtils.PACKAGE_SEPARATOR_MULTIPLE);
        System.out.println(ArrayUtils.asList(multiplePackages));
        for(String mpk : multiplePackages){
            if(!mpk.equals(ClassUtils.PACKAGE_SEPARATOR_MULTIPLE)){
                String[] packages = StringUtils.split(mpk, String.valueOf(ClassUtils.PACKAGE_SEPARATOR));
                exPackageNames.addAll(ArrayUtils.asList(packages));
            }
            else {
                exPackageNames.add(mpk);
            }
        }
        System.out.println(exPackageNames);
    }

    private static boolean isMatch(String ex, String mx){
        if(allMatch.equals(ex)){
            return true;
        }else {
            String[] exs = StringUtils.groupBySeparator(ex, allMatch);
            // match start and end
            if(exs.length != 0) {
                String start = exs[0];
                if(!start.equals(allMatch)){
                    if(mx.startsWith(start)){
                        mx = mx.replaceFirst(start,"");
                        exs = Arrays.copyOfRange(exs,1,exs.length);
                    }
                    else {
                        return false;
                    }
                }
            }
            if(exs.length != 0){
                String end = exs[exs.length - 1];
                if(!end.equals(allMatch)){
                    if(mx.endsWith(end)){
                        mx = mx.substring(0,mx.length() - end.length());
                        exs = Arrays.copyOfRange(exs,0,exs.length-1);

                    }
                    else {
                        return false;
                    }
                }
            }
            if(exs.length != 0){
                // match other
                exs = ArrayUtils.deleteElement(exs,allMatch);
                int currentMatch = 0;
                int currentMatchIndex = 0;
                char[] chars = mx.toCharArray();
                for(char c : chars){

                    if(currentMatch == exs.length){
                        break;
                    }
                    if(exs[currentMatch].charAt(currentMatchIndex) == c){
                        if(currentMatchIndex == (exs[currentMatch].length() - 1)){
                            currentMatch++;
                            currentMatchIndex = 0;
                        }
                        else{
                            currentMatchIndex++;
                        }
                    }
                    else if(currentMatchIndex != 0){
                        currentMatchIndex = 0;
                    }

                }
                return currentMatch == exs.length;
            }
            return true;
        }
    }


}