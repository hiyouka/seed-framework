package seed.seedframework.web.core.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;


/**
 * create by jianglei on 2018/12/20
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;

    public HttpServerInitializer(SslContext sslCtx){
        this.sslCtx = sslCtx;
    }

    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if(sslCtx != null){
            pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        }

        pipeline.addLast(new HttpRequestDecoder());
        //如果您不想处理HttpChunks，请取消注释以下行
        //p.addLast(new HttpObjectAggregator(1048576));
        // 将HTTP消息的多个部分合成一条完整的HTTP消息
        pipeline.addLast("http-aggregator", new HttpObjectAggregator(65535));
        // 响应转码器
        pipeline.addLast("http-decoder", new HttpRequestDecoder());
        pipeline.addLast("http-encoder", new HttpResponseEncoder());
        // 解决大码流的问题，ChunkedWriteHandler：向客户端发送HTML5文件
        pipeline.addLast("http-chunked", new ChunkedWriteHandler());

        //如果您不想要自动内容压缩，请删除以下行
        //p.addLast(new HttpContentCompressor());
        // 自定义处理handler
        pipeline.addLast("http-server",new HttpServerHandler());
    }

}