package com.rabbitmq.four;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.utils.RabbitMqUtils;

import java.util.Properties;
import java.util.UUID;

public class ConfirmMessage {

    //单个发消息的个数
    public static final int MESSAGE_COUNT = 1000; //选中小写字母，Ctrl+Shift+U 变大写

    public static void main(String[] args) throws Exception {
        //单个确认
        // publishMessageIndividually();//发布1000个单独确认消息，耗时:599ms
        //批量确认
        //publishMessageBatch();
        //异步发布确认
        publishMessageAsync();
    }


    /*
    单个确认
     */
    public static void publishMessageIndividually() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //通过uuid 随机生成信道名字
        String queueName = UUID.randomUUID().toString();
        //队列的声明 【注意：这里的exclusive 是 true 是一个独占队列，只能由声明它的连接访问，连接关闭时队列会被自动删除】
        channel.queueDeclare(queueName,false,true,false,null);

        //【开启发布确认】
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        //批量发消息
        for (int i = 0; i < 1000; i++) {
            String message = i+"";
            channel.basicPublish("",queueName, null,message.getBytes());
            //【单个消息就马上进行发布确认】
            boolean flag = channel.waitForConfirms();
            if(flag){
                System.out.println("消息发送成功");
            }
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"个单独确认消息，耗时:"+(end-begin)+"ms");
        
    }


   /*
   批量确认
    */
    public static void publishMessageBatch() throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //队列的声明 【注意：这里的exclusive 是 true 是一个独占队列，只能由声明它的连接访问，连接关闭时队列会被自动删除】
        String queueName = UUID.randomUUID().toString();
        System.out.println(queueName);
        channel.queueDeclare(queueName, false, true, false, null);

        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        //批量确认消息大小
        int batchSize =100;

        //【批量发送消息，批量发布确认】
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message=i+"";
            channel.basicPublish("",queueName,null,message.getBytes());

            //【判断达到100条消息的时候，批量确认一次】
            if((i+1)%batchSize==0){
                //发布确认
                channel.waitForConfirms();
            }
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布1000个批量确认消息，耗时:"+(end-begin)+"ms");
    }

    /*
    异步发布确认
     */
    public static void publishMessageAsync() throws Exception{

        Channel channel = RabbitMqUtils.getChannel();
        //队列的声明 【注意：这里的exclusive 是 true 是一个独占队列，只能由声明它的连接访问，连接关闭时队列会被自动删除】
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false, true, false, null);

        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        //消息确认回调的函数
        ConfirmCallback ackCallback = (deliveryTag, multiple) ->{
            System.out.println("确认的消息:"+deliveryTag);
        };

        /**
         * 1.消息的标记
         * 2.是否为批量确认
         */
        //消息确认失败回调函数
        ConfirmCallback nackCallback= (deliveryTag,multiple) ->{
            System.out.println("未确认的消息:"+deliveryTag);
        };

        //准备消息的监听器 监听那些消息成功了，哪些消息失败了
        /**
         * 1.监听哪些消息成功了
         * 2.监听哪些消息失败了
         */
        channel.addConfirmListener(ackCallback,nackCallback);//异步通知

        //批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message=i+"消息";
            channel.basicPublish("",queueName,null,message.getBytes());
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布1000个异步发布确认消息，耗时:"+(end-begin)+"ms");
    }

}
