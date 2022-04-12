package ru.inbox.vinnikov.tsys_sbb_railway_tickets_part2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.inbox.vinnikov.tsys_sbb_railway_tickets_part2.dto.ScheduleOnRwstationDto;
import ru.inbox.vinnikov.tsys_sbb_railway_tickets_part2.entity.RailwayStationBahnhof;
import ru.inbox.vinnikov.tsys_sbb_railway_tickets_part2.entity.TimetableZeitplan;
import ru.inbox.vinnikov.tsys_sbb_railway_tickets_part2.repository.RwStationRepository;
import ru.inbox.vinnikov.tsys_sbb_railway_tickets_part2.repository.TimetableRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import static ru.inbox.vinnikov.tsys_sbb_railway_tickets_part2.TsysSbbRailwayTicketsPart2Application.LOGGER;

@Service
public class RwStationService {
    private final RwStationRepository rwStationRepository;
    private final TimetableRepository timetableRepository;
    private final String endStationRU = "-=КОНЕЧНАЯ=-";

    @Autowired
    public RwStationService(RwStationRepository rwStationRepository,TimetableRepository timetableRepository) {
        this.rwStationRepository = rwStationRepository;
        this.timetableRepository = timetableRepository;
    }

    //----------------------------------------------------------------
    public String getAllStationsNamesStr() {
        ArrayList<RailwayStationBahnhof> allStationsList = rwStationRepository.findAll();
        ArrayList<String> allStationsNamesList = new ArrayList<>();
        for (RailwayStationBahnhof st : allStationsList) {
            allStationsNamesList.add(st.getNameRailwayStationBahnhof());
        }
        Collections.sort(allStationsNamesList);
        String allStationsNames = "Список актуальных станций: ";
        if (allStationsNamesList.isEmpty()){
            allStationsNames = "В базу не внесено ни одной станции!";
        } else {
            for (String rwStation : allStationsNamesList) {
                allStationsNames = allStationsNames + rwStation + "; ";
            }
        }
        return allStationsNames.trim();
    }

    public ArrayList<ScheduleOnRwstationDto> getScheduleOnRwstationHandler(String rwstationName){
        LOGGER.info("---start----------------------------------getScheduleOnRwstationHandler-------" + LocalDateTime.now());
        ArrayList<ScheduleOnRwstationDto> schedulesDtoList = new ArrayList<>();
        // искать станцию в БД - нужен будет айди
        RailwayStationBahnhof stationFromDB = rwStationRepository.findByRwStationName(rwstationName);
        LOGGER.info(LocalDateTime.now() + "\n --------getUserScheduleOnRwstationHandler---stationFromDB->" + stationFromDB);

        if (stationFromDB == null){
            ScheduleOnRwstationDto schedule = new ScheduleOnRwstationDto();
            schedule.setTrainNumber("N/A");
            schedule.setPreviousStationName("N/A");
            schedule.setCurrentStationName("N/A");
            schedule.setNextStationName("N/A");
            schedule.setCurrentStationArrTime("N/A");
            schedule.setCurrentStationDepTime("N/A");
            schedulesDtoList.add(schedule);
        } else {
            long rwstationId = stationFromDB.getId();
            // взять из базы все расписания по станции
            ArrayList<TimetableZeitplan> schedulesListFromDB =
                    timetableRepository.findAllByCurrentRwstationId(rwstationId);
            System.out.println("=======getScheduleOnRwstationHandler==================schedulesListFromDB:\n" );
                 //   + schedulesListFromDB);
            // перебрать каждое расписание из БД в ДТО
            if (schedulesListFromDB.isEmpty()){ // станция есть в БД, но по ней нет расписания
                ScheduleOnRwstationDto schedule = new ScheduleOnRwstationDto();
                schedule.setTrainNumber             ("по");
                schedule.setPreviousStationName     ("данной");
                schedule.setCurrentStationArrTime   ("станции");
                schedule.setCurrentStationDepTime   ("нет");
                schedule.setNextStationName         ("поездов");

                schedule.setCurrentStationName("N/A");
                schedulesDtoList.add(schedule);
            } else {
                for (TimetableZeitplan timetable : schedulesListFromDB) {
                    ScheduleOnRwstationDto schedule = new ScheduleOnRwstationDto();
                    schedule.setTrainNumber(timetable.getTrainIdZugId().getNumberTrainNummerZug());
                    if(timetable.getPreviousRwstationIdBahnhofId().getNameRailwayStationBahnhof()
                            .equals(timetable.getCurrentRwstationIdBahnhofId().getNameRailwayStationBahnhof())){
                        schedule.setPreviousStationName("=--->");
                        schedule.setCurrentStationArrTime("=--->");
                        schedule.setNextStationName(timetable.getNextRwstationIdBahnhofId().getNameRailwayStationBahnhof());
                        schedule.setCurrentStationDepTime(timetable.getTrainDepartureTimeZugesAbfahrtszeit());
                    } else
                    if (timetable.getCurrentRwstationIdBahnhofId().getNameRailwayStationBahnhof()
                            .equals(timetable.getNextRwstationIdBahnhofId().getNameRailwayStationBahnhof())){
                        schedule.setPreviousStationName(timetable.getPreviousRwstationIdBahnhofId().getNameRailwayStationBahnhof());
                        schedule.setCurrentStationArrTime(timetable.getTrainArrivalTimeZugesAnkunftszeit());
                        schedule.setCurrentStationDepTime("----");
                        schedule.setNextStationName(endStationRU);
                    } else {
                        schedule.setPreviousStationName(timetable.getPreviousRwstationIdBahnhofId().getNameRailwayStationBahnhof());
                        schedule.setCurrentStationName(timetable.getCurrentRwstationIdBahnhofId().getNameRailwayStationBahnhof());
                        schedule.setNextStationName(timetable.getNextRwstationIdBahnhofId().getNameRailwayStationBahnhof());
                        schedule.setCurrentStationArrTime(timetable.getTrainArrivalTimeZugesAnkunftszeit());
                        schedule.setCurrentStationDepTime(timetable.getTrainDepartureTimeZugesAbfahrtszeit());
                    }
                    schedulesDtoList.add(schedule);
                }
            }
        }
        LOGGER.info("---finish----------------------------------getScheduleOnRwstationHandler-------" + LocalDateTime.now());
        return schedulesDtoList;
    }
}
