/**
 * Created by 尼恩 at 疯狂创客圈
 */

package com.crazymakercircle.cocurrent;


import org.apache.log4j.Logger;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 在一个EventLoop内部线程上任务是串行的。如果一个Handler业务处理器中的
 * channelRead() 入站处理方法执行1000ms或者几秒钟，最终的结果是，阻塞了EventLoop内
 * 部线程其他几十万个通道的出站和入站处理，阻塞时长为1000ms或者几秒钟。而耗时的入
 * 站/出站处理越多，就越会拖慢整个线程的其他IO处理，最终导致严重的性能问题。
 * 就这样，严重的性能问题就出来了。咋办呢？解决办法是：业务操作和EventLoop线程
 * 相隔离。具体来说，就是专门开辟一个独立的线程池，负责一个独立的异步任务处理。对
 * 于耗时的业务操作封装成异步任务，并放入独立的线程池中去处理。这样的话，服务器端
 * 的性能会提升很多，避免了对IO操作的阻塞。
 * 有两种办法使用独立的线程池：（1）使用Netty的EventLoopGroup线程池 （2）使用
 * 自己创建的Java线程池.
 * 方法1：创建Netty的EventLoopGroup线程池，专用于处理耗时任务。
 * 方法2：创建一个专门的JAVA线程池，专用于处理耗时任务。
 */
public class FutureTaskScheduler extends Thread {
    private final Logger logger = Logger.getLogger(this.getClass());
    private ConcurrentLinkedQueue<ExecuteTask> executeTaskQueue =
            new ConcurrentLinkedQueue<ExecuteTask>();// 任务队列
    private long sleepTime = 200;// 线程休眠时间
    private ExecutorService pool = Executors.newFixedThreadPool(10);

    private static FutureTaskScheduler inst = new FutureTaskScheduler();

    private FutureTaskScheduler() {
        this.start();
    }

    /**
     * 添加任务
     *
     * @param executeTask
     */


    public static void add(ExecuteTask executeTask) {
        inst.executeTaskQueue.add(executeTask);
    }

    @Override
    public void run() {
        while (true) {
            handleTask();// 处理任务
            threadSleep(sleepTime);
        }
    }

    private void threadSleep(long time) {
        try {
            sleep(time);
        } catch (InterruptedException e) {
            logger.error(e);
        }
    }

    /**
     * 处理任务队列，检查其中是否有任务
     */
    private void handleTask() {
        try {
            ExecuteTask executeTask;
            while (executeTaskQueue.peek() != null) {
                executeTask = executeTaskQueue.poll();
                handleTask(executeTask);
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
     * 执行任务操作
     *
     * @param executeTask
     */
    private void handleTask(ExecuteTask executeTask) {
        pool.execute(new ExecuteRunnable(executeTask));
    }

    class ExecuteRunnable implements Runnable {
        ExecuteTask executeTask;

        ExecuteRunnable(ExecuteTask executeTask) {
            this.executeTask = executeTask;
        }

        public void run() {
            executeTask.execute();
        }
    }
}
