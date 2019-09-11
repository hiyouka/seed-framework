package seed.seedframework.web.core.request;

import io.netty.handler.codec.http.HttpRequest;

import java.util.Map;

/**
 * 包装request请求的参数以及一些基本信息，
 * json提交方式处理
 * @author hiyouka
 * @since JDK 1.8
 */
public interface RequestWrapper<T extends HttpRequest>{

    /**
     * httpRequest 对象
     */
    T getRequest();

    /**
     * 原始的url
     */
    String originUrl(T request);

    /**
     * 请求的路径
     */
    String path(T request);


    Map<String,Object> params(T request);


    //请求的ip
    String getRemoteIp();




}