package ru.inbox.vinnikov.tsys_sbb_railway_tickets_part2.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity // Сущность Станция
@Table(name = "sbb_railwaystation_bahnhof")
@Getter
@Setter
//@ToString
public class RailwayStationBahnhof extends SuperclassForEntity {
    // из суперкласса придёт айди и версия

    // название жд станции
    @Column(name = "name_rwstation_bahnhof", nullable = false, unique = true)
    private String nameRailwayStationBahnhof;

    @Override
    public String toString() {
        return "\nRailwayStation_Bahnhof{" + "id='" + getId() + '\'' + ", version='" + getVersion() + '\'' +
                ", name_RailwayStation_Bahnhof='" + nameRailwayStationBahnhof + '\'' +
                '}';
    }
}
