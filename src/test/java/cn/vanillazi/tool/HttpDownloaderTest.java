package cn.vanillazi.tool;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.CountingOutputStream;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpDownloaderTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        new HttpDownloaderTest().test();
    }
    @Test
    public void test() throws IOException, InterruptedException {
        var url="https://github.com/go-gost/gost/releases/download/v3.0.0-rc8/gost_3.0.0-rc8_windows_amd64.zip";
        ProxySelector proxySelector=new ProxySelector() {
            @Override
            public List<Proxy> select(URI uri) {
                var proxy=new Proxy(Proxy.Type.SOCKS,new InetSocketAddress("localhost",1080));
                return Arrays.asList(proxy);
            }

            @Override
            public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {

            }
        };
        var http= HttpClient.newBuilder()
                .proxy(proxySelector)
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();

        HttpRequest req= HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        var filename= FilenameUtils.getName(url);
        var target=Path.of("target",filename).toFile().getAbsoluteFile().toPath();
        FileUtils.createParentDirectories(target.toFile());

        var resp=http.send(req, HttpResponse.BodyHandlers.ofInputStream());
        if(resp.statusCode()==200){
            var value=resp.headers().firstValue("content-length");
            var total=-1L;
            if(value.isPresent()){
                total=Long.valueOf(value.get());
            }
            try(var in=resp.body();var output=new ListenableOutputStream(new FileOutputStream(target.toFile()),total);) {
                IOUtils.copy(in,output,8192);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}