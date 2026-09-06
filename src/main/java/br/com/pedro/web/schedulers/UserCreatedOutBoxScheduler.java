package br.com.pedro.web.schedulers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.pedro.core.domain.EventRepository;

@Component
public class UserCreatedOutBoxScheduler {

    private final EventRepository events;
    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final String routingKey;
    private final Logger logger;

    public UserCreatedOutBoxScheduler(@Autowired EventRepository events,
            @Autowired RabbitTemplate rabbitTemplate,
            @Autowired @Value("${exchanges.users}") String exchangeName,
            @Autowired @Value("${exchanges.users.routing-key}") String routingKey) {
        this.events = events;
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
        this.logger = LoggerFactory.getLogger(UserCreatedOutBoxScheduler.class);
    }

    @Scheduled(initialDelay = 5000, fixedDelay = 5000)
    public void execute() {
        this.logger.info("Handling users created event");
        var events = this.events.readAllByType("events:user-created");
        this.logger.info("Found {} users created events", events.size());
        for (var event : events) {
            this.logger.info("Handling user created event with id: {}",
                    event.getId());
            this.logger.info("User created event details: {}", event.getContent());
            var message = new Message(event.getContent().getBytes());
            message.getMessageProperties().setContentEncoding("UTF-8");
            message.getMessageProperties().setContentType("application/json");
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            this.rabbitTemplate.send(this.exchangeName, this.routingKey, message);
            this.events.remove(event);
            this.logger.info("User created event with id: {} handled and deleted",
                    event.getId());
        }
    }
}
