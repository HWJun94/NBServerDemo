package com.leisen.msgqueue;

public interface QueueMessagePorter extends Runnable{
    @Override
    void run();
}
