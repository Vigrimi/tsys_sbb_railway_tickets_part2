package ru.inbox.vinnikov.tsys_sbb_railway_tickets_part2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.time.LocalDateTime;

@SpringBootApplication
public class TsysSbbRailwayTicketsPart2Application {

	public static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TsysSbbRailwayTicketsPart2Application.class);

	public static void main(String[] args) {
		LOGGER.info("\n===========------------part2 --- started-> " + LocalDateTime.now());
		SpringApplication.run(TsysSbbRailwayTicketsPart2Application.class, args);
		LOGGER.info("\n===========------------part2 --- finished-> " + LocalDateTime.now());
	}


}
