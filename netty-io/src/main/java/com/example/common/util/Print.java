/**
 * Created by 尼恩 at 疯狂创客圈
 */

package com.example.common.util;


import io.netty.buffer.ByteBuf;

public class Print {

    /**
     * 信息输出
     *
     * @param s 输出的字符串形参
     */
    public static void o(Object s) {
        System.out.println(s);
    }

    /**
     * 带着方法名输出，方法名称放在前面
     *
     * @param s 待输出的字符串形参
     */
    public static void fo(Object s) {
        System.out.println(ReflectionUtil.getCallMethod() + ":" + s);
    }

    /**
     * 带着类名+方法名输出
     *
     * @param s 待输出的字符串形参
     */
    synchronized public static void cfo(Object s) {
        System.out.println(ReflectionUtil.getCallClassMethod() + ":" + s);
    }

    /**
     * 带着线程名+类名+方法名称输出
     *
     * @param s 待输出的字符串形参
     */
    synchronized public static void tcfo(Object s) {
        String cft= "["+Thread.currentThread().getName()+"|"+ReflectionUtil.getNakeCallClassMethod()+"]";
        System.out.println(cft +   "："+ s);
    }

    /**
     * 编程过程中的提示说明
     *
     * @param s 提示的字符串形参
     */
    public static void hint(Object s) {
        System.out.println("/--" + s + "--/");
    }

    public  void print(String action, ByteBuf b) {
        Logger.info("after ===========" + action + "============");
        Logger.info("1.0 isReadable(): " + b.isReadable());
        Logger.info("1.1 readerIndex(): " + b.readerIndex());
        Logger.info("1.2 readableBytes(): " + b.readableBytes());
        Logger.info("2.0 isWritable(): " + b.isWritable());
        Logger.info("2.1 writerIndex(): " + b.writerIndex());
        Logger.info("2.2 writableBytes(): " + b.writableBytes());
        Logger.info("3.0 capacity(): " + b.capacity());
        Logger.info("3.1 maxCapacity(): " + b.maxCapacity());
        Logger.info("3.2 maxWritableBytes(): " + b.maxWritableBytes());
    }
}
