package com.validationurl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.validationurl.consumer.ValidationUrlConsumer;

@SpringBootApplication
@EnableRabbit
public class Application implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	@Value("${rabbitmq.queue.number.consumers:1}")
	private int numberConsumers;

	public static void main(String[] args) {
		LOGGER.info(">> Iniciou aplicação...");
		SpringApplication.run(Application.class, args);
		LOGGER.info("<< Iniciou aplicação...");
	}

	private ConfigurableApplicationContext context;

	@Autowired
	public Application(ConfigurableApplicationContext context) {
		this.context = context;
	}

	@Override
	public void run(String... strings) throws Exception {
		LOGGER.debug("Create consumers = {}", this.numberConsumers);
		for (int i = 0; i < this.numberConsumers; i++) {
			ValidationUrlConsumer consumer = this.context.getBeanFactory().createBean(ValidationUrlConsumer.class);
			this.context.getBeanFactory().registerSingleton("ValidationUrlConsumer_" + i, consumer);
		}

		RabbitAdmin admin = this.context.getBean(RabbitAdmin.class);
		admin.initialize();
	}

}
