package cn.vanillazi.tool;

import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;

@Deprecated
public class HttpClientSocketTest {

    @Test
    public void test(){
        final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", new PlainConnectionSocketFactory(){
                            @Override
                            public Socket createSocket(HttpContext context) throws IOException {
                                return super.createSocket(context);
                            }
                        })
                        //.register("https", sslSocketFactoryCopy)
                        .build());
        HttpClientBuilder.create()
                .setConnectionManager(cm);
    }
}
