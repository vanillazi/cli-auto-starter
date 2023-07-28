package cn.vanillazi.tool;

import org.apache.commons.io.output.CountingOutputStream;

import java.io.IOException;
import java.io.OutputStream;

public class ListenableOutputStream extends CountingOutputStream {

    private Long total;
    /**
     * Constructs a new ProxyOutputStream.
     *
     * @param proxy the OutputStream to delegate to
     */
    public ListenableOutputStream(OutputStream proxy, Long total) {
        super(proxy);
        this.total=total;
    }

    @Override
    protected void afterWrite(int n) throws IOException {
        super.afterWrite(n);
        int process= (int) (100*getCount()/total);
        var sb=new StringBuilder("[");
        for(int i=0;i<100;i++){
            if(i<=process) {
                sb.append("#");
            }else {
                sb.append("-");
            }
        }
        sb.append("]");
        System.out.printf("\r%s%d/%d",sb.toString(),getCount(),total);

    }


    @Override
    protected synchronized void beforeWrite(int n) {
        super.beforeWrite(n);
    }
}
