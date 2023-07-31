package cn.vanillazi.tool;

public interface CommandProcessListener {

    public void onStarted();
    public void onFinished();
    public void onError(String msg,Throwable e);
}
