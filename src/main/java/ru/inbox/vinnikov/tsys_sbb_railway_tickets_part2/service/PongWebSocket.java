package ru.inbox.vinnikov.tsys_sbb_railway_tickets_part2.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.inbox.vinnikov.tsys_sbb_railway_tickets_part2.dto.ScheduleOnRwstationDto;
import ru.inbox.vinnikov.tsys_sbb_railway_tickets_part2.dto.WsMessageEncoder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import static ru.inbox.vinnikov.tsys_sbb_railway_tickets_part2.TsysSbbRailwayTicketsPart2Application.LOGGER;

//@Service
@EnableKafka
public class PongWebSocket extends TextWebSocketHandler /*implements WebSocketSession*/ {
    @Autowired
    private RwStationService rwStationService;

    private final String wsKey = "sbb2key";
    private final String wsDoRefreshSchedule = "kafkaRefreshSchedule";
    // Место для сохранения сеанса подключения
    private static final ConcurrentHashMap<String, WebSocketSession> SESSION_POOL = new ConcurrentHashMap<>();
    private String rwstationName;

    public void setRwstationName(String rwstationName) throws Exception {
        this.rwstationName = rwstationName;
    }
//---------------------------------------------------------------------------------------------------------
    // соединение совершилось и отправлять сообщение клиенту в браузер во фронт
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // отобразится в хтмл в стэке
//        session.sendMessage(new TextMessage("connection established: " + rwstationName));
        SESSION_POOL.put(wsKey, session);

        // test websocket
//        handleMessageFromKafka("kafkaRefreshSchedule");
    }

    // обработка входящих сообщений от фронта на сервер
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // пришло сообщение, увидеть его в логировании
        LOGGER.info("\n------------message.getPayload->" + message.getPayload());
        // если мы отправили с фронда "test", то message.getPayload = test
        // отправить во фронт вроде
//        session.sendMessage(new TextMessage("Pong"));
        session.sendMessage(message);
    }

    // отправка сообщения с сервера в браузер кленту во фронт
    public void sendSomething(String someString) throws Exception {
        LOGGER.info("\n--------------------sendSomething(String someString) 1:");
        handleTextMessage(SESSION_POOL.get(wsKey),new TextMessage(someString));
        LOGGER.info("\n--------------------sendSomething(String someString) 9:");
//        session.sendMessage(new TextMessage(someString));
    }

    // получение Кафкой команды к действию от другого сервера и его обработка
    @KafkaListener(topics="sbb")
    public void orderListener(ConsumerRecord<Long, String> record) throws Exception {
        LOGGER.info("\n===========------------part2 --- KafkaListener started-> " + LocalDateTime.now());
//        LOGGER.info("\n---------------partition:" + record.partition());
//        LOGGER.info("\n---------------key:" + record.key());
//        LOGGER.info("\n---------------value:" + record.value()); // String или ScheduleOnRwstationDto
        LOGGER.info("\n--------------pongWebSocket.sendSomething");
        // полученную информацию из Кафки надо обработать и передать выполнение в вэбсокет
        handleMessageFromKafka(record.value());
        LOGGER.info("\n===========------------part2 --- KafkaListener finished-> " + LocalDateTime.now());
    }

    public void handleMessageFromKafka(String messageFromKafka) throws Exception {
        LOGGER.info("\n-------88-------handleMessageFromKafka:" + messageFromKafka);
        if (wsDoRefreshSchedule.equalsIgnoreCase(messageFromKafka)){
            // надо получить название станции, которая просматривается клиентом, потом обновить расписание с сервера
            // и отправить новые данные во фронт
            ArrayList<ScheduleOnRwstationDto> scheduleDto = rwStationService.getScheduleOnRwstationHandler(rwstationName);
//            LOGGER.info("\n-------93-------handleMessageFromKafka rwstationName:" + rwstationName);
//            LOGGER.info("\n-------94-------handleMessageFromKafka scheduleDto:" + scheduleDto);
            for (ScheduleOnRwstationDto schedule : scheduleDto) {
                String scheduleStr = schedule.toString();
                // отправить в вэбсокет для клиента в браузер
                sendSomething(scheduleStr);
            }
        }
        // если в пришедешем по кафке уведомлении есть текущая просматриваемая станция, то в вэбсокет отправить уведомление
        if (messageFromKafka.contains(rwstationName)){
//            sendSomething(messageFromKafka);
//            System.out.println("-----121---------messageFromKafka:" + messageFromKafka);
            ArrayList<ScheduleOnRwstationDto> scheduleDto = rwStationService.getScheduleOnRwstationHandler(rwstationName);
            // JsonArray — множество объектов типа JsonElement; можно рассматривать как List<JsonElement>; элементы
            // могут быть любой из реализаций JsonElement, причем поддерживаются смешанные типы;
            JsonArray scheduleDtoJsonArray = new JsonArray();

            for (ScheduleOnRwstationDto schedule : scheduleDto) {
                WsMessageEncoder message = new WsMessageEncoder();
                String jsonFmDto = message.encode(schedule);
                scheduleDtoJsonArray.add(jsonFmDto);
//                System.out.println("-----122---------jsonFmDto:" + jsonFmDto);
            }
//            System.out.println("-----125---------scheduleDto.get(0):" + scheduleDto.get(0));

            // один элемент
//            sendSomething(jsonFmDto);
//            System.out.println("-----137---------scheduleDtoJsonArray:" + scheduleDtoJsonArray);
//            System.out.println("-----138---------scheduleDtoJsonArray.isJsonArray:" + scheduleDtoJsonArray.isJsonArray());

            // массив
            sendSomething(String.valueOf(scheduleDtoJsonArray));
        }
    }
}
