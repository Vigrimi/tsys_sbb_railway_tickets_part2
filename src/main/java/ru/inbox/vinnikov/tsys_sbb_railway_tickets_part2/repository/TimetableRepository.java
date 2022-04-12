package ru.inbox.vinnikov.tsys_sbb_railway_tickets_part2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.inbox.vinnikov.tsys_sbb_railway_tickets_part2.entity.TimetableZeitplan;

import java.util.ArrayList;

@Repository
public interface TimetableRepository extends JpaRepository<TimetableZeitplan, Long> {
    @Query(value = "SELECT * FROM sbb.sbb_timetable_zeitplan WHERE current_rwstation_id_bahnhof_id = :rwstationId"
            , nativeQuery = true)
    ArrayList<TimetableZeitplan> findAllByCurrentRwstationId(long rwstationId);

//    @Query(value = "SELECT * FROM sbb.sbb_timetable_zeitplan", nativeQuery = true)
//    ArrayList<TimetableZeitplan> findAll();
}