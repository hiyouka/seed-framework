package hiyouka.seedframework.web.core.server;

import com.google.gson.reflect.TypeToken;
import com.jy.JsonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import io.netty.util.CharsetUtil;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.netty.buffer.Unpooled.copiedBuffer;

/**
 * create by jianglei on 2018/12/20
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    /*
     * 处理请求
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) {
        System.out.println(fullHttpRequest);
        InetSocketAddress socketAddress = (InetSocketAddress)channelHandlerContext.channel().remoteAddress();
        System.out.println("hostName : " + socketAddress.getHostName());
        System.out.println("address: " + socketAddress.getAddress().getHostAddress());
        System.out.println("port: " + socketAddress.getPort());
        FullHttpResponse response = null;
        if (fullHttpRequest.method() == HttpMethod.GET) {
            Map<String, Object> getParamsFromChannel = getGetParamsFromChannel(fullHttpRequest);
            System.out.println(getGetParamsFromChannel(fullHttpRequest));
            String data = "GET method over";
            ByteBuf buf = copiedBuffer(data, CharsetUtil.UTF_8);
            response = responseOK(HttpResponseStatus.OK, buf);

        } else if (fullHttpRequest.method() == HttpMethod.POST) {
            System.out.println(getPostParamsFromChannel(fullHttpRequest));
            String data = "POST method over";
            ByteBuf content = copiedBuffer(data, CharsetUtil.UTF_8);
            response = responseOK(HttpResponseStatus.OK, content);

        } else {
            response = responseOK(HttpResponseStatus.INTERNAL_SERVER_ERROR, null);
        }
        // 发送响应
        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }


    private Map<String,Object> getParamsFromUrl(FullHttpRequest request){
        Map<String,Object> params = new HashMap<String, Object>();
        HttpHeaders headers = request.headers();
        List<Map.Entry<String, String>> entries = headers.entries();
        for(Map.Entry<String,String> entry : entries){
            System.out.println(entry.getKey() + "=================" + entry.getValue());
        }
        QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
        Map<String, List<String>> paramList = decoder.parameters();
        for (Map.Entry<String, List<String>> entry : paramList.entrySet()) {
            params.put(entry.getKey(), entry.getValue().get(0));
        }
        return params;
    }

    private Map<String,Object> processUrl(String url){
        return null;
    }

    /*
     * 获取GET方式传递的参数
     */
    private Map<String, Object> getGetParamsFromChannel(FullHttpRequest fullHttpRequest) {
        if (fullHttpRequest.method() == HttpMethod.GET ) {
            // 处理get请求
            return getParamsFromUrl(fullHttpRequest);
        }
        return null;
    }

    /*
     * 获取POST方式传递的参数
     */
    private Map<String, Object> getPostParamsFromChannel(FullHttpRequest fullHttpRequest) {
        Map<String, Object> params = getParamsFromUrl(fullHttpRequest);
        if (fullHttpRequest.method() == HttpMethod.POST) {
            // 处理POST请求
            String strContentType = fullHttpRequest.headers().get("Content-Type").trim();
            if (strContentType.contains("x-www-form-urlencoded")) {
                params.putAll(getFormParams(fullHttpRequest));
            }
            else if (strContentType.contains("application/json")) {
                try {
                    params.putAll(getJSONParams(fullHttpRequest));
                } catch (UnsupportedEncodingException e) {
                    return null;
                }
            }
            else {
                return null;
            }
            return params;
        }
        return params;
    }

    /*
     * 解析from表单数据（Content-Type = x-www-form-urlencoded）
     */
    private Map<String, Object> getFormParams(FullHttpRequest fullHttpRequest) {
        Map<String, Object> params = new HashMap<String, Object>();

        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), fullHttpRequest);
        List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();

        for (InterfaceHttpData data : postData) {
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                MemoryAttribute attribute = (MemoryAttribute) data;
                params.put(attribute.getName(), attribute.getValue());
            }
        }

        return params;
    }

    /*
     * 解析json数据（Content-Type = application/json）
     */
    private Map<String, Object> getJSONParams(FullHttpRequest fullHttpRequest) throws UnsupportedEncodingException {
        Map<String, Object> params = new HashMap<String, Object>();

        ByteBuf content = fullHttpRequest.content();
        byte[] reqContent = new byte[content.readableBytes()];
        content.readBytes(reqContent);
        String strContent = new String(reqContent, "UTF-8");
        Map<String, Object> map = JsonUtils.toObjectByType(strContent, new TypeToken<Map<String, Object>>() {
        });
        for (Object key : map.keySet()) {
            params.put(key.toString(), map.get(key));
        }
        return params;
    }

    private FullHttpResponse responseOK(HttpResponseStatus status, ByteBuf content) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, content);
        if (content != null) {
            response.headers().set("Content-Type", "text/plain;charset=UTF-8");
            response.headers().set("Content_Length", response.content().readableBytes());
        }
        return response;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }


}

