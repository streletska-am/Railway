package dao;

import model.entity.Station;

import java.util.List;


public interface StationDAO {
    /**
     * Find all STATIONS in DB
     */
    List<Station> findAll();

    /**
     * Find STATION by ID
     */
    Station findById(String id);

    /**
     * Insert new STATION
     */
    Station create(Station station);

    /**
     * Update STATION
     */
    Station update(Station station);

    /**
     * Delete STATION
     */
    void delete(Station station);
}
