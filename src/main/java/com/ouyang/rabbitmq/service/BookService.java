package com.ouyang.rabbitmq.service;

import com.ouyang.rabbitmq.entity.Book;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    @RabbitListener(queues = "ouyang")
    public void receive(Book book){
        System.out.println(book);
    }

    @RabbitListener(queues = "ouyang.news")
    public void receive2(Message message){
        System.out.println(message.getBody());
        System.out.println(message.getMessageProperties());
    }

}
