package dao;

import model.entity.Train;

import java.util.List;


public interface TrainDAO {
    /**
     * Find all TRAINS in DB
     */
    List<Train> findAll();

    /**
     * Find TRAINS by given ID of ROUTE
     */
    List<Train> findByRoute(String route_id);

    /**
     * Find TRAIN by ID
     */
    Train findById(String id);

    /**
     * Insert new TRAIN
     */
    Train create(Train train);

    /**
     * Update TRAIN
     */
    Train update(Train train);

    /**
     * Delete TRAIN
     */
    void delete(Train train);

}
