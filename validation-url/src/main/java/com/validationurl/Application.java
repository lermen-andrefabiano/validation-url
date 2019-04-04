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

	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	
	@Value("${rabbitmq.queue.number.consumers:2}")
	private int numberConsumers;

	public static void main(String[] args) {
		logger.info(">> main...");
		SpringApplication.run(Application.class, args);
		logger.info("<< main...");
	}

	private ConfigurableApplicationContext context;

	@Autowired
	public Application(ConfigurableApplicationContext context) {
		this.context = context;
	}

	@Override
	public void run(String... strings) throws Exception {
		logger.info("Create consumers");
		for (int i = 0; i < numberConsumers; i++) {
			ValidationUrlConsumer consumer = context.getBeanFactory().createBean(ValidationUrlConsumer.class);
			context.getBeanFactory().registerSingleton("ValidationUrlConsumer_" + i, consumer);
		}

		RabbitAdmin admin = context.getBean(RabbitAdmin.class);
		admin.initialize();
	}

}
