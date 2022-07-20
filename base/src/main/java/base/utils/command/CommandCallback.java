package base.utils.command;

/**
 * Created by cl on 2017/7/10.
 * 命令回调
 */
public interface CommandCallback {

    /**
     * 命令执行成功
     */
    void onSuccess(String result);

    /**
     * 命令执行失败
     */
    void onFailure(Throwable throwable);

    /**
     * 命令执行完成，成功/失败都会执行完成方法
     */
    void onComplete();

}
