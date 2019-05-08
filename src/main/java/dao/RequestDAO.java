package dao;

import model.entity.Request;

import java.util.List;


public interface RequestDAO {
    /**
     * Find all REQUESTS in DB
     */
    List<Request> findAll();

    /**
     * Find REQUEST by ID
     */
    Request findById(String id);

    /**
     * Insert new REQUEST
     */
    Request create(Request request);

    /**
     * Update REQUEST
     */
    Request update(Request request);

    /**
     * Delete REQUEST
     */
    void delete(Request request);
}
