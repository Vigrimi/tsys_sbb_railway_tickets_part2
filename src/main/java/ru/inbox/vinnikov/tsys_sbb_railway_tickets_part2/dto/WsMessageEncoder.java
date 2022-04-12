package ru.inbox.vinnikov.tsys_sbb_railway_tickets_part2.dto;

import com.google.gson.Gson;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import javax.json.JsonObject;

public class WsMessageEncoder implements Encoder.Text<ScheduleOnRwstationDto> {
//    private JsonObject json;
    private static Gson gson = new Gson();

    @Override
    public String encode(ScheduleOnRwstationDto o) throws EncodeException {
        return gson.toJson(o);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        System.out.println("=================------------- init");
    }

    @Override
    public void destroy() {
        System.out.println("==============---------------------- destroy");
    }
}
