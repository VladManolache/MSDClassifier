package local.multiThread.utils;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * My monitor thread. To monitor the status of {@link ThreadPoolExecutor}
 * and its status.
 */
public class MyMonitorThread implements Runnable {
	
	MyThreadPoolExecutor executor;
 
    public MyMonitorThread(MyThreadPoolExecutor executor) {
        this.executor = executor;
    }
 
    @Override
    public void run() {
    	
        try {
            do {
                System.out.println(
                    String.format("[monitor] [%d/%d] Active: %d, Completed: %d, Task: %d, isShutdown: %s, isTerminated: %s",
                        this.executor.threadPool.getPoolSize(),
                        this.executor.threadPool.getCorePoolSize(),
                        this.executor.threadPool.getActiveCount(),
                        this.executor.threadPool.getCompletedTaskCount(),
                        this.executor.threadPool.getTaskCount(),
                        this.executor.threadPool.isShutdown(),
                        this.executor.threadPool.isTerminated()));
                Thread.sleep(3000);
            }
            while (true);
        }
        catch (Exception e) {
        }
    }
}