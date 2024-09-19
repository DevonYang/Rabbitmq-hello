package com.rabbitmq.one;

import com.rabbitmq.client.*;


public class Producter {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        //创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //工厂ip,连接rabbitmq队列
        factory.setHost("localhost");
        // factory.setVirtualHost("/");
        // 默认端口是 5672， 【注意】： 15672是web管理页面的端口，这里如果填15672 将连接错误
        // factory.setPort(5672);
        //用户名
        factory.setUsername("guest");
        //密码
        factory.setPassword("guest");
        //创建连接
        Connection connection = factory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();
        /**
         *  生成一个队列，queueDeclare的参数：
         *
         * 1.队列名称
         * 2.队列里面的消息是否持久化 默认消息存储在内存中
         * 3.独占 (exclusive): false 表示队列是非独占的，可以被多个连接访问; true 表示队列是独占的，只能由声明它的连接访问，连接关闭时队列会被自动删除
         * - 独占队列（exclusive）即使设置为持久化（durable），在连接关闭时仍会被删除。
         * 4.是否自动删除 最后一个消费者端开连接以后 该队列是否自动删除 true 自动删除
         * 5.其他参数
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //发消息
        String message = "hello world!";

        /**
         * 发送一个消息
         * 1.发送到那个交换机
         * 2.路由的 key 是哪个
         * 3.其他的参数信息
         * 4.发送消息的消息体
         */
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.println("消息发送成功！");
    }
}
