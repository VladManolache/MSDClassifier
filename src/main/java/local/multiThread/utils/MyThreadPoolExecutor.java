package local.multiThread.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyThreadPoolExecutor {
 
	int poolSize = 4;

    int maxPoolSize = 8;

    long keepAliveTime = 10000000;

    public ThreadPoolExecutor threadPool = null;
    public Thread monitor;
    
    final ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(5);

    public MyThreadPoolExecutor() {
		
        threadPool = new ThreadPoolExecutor(poolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, queue);
         
    	monitor = new Thread(new MyMonitorThread(this));
    	monitor.setDaemon(true);
		monitor.start();
    }

    public void runTask(Runnable task) {
//        System.out.println("Task count.."+threadPool.getTaskCount() );
//        System.out.println("Queue Size before assigning the task.."+queue.size() );
        threadPool.execute(task);
//        System.out.println("Queue Size after assigning the task.."+queue.size() );
//        System.out.println("Pool Size after assigning the task.."+threadPool.getActiveCount() );
//        System.out.println("Task count.."+threadPool.getTaskCount() );
//        System.out.println("Task count.." + queue.size());
    }

    public void shutDown() {
        threadPool.shutdown();
    }
}
