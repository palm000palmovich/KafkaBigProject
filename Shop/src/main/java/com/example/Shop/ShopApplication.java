package com.example.Shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopApplication.class, args);
		//TODO Про безопасность - в docker-compose все PLAINTEXT, а в задании четко написано про TLS
		//TODO Мониторинг в docker-compose поднял, но JMX Exporter не подключен. В prometheus.yml порты 9090 для kafka - это ж не JMX порты. Надо JMX Exporter агент цеплять к брокерам и правильные порты прописывать. Дашборд в Grafana тоже не сделан походу.
	}

}
