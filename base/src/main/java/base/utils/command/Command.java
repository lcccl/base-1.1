package base.utils.command;

import com.google.common.util.concurrent.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by cl on 2017/7/10.
 * 命令
 */
public class Command {

    private final static Log logger = LogFactory.getLog(Command.class);

    /* 默认的超时时间 */
    private final static long DEFAULT_TIMEOUT = 30L;

    /* 命令执行线程池 */
    private static ListeningExecutorService cmdExecutorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

    /* 命令字符串 */
    private String cmd;

    /* 命令执行超时时间（秒） */
    private long timeout;

    /* 命令进程 */
    private Process process;

    /* 命令异步执行结果 */
    private ListenableFuture<String> future;

    /* 命令回调队列 */
    private List<CommandCallback> callbackQueue = new ArrayList<CommandCallback>();

    public Command(String cmd) {
        this(cmd, DEFAULT_TIMEOUT);
    }

    public Command(String cmd, long timeout) {
        this.cmd = cmd;
        this.timeout = timeout;
    }

    /**
     * 执行命令
     */
    public void execute() {
        try {
            process = Runtime.getRuntime().exec(cmd);
            logger.info("command to execute: " + cmd);

            // 异步执行，接受返回结果
            future = cmdExecutorService.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    String output = IOUtils.toString(process.getInputStream(), Charset.defaultCharset().displayName());
                    process.waitFor();
                    logger.info("command " + cmd + " output: " + output);
                    return output;
                }
            });

            // 命令超时处理
            cmdExecutorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        future.get(timeout, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        future.cancel(false);
                        process.destroy();
                    }
                }
            });

            // 命令执行完成后执行回调
            Futures.addCallback(future, new FutureCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    for (CommandCallback callback : callbackQueue) {
                        callback.onSuccess(s);
                        callback.onComplete();
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    logger.error("command failure: " + throwable.getLocalizedMessage(), throwable);

                    for (CommandCallback callback : callbackQueue) {
                        callback.onFailure(throwable);
                        callback.onComplete();
                    }
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 获取执行结果（命令执行结束前，该方法会一直阻塞）
     */
    public String getResult() {
        String result = null;
        try {
            result = future.get().trim();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return result;
    }

    /**
     * 添加命令执行完成的回调
     */
    public void addCallback(CommandCallback callback) {
        callbackQueue.add(callback);
    }

    /**
     * 重设命令执行的线程池（用于命令并发执行时性能的优化）
     */
    public static void resetThreadPool(ExecutorService executorService) {
        synchronized (Command.class) {
            cmdExecutorService = MoreExecutors.listeningDecorator(executorService);
        }
    }

}
