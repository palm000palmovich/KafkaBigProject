package com.example.Shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopApplication.class, args);
		//TODO В NewItemsConsumer.java в никакой обработки ошибок, сообщение упадет и что, offset закоммитится автоматом?
		//TODO В OrderService.java просто send() без обработки результата - это fire-and-forget называется :)))) А вообще по-хорошему надо бы транзакционный producer, если у тебя запись в БД + отправка в Kafka, иначе консистентности не будет.
		//TODO Про безопасность - в docker-compose все PLAINTEXT, а в задании четко написано про TLS
		//TODO Намудрил немного с CommonVariables модулем - зачем отдельный jar? Schema Registry же существует для этого, там и версионирование схем, и compatibility checks. А у тебя JsonSerializer.ADD_TYPE_INFO_HEADERS=false - зачем отключил?
		//TODO Мониторинг в docker-compose поднял, но JMX Exporter не подключен. В prometheus.yml порты 9090 для kafka - это ж не JMX порты. Надо JMX Exporter агент цеплять к брокерам и правильные порты прописывать. Дашборд в Grafana тоже не сделан походу.
	}

}
