package com.ouyang.rabbitmq;

import com.ouyang.rabbitmq.entity.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpAdmin amqpAdmin;

    /**
     * 1、单播（点对点）
     */
    @Test
    public void contextLoads() {
        //Message需要自己构造一个;定义消息体内容和消息头
        //rabbitTemplate.send(exchage,routeKey,message);

        //object默认当成消息体，只需要传入要发送的对象，自动序列化发送给rabbitmq；
        //rabbitTemplate.convertAndSend(exchage,routeKey,object);
        Map<String,Object> map = new HashMap<>();
        map.put("msg","这是第一个消息");
        map.put("data", Arrays.asList("helloworld",123,true));
        //对象被默认序列化以后发送出去
        rabbitTemplate.convertAndSend("exchange.drect","ouyang.news",new Book("西游记","吴承恩"));
        //rabbitTemplate.convertAndSend("exchange.drect","ouyang.news",map);
    }

    /**
     * 测试接受消息
     */
    @Test
    public void receive(){
        Object o = rabbitTemplate.receiveAndConvert("ouyang.news");
        Object o1 = rabbitTemplate.receiveAndConvert("ouyang");
        Object o2 = rabbitTemplate.receiveAndConvert("ouyang2.news");
        Object o3 = rabbitTemplate.receiveAndConvert("ouyang.message");
        //System.out.println(o.getClass());
        System.out.println(o);
        System.out.println(o1);
        System.out.println(o2);
        System.out.println(o3);

    }

    /**
     * 测试fanout广播
     */
    @Test
    public void testFanout(){
        rabbitTemplate.convertAndSend("exchange.fanout","",new Book("三国演义","吴冠中"));
    }

    /**
     * 测试topic 订阅
     */
    @Test
    public void testTopic(){
        rabbitTemplate.convertAndSend("exchange.topic","ouyang.news",new Book("西游记","吴承恩"));
    }


    /**
     * 测试amqpAdmin 创建交换器 exchange
     */
    @Test
    public void testAmqpAdmin(){
        //创建交换器
        //amqpAdmin.declareExchange(new FanoutExchange());
        amqpAdmin.declareExchange(new DirectExchange("amqp.exchange"));//创建一个direct类型的交换器
        //创建队列
        amqpAdmin.declareQueue(new Queue("amqp.queue",true));
        //创建绑定规则
        amqpAdmin.declareBinding(new Binding("amqp.queue", Binding.DestinationType.QUEUE,
                "amqp.exchange","amqp.exchange",null));
    }

}

