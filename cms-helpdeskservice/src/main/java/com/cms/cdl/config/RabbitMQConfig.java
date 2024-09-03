package com.cms.cdl.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {
    @Value("${spring.rabbitmq.queue}")
    private String emailQueue;

    @Value("${spring.rabbitmq.exchange}")
    private String emailExchange;

    @Value("${spring.rabbitmq.routingkey}")
    private String emailRoutingKey;
    
    
    
  
   @Bean
     Queue emailQueue(){
        return new Queue(emailQueue);
    }


    @Bean
     DirectExchange emailExchange(){
        return new DirectExchange(emailExchange);
    }


    @Bean
     Binding emailBinding(){
        return BindingBuilder.bind(emailQueue())
                .to(emailExchange())
                .with(emailRoutingKey);
    }


    @Bean
    AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
    	
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

    @Bean
    MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }
}