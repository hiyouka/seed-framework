package hiyouka.seedframework.core.io.resource;

import com.sun.istack.internal.Nullable;
import hiyouka.seedframework.util.Assert;
import hiyouka.seedframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AntPathMatcher implements PathMatcher{

    private String pathSeparator;

    private boolean trimTokens = false;

    /**
     * 表达式缓存的最大数量
     */
    private static final int CACHE_TURNOFF_THRESHOLD = 65536;

    private volatile Boolean cachePatterns = true;

    private final Map<String, String[]> tokenizedPatternCache = new ConcurrentHashMap<>(256);

    public AntPathMatcher() {
        this.pathSeparator = StringUtils.FOLDER_SEPARATOR;
    }
    public AntPathMatcher(String pathSeparator) {
        Assert.notNull(pathSeparator, "'pathSeparator' is required");
        this.pathSeparator = pathSeparator;
    }


    public void setPathSeparator(@Nullable String pathSeparator) {
        this.pathSeparator = (pathSeparator != null ? pathSeparator : StringUtils.FOLDER_SEPARATOR);
    }

    public void setTrimTokens(boolean trimTokens) {
        this.trimTokens = trimTokens;
    }


    @Override
    public boolean isPattern(String path) {
        return (path.indexOf('*') != -1);
    }


    @Override
    public boolean match(String pattern, String path) {
        return doMatch(pattern,path);
    }

    protected boolean doMatch(String pattern, String path){
        String[] pattDirs = tokenizePattern(pattern);
        String[] pathDirs = tokenizePath(path);
        if(pattDirs.length == 0 || pathDirs.length == 0)
            return false;
        int pattIdxStart = 0;
        int pattIdxEnd = pattDirs.length - 1;
        int pathIdxStart = 0;
        int pathIdxEnd = pathDirs.length - 1;
        while (pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd) {
            String pattDir = pattDirs[pattIdxStart];
            if ("**".equals(pattDir)) {
                break;
            }
            if("*".equals(pattDir) || (pattIdxStart == pathIdxEnd && pattDir.contains("*"))){
                pattIdxStart++;
                pathIdxStart++;
                continue;
            }
            if(!StringUtils.equals(pattDir, pathDirs[pathIdxStart])){
                return false;
            }
            pattIdxStart++;
            pathIdxStart++;
        }
        String suf = pattDirs[pattIdxEnd];
        String pathSuf = pathDirs[pathIdxEnd];
        if(suf.contains("*")){
            //classpath:hiyouka/seedframework/**/resource/*.class
            suf = StringUtils.replace(suf,"*","");
            return pathSuf.endsWith(suf);
        }else {
            return StringUtils.equals(suf, pathSuf);
        }
    }


    protected String[] tokenizePattern(String pattern) {
        String[] tokenized = null;
        Boolean cachePatterns = this.cachePatterns;
        if (cachePatterns.booleanValue()) {
            tokenized = this.tokenizedPatternCache.get(pattern);
        }
        if (tokenized == null) {
            tokenized = tokenizePath(pattern);
            if (cachePatterns == null && this.tokenizedPatternCache.size() >= CACHE_TURNOFF_THRESHOLD) {
                deactivatePatternCache();
                return tokenized;
            }
            if (cachePatterns.booleanValue()) {
                this.tokenizedPatternCache.put(pattern, tokenized);
            }
        }
        return tokenized;
    }

    protected String[] tokenizePath(String path) {
        return StringUtils.tokenizeToStringArray(path, this.pathSeparator, this.trimTokens, true);
    }

//    public boolean matchStrings(String str, @Nullable Map<String, String> uriTemplateVariables) {
//        Matcher matcher = this.pattern.matcher(str);
//        Pattern.
//        if (matcher.matches()) {
//            if (uriTemplateVariables != null) {
//                // SPR-8455
//                if (this.variableNames.size() != matcher.groupCount()) {
//                    throw new IllegalArgumentException("The number of capturing groups in the pattern segment " +
//                            this.pattern + " does not match the number of URI template variables it defines, " +
//                            "which can occur if capturing groups are used in a URI template regex. " +
//                            "Use non-capturing groups instead.");
//                }
//                for (int i = 1; i <= matcher.groupCount(); i++) {
//                    String name = this.variableNames.get(i - 1);
//                    String value = matcher.group(i);
//                    uriTemplateVariables.put(name, value);
//                }
//            }
//            return true;
//        }
//        else {
//            return false;
//        }
//    }

    /**
     * 关闭缓存
     */
    private void deactivatePatternCache() {
        this.cachePatterns = false;
        this.tokenizedPatternCache.clear();
    }



}