package com.rabbitmq.five;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.utils.RabbitMqUtils;

public class ReceiveLogs02 {
    //交换机名字
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws  Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //声明一个临时队列
        /*
        临时队列说明：
            1.生成的临时队列，队里名是随机的
            2.当消费者断开与队列的连接时，队列就会自动删除
         */
        String queueName = channel.queueDeclare().getQueue();
        //绑定交换机与队列
        channel.queueBind(queueName,EXCHANGE_NAME,"");
        System.out.println("等待接收消息,把接收到的消息打印在屏幕........... ");

        //接收消息
        DeliverCallback deliverCallback = ( consumerTag,  message)->{
            System.out.println("ReceiveLogs02接收的消息："+ new String(message.getBody(),"UTF-8"));

        };

        channel.basicConsume(queueName,true,deliverCallback,consumerTag -> {});

    }
}
