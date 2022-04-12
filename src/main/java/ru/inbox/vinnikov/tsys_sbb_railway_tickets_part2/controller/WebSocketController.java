package ru.inbox.vinnikov.tsys_sbb_railway_tickets_part2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sbb/v2")
public class WebSocketController {
    @GetMapping("/pingpong")
//    @PostMapping("/schedule_on_rwstation_handler")
    public String pingPong(){
        return
//                "schedule_on_rwstation_handler";
        "pingpong";
    }
}
