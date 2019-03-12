package hiyouka.seedframework.web.core.request;

import io.netty.handler.codec.http.HttpRequest;

import java.util.Map;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AbstractRequestWrapper<T extends HttpRequest> implements RequestWrapper<T> {

    private T request;

    public AbstractRequestWrapper(T request) {
        this.request = request;
    }

    public T getRequest() {
        return this.request;
    }

    public String originUrl(HttpRequest request) {
        String uri = request.uri();
        return uri;
    }

    public String path(HttpRequest request) {
        return null;
    }

    public Map<String, Object> params(HttpRequest request) {
        return null;
    }

    public String remoteIp() {
        return null;
    }

}
