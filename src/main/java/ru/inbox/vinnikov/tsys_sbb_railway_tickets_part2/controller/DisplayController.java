package ru.inbox.vinnikov.tsys_sbb_railway_tickets_part2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.inbox.vinnikov.tsys_sbb_railway_tickets_part2.dto.ScheduleOnRwstationDto;
import ru.inbox.vinnikov.tsys_sbb_railway_tickets_part2.service.PongWebSocket;
import ru.inbox.vinnikov.tsys_sbb_railway_tickets_part2.service.RwStationService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@Controller
@RequestMapping("/sbb/v2")
public class DisplayController {
    private final RwStationService rwStationService;
    private final PongWebSocket pongWebSocket;
    private final ArrayList<String> allS = new ArrayList<>(Arrays.asList("BASEL","BARGEN","BAD ZURZACH","ZURICH","INNSBRUCK",
        "JEGENSTORF","LUZERN","MOOSSEEDORF","BERN","DAVOS","GENEVA","ANNECY"));

    @Autowired
    public DisplayController(RwStationService rwStationService,PongWebSocket pongWebSocket) {
        this.rwStationService = rwStationService;
        this.pongWebSocket = pongWebSocket;
    }

    //------------------------------------------------------
    // Расписание по станции
    // Список актуальных станций: ANNECY; BAD ZURZACH; BARGEN; BASEL; BERN; DAVOS; GENEVA; INNSBRUCK; JEGENSTORF;
    // LUZERN; MOOSSEEDORF; ZURICH;
    @GetMapping("/")
    public String getStartForm(Model model) {
        String allStationsNames = rwStationService.getAllStationsNamesStr();
//        model.addAttribute("allstations", allStationsNames);
//        return "startdisplay";

//        ArrayList<String> allS = new ArrayList<>(Arrays.asList(allStationsNames.split(";")));
        model.addAttribute("allsts", allS);
        return "startdisplaymap";
    }

    @PostMapping("/schedule_on_rwstation_handler")
    public String userScheduleOnRwstationHandler(String rwstationName,Model model) throws Exception {
//        model.addAttribute("result", result);
        String textForScoreboard = "Информация по станции: " + rwstationName.toUpperCase();
        pongWebSocket.setRwstationName(rwstationName.toUpperCase());
//        System.out.println("**************-----tablo------BAD ZURZACH textForScoreboard:" + textForScoreboard);
        model.addAttribute("rwstationName", textForScoreboard);
        ArrayList<ScheduleOnRwstationDto> scheduleDto = rwStationService.getScheduleOnRwstationHandler(rwstationName);
//        System.out.println("----------PostMapping schedule_on_rwstation_handler--------");
        model.addAttribute("schedules", scheduleDto);
        return "schedule_on_rwstation_handler";
    }

    /*gg
//    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
//    @SendTo("/schedule_on_rwstation_handler") // отправить ответы обратно клиенту
    public static String *//*Model*//* greeting(ScheduleOnRwstationDto schedule,Model model) throws Exception {
        //allStationsNames = rwStationService.getAllStationsNamesStr();
        model.addAttribute("websocket", schedule);

        //return new Model("Hello, " + message.getName() + "!");
        return "schedule_on_rwstation_handler";
    }*/
}
