package br.com.pedro.web.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;

@Configuration
public class RabbitMQConfiguration {

    @Bean
    Queue userCreatedQueue(@Value("${queues.users.created}") String queueName) {
        return new Queue(queueName, true, false, false, null);
    }

    @Bean
    Exchange userExchange(@Value("${exchanges.users}") String exchangeName) {
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    Binding bindUserCreatedQueueToUsersExchange(Queue userCreatedQueue,
        DirectExchange usersExchange,
        @Value("${exchanges.users.routing-key}") String routingKey) {
        return BindingBuilder
            .bind(userCreatedQueue)
            .to(usersExchange)
            .with(routingKey);
    }

    @Bean
    JacksonJsonMessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    RabbitAdmin rabbitAdmin(ConnectionFactory factory) {
        return new RabbitAdmin(factory);
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
            JacksonJsonMessageConverter messageConverter) {
        var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            JacksonJsonMessageConverter messageConverter) {
        var factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setMessageConverter(messageConverter);
        return factory;
    }

    @Bean
    ApplicationListener<ApplicationReadyEvent> applicationReady(RabbitAdmin rabbitAdmin) {
        return _ -> rabbitAdmin.initialize();
    }
}
